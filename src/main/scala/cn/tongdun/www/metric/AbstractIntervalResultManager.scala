package cn.tongdun.www.metric

import net.minidev.json.parser.JSONParser
import net.minidev.json.{JSONStyle, JSONValue, JSONObject}
import org.apache.spark.Logging

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.StringBuilder
import scala.collection.mutable
import scala.collection.mutable.{HashMap, MutableList}
import cn.tongdun.www.function.Utils._

/**
 * Created by wangqiaoshi on 15/7/29.
 */
trait  AbstractIntervalResultManager[T] extends Logging{
  private var historyMap_ = new HashMap[Long,MutableList[T]]


  def historyMap: HashMap[Long,MutableList[T]]={
    if(historyMap_ == null) {
      historyMap_ =new HashMap[Long,MutableList[T]]
    }
    historyMap_
  }

   def subtractIntervalResult(outTime:Long): T ={
     val dimensions = historyMap.get(outTime).get
     if(dimensions.size==1)historyMap.remove(outTime).get.head
     else{

       val newDimensions=dimensions.tail
       historyMap.put(outTime,newDimensions)
       dimensions.head

     }
   }
//
  def addIntervalResult(currTime:Long,dimension:T): Unit ={
  historyMap.get(currTime) match{
    case Some(dimensions)=>
      dimensions.+=(dimension)
      historyMap.put(currTime,dimensions)

    case None=>
      val dimensions=new MutableList[T]()
      dimensions.+=(dimension)
      historyMap.put(currTime,dimensions)
  }

     dimension
   }

}

import cn.tongdun.www.redis.RedisConnector._
trait IntervalResultManager[T] {
  var key=""

  def subtractIntervalResult(outTime:Long): String={
    var result=""
     clients.withClient{client=>
       client.hget(key,outTime) match {

         case dimensionstr=>
           val dimensions=dimensionstr.get.split(",")
           if(dimensions.size==1){
             client.hdel(key,outTime)
             result=dimensions.head
           }
           else {
             client.hset(key,outTime,dimensions.slice(1,dimensions.size).mkString(","))
             result=dimensions.head
           }
       }
     }
    result
  }

  def addIntervalResult(currTime:Long,dimension:T): Unit={
    clients.withClient{client=>
      client.hget(key,currTime) match {
        case None=>
          client.hmset(key,Map(currTime->dimension))
        case Some(frev)=>
          val newDimension=frev+","+dimension
          client.hmset(key,Map(currTime->newDimension))
      }
    }
  }
}


trait IntervalHashMapManager{
  val jsonParser =new JSONParser()
  var key=""

  def subtractIntervalResult(outTime:Long):mutable.Map[String,Int]={
    var result=""
    clients.withClient{client=>
      client.hget(key,outTime) match{
        case None=>
          println(outTime)
        case Some(dimensionstr)=>
          val dimensions=dimensionstr.split("\\|")
          if(dimensions.size==1){
            client.hdel(key,outTime)
            result=dimensions.head
          }
          else {
            client.hset(key,outTime,dimensions.slice(1,dimensions.size).mkString("|"))
            result=dimensions.head
          }
      }
    }
    strToMap(result)
  }
def strToMap(dimension:String): mutable.Map[String,Int] ={

  val json =  mapAsScalaMap(jsonParser.parse(dimension).asInstanceOf[java.util.HashMap[String,Int]])
  json
}
  def mapToStr(datas:HashMap[String,Int]): String ={
    JSONObject.toJSONString(mapAsJavaMap(datas),JSONStyle.MAX_COMPRESS)

  }


//
  def addIntervalResult(currTime:Long,dimension:HashMap[String,Int]): Unit={
    val inputData=mapToStr(dimension)
    clients.withClient{client=>
      client.hget(key,currTime) match {
        case None=>
          client.hmset(key,Map(currTime->inputData))
        case Some(frev)=>
          val newDimension=frev+"|"+inputData
          client.hmset(key,Map(currTime->newDimension))
      }
    }
  }

}

object TT{
   def main (args: Array[String]) {

     println("123|346|456".split("/|").slice(1,3).mkString("|"))
//     val ye= new Ye
//     println(ye.strToMap("{wenzhou:34,shanghai:33,hangzhou:37,jinghua:28}"))
//     strToMap("{\"178.9\":\"6\"}")
////     val hashMap=new HashMap[String,Int]
////     hashMap.put("178.9",10)
////     println(ye.strToMap("{\"178.9\":\"6\"}"))
////     println(ye.strToMap("{\"178.9\":\"6\"}"))
////     println(ye.mapToStr(hashMap))
////
////  }
////  def strToMap(dimension:String):mutable.Map[Float,Int] ={
////    val json =  mapAsScalaMap(jsonParser.parse(dimension).asInstanceOf[java.util.HashMap[Float,Int]])
////    json
////
  }
}
//
class Ye{

  val jsonParser =new JSONParser()
  def strToMap(dimension:String): mutable.Map[String,Int] ={

    val json =  mapAsScalaMap(jsonParser.parse(dimension).asInstanceOf[java.util.HashMap[String,Int]])
    json
  }
  def mapToStr(datas:HashMap[String,Int]): String ={
    JSONObject.toJSONString(mapAsJavaMap(datas),JSONStyle.MAX_COMPRESS)

  }
}


