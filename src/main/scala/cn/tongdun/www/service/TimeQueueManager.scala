package cn.tongdun.www.service

import scala.collection.mutable

import cn.tongdun.www.redis.RedisConnector._
/**
 * Created by wangqiaoshi on 15/10/13.
 */
class TimeQueueManager(key:String) {


  var redisKey=key+",timeQueue"
  var limit=86400l//一天
  var timeRange:Long=0
  val timeQueue=new mutable.Queue[Long]()
  var size=0


  def enqueue(time:Long): Unit ={
    //时间窗口超过界限,使用redis存储
    if(timeRange>limit){

      clients.withClient{client=>
        client.rpush(redisKey,time)
      }
    }
    else {
      timeQueue.enqueue(time)
    }
    size=size+1

  }

  def dequeue() ={
    if(timeRange>limit){
      clients.withClient{client=>
        client.lpop(redisKey)
      }
    }
    else {
      timeQueue.dequeue()
    }
    size=size-1

  }

  def front():Long={
    if(timeRange>limit){
      clients.withClient{client=>
        client.lindex(redisKey,0).get.toLong
      }
    }
    else {
      timeQueue.front
    }
  }


}
