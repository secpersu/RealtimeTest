package cn.tongdun.www.metric

import org.apache.spark.Logging


import scala.collection.mutable.{HashMap, MutableList}

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

  def subtractIntervalResult(outTime:Long): T={

     clients.withClient{client=>
       val dimensionstr=client.hget(key,outTime).get
       val dimensions=dimensionstr.split(",")
       if(dimensions.size==0){
         client.hdel(key,outTime)
         dimensionstr.asInstanceOf[T]
       }
       else {

         client.hset(key,outTime,dimensions.slice(1,dimensions.size).mkString(","))
         dimensions.head.asInstanceOf[T]
       }
     }
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


