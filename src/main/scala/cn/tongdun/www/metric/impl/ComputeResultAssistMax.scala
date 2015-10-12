package cn.tongdun.www.metric.impl

import org.apache.spark.Logging

import scala.collection.immutable.TreeMap

/**
 * Created by wangqiaoshi on 15/9/8.
 */
class ComputeResultAssistMax extends Serializable with  Logging {
  private var dimesionNumMap=new TreeMap[Float,Int]()(Ordering[Float].reverse)

  def substractAssist(outMax:Float) ={

    var num= dimesionNumMap.get(outMax).get
    num=num-1
    if(num==0){
      dimesionNumMap=dimesionNumMap.-(outMax)
    }
    else
      dimesionNumMap=dimesionNumMap.+((outMax,num))

  }
  def addAssist(prevMax:Float): Unit ={
    dimesionNumMap.get(prevMax) match{
      case Some(num)=>
        val newNum=num+1
        dimesionNumMap=dimesionNumMap.+((prevMax,newNum))
      case None=>
        dimesionNumMap=dimesionNumMap.+((prevMax,1))
    }

  }

  def compute():Float={


      dimesionNumMap.head._1

  }


}
