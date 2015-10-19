package cn.bigdata.www.metric.impl

import org.apache.spark.Logging



/**
 * Created by wangqiaoshi on 15/9/7.
 */
class ComputeResultAssistSum extends Serializable with  Logging{
  private var sum=0f

  def substractAssist(outSum:Float) ={

    sum=sum-outSum
    if(sum<0){
      logError("compute sum error")
      //此处需要报警
      sum=0
    }


  }

  def addAssist(prevSum:Float) ={

    sum=sum+prevSum
  }

  def compute():Float={
  sum
  }


}


class ComputeResultAssistCount extends Serializable with  Logging{
  private var count=0

  def substractAssist(outCount:Int) ={

    count=count-outCount
    if(count<0){
      logError("compute sum error")
      //此处需要报警
      count=0
    }


  }

  def addAssist(prevCount:Int) ={

    count=count+prevCount
  }

  def compute():Int={
    count
  }

}