package cn.tongdun.www.realtime

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import cn.tongdun.www.function.velocityEntryFunction._
import cn.tongdun.www.function.Utils._

/**
 * Created by wangqiaoshi on 15/8/25.
 */
object velocityEntry {

  def main(args: Array[String]) {

    if (args.length < 5) {
      System.err.println("Usage: KafkaWordCount <zkQuorum> <group> <topics> <numThreads> <conf file>")
      System.exit(1)
    }

    val Array(zkQuorum, group, topics, numThreads,conf) = args
//    val ssc = StreamingContext.getOrCreate("velocity",
//      () => {
//        createContext(zkQuorum,group,topics,numThreads.toInt,"velocity")
//      })
    val command=loadPropertie(conf)

     val ssc = createContext(zkQuorum,group,topics,numThreads.toInt,"velocity",command)

    ssc.start()
    ssc.awaitTermination()




  }

}
