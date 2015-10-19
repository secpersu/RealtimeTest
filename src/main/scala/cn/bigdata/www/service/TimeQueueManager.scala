package cn.bigdata.www.service

import cn.bigdata.www.metric.TimeSlice

import scala.collection.mutable

import cn.bigdata.www.redis.RedisConnector._
/**
 * Created by wangqiaoshi on 15/10/13.
 */
class TimeQueueManager(key:String,timeRange:Long,nowTime:Long) {


  var redisKey=key+",timeQueue"

//  var timeRange=0l
  var timeUnit=0
  var lastTime=nowTime
  val timeQueue=new mutable.Queue[Long]()
  var size=0



    timeUnit={
      if(timeRange<=86400)//小于一天,时间片为1s
        1
      else if(timeRange>86400&&timeRange<=604800)//大于一天,小于7天,时间片单位为1分
        60
      else if(timeRange>604800&&timeRange<=2592000)//小于30天,大于7天,时间片单位5分中
        300
      else
        600
    }



  def enqueue(time:Long): Unit ={
    //时间窗口超过界限,使用redis存储

    if(checkEnqueue(time)){//存入redis,先合并,再存入,再清楚缓存

      clients.withClient{client=>
        client.rpush(redisKey,time)
      }
      clear()
    }
    else{
      timeQueue.enqueue(time)
    }
    size=size+1
  }


  def dequeue() ={

      clients.withClient { client =>
        client.lpop(redisKey)
      }
    size=size-1
  }

  def front():Long={

    var reslut=0l
      clients.withClient{client=>
        client.lindex(redisKey,0) match{
          case None=>
            reslut=timeQueue.front

          case Some(timeFront)=>
            reslut=timeFront.toLong
        }
      }
    reslut

  }


  //如果时间窗口
  //返回true,redis
  private def checkEnqueue(currTime:Long): Boolean ={
    var result:Boolean=false
    if(lastTime!=0&&(currTime-lastTime)/1000>=timeUnit) {
      result = true
      lastTime = currTime
    }
    result
  }
  private def checkDequeue(outTime:Long): Boolean ={
    var result:Boolean=false
    if((lastTime-outTime)/1000>=timeUnit)
      result=true
    result

  }




  //清空缓存
  def clear(): Unit ={
    timeQueue.clear()
  }
}

object TimeQueueManager{

  def main(args: Array[String]) {
    val timeQueue = new TimeQueueManager("redis",604800l,120l)
//    timeQueue.setTimeRange(604800l)
    timeQueue.enqueue(123l)
    timeQueue.enqueue(129l)
    timeQueue.enqueue(130l)
    timeQueue.enqueue(60123l)
    timeQueue.enqueue(60124l)
    timeQueue.dequeue()



  }

}








