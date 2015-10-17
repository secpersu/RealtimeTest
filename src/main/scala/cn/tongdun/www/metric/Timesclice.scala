package cn.tongdun.www.metric

import scala.collection.mutable


/**
 * Created by wangqiaoshi on 15/10/14.
 */


abstract class TimeSlice[T](timeRange:Long,nowTime:Long)  extends AbstractIntervalResultManager[T]{



  var timeUnit=0
  var lastTime=nowTime


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

  //如果时间窗口
  //返回true,redis
  private def checkAdd(currTime:Long): Boolean ={
    var result:Boolean=false
    if(lastTime!=0&&(currTime-lastTime)/1000>=timeUnit) {
      result = true
      lastTime = currTime
    }
    result
  }
  private def checkSubstract(outTime:Long): Boolean ={
    var result:Boolean=false
    if((lastTime-outTime)/1000>=timeUnit)
      result=true
    result

  }
  def check(time:Long,op:String): Boolean ={
    op match {
      case "add"=>
        checkAdd(time)
      case "substract"=>
        checkSubstract(time)
    }
  }
  val addOp="add"
  val substractOp="substract"




  //清空缓存
  def clear(): Unit ={

    historyMap.clear()
  }





}
