package cn.tongdun.www.function


import com.datastax.driver.core.{Session, Cluster, PoolingOptions}
import com.datastax.spark.connector.SomeColumns
import com.fasterxml.jackson.databind.ObjectMapper
import com.redis.RedisClientPool
import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Time, Seconds, StreamingContext}
import scala.collection.mutable
import scala.collection.mutable.Map
import com.datastax.spark.connector.streaming._

import cn.tongdun.www.function.Utils._





/**
 * Created by wangqiaoshi on 15/8/25.
 */
object velocityEntryFunction {


  import cn.tongdun.www.function.Settings._
  import cn.tongdun.www.service.ResultManager
  val clients = new RedisClientPool("192.168.6.52",6379)

  /**
   *
   * @param zkQuorum
   * @param group
   * @param topics
   * @param numThreads
   * @return
   */

  def createContext(zkQuorum: String, group: String, topics: String, numThreads: Int, checkpointDirectory: String, command: Map[String, String])
  : StreamingContext = {

    //.setMaster("local[4]").setAppName("ve")volecityRealTime

    val sparkConf = new SparkConf().setAppName("ve").setMaster("local[4]")
    sparkConf.set("spark.cassandra.connection.host", "192.168.6.52")
    sparkConf.set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    sparkConf.set("spark.cassandra.output.batch.size.bytes","524288")
    sparkConf.set("spark.cassandra.connection.timeout_ms", sparkcassandraconnectionkeep_alive_ms.toString)
    sparkConf.set("spark.cassandra.connection.keep_alive_ms", sparkcassandraconnectionkeep_alive_ms.toString)

    // Create the context with a 1 second batch size
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    ssc.checkpoint(checkpointDirectory)


    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap

    val rawStream = (1 to 3).map{_=>KafkaUtils.createStream(ssc, zkQuorum, group, topicMap)
    }
    val unionDStream = ssc.union(rawStream)



//.filter(values => filter(values._2)).

//.flatMap(iter=>smartJson(iter._2))
    val distinctGroupy = unionDStream.mapPartitions(r=>parseToPolicyAndDataV2(r)).groupByKey()
    val intervalResult = distinctGroupy.map(ips => compute( ips._1, ips._2))

    val state=intervalResult.updateStateByKey(updateResult,new HashPartitioner(ssc.sparkContext.defaultParallelism),true)
    state.checkpoint(Seconds(10))
//.repartition(partitionNum).x
    state.map(r=>(r._1,r._2.inputBegintime,r._2.inputEndtime,r._2.computeTime,r._2.toString)).saveToCassandra("volecityrealtime","velocityresult",SomeColumns("timerule","inputbegintime","inputendtime","computetime","result"))

    ssc
  }






  def function(r:(String,ResultManager)): (String,Long,Long,Long,String) ={

    val result=(r._1,r._2.inputBegintime,r._2.inputEndtime,r._2.computeTime,r._2.toString)
    println(result)
    result
  }


  import cn.tongdun.www.service.IntervalCompute._
  /**
   *
   *

   * @param lineValues
   */
  def compute(groupbyKey: String, lineValues: Iterable[mutable.Map[String, String]]): (String, (Long, Long, String)) = {



    val keys=groupbyKey.split(",")
    val rules=keys(0).split(":")
    val computeFiledName=rules(1)
    val computeType=rules(2)
    val timeRange=rules(3)

    var result:(Long,Long,String)=null
    computeType match {
      case "distinctcount"=>
        result=distinctcount(computeFiledName,lineValues)
      case "sum"=>
        result=sum(computeFiledName,lineValues)
      case "count"=>
        result=count(computeFiledName,lineValues)
      case "max"=>
        result=max(computeFiledName,lineValues)
      case "min"=>
        result=min(computeFiledName,lineValues)
      case "avg"=>
        result=avg(computeFiledName,lineValues)
      case "median"=>
        result=median(computeFiledName,lineValues)
      case "std"=>
        result=std(computeFiledName,lineValues)
      case _=>
    }
    (groupbyKey,result)
  }

  val updateResult = (iterable: Iterator[(String, Seq[( Long, Long, String)], Option[(ResultManager)])]) => {

    //获得是具体的某个指标,根据key得出是什么计算类型的
    val result=iterable.map { case (groupbyKey: String, newValues: Seq[( Long, Long, String)], nowvalue: Option[ResultManager]) =>
    {
      val keys=groupbyKey.split(",")
      val rules=keys(0).split(":")
      val computeFiledName=rules(1)
      val computeType=rules(2)
      val timeRange=rules(3)


      val resultManager = nowvalue.getOrElse(new ResultManager(groupbyKey))
      resultManager.rangTime = timeRange.toLong
      resultManager.computeType=computeType
      resultManager.addResult(computeType,newValues)
      resultManager.substractResult(computeType)
      (groupbyKey,resultManager)

    }

  }



    result

   }










}
