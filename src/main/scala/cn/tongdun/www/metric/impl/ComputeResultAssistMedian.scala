package cn.tongdun.www.metric.impl

import org.apache.spark.Logging

import scala.collection.immutable.TreeMap
import scala.collection.mutable

/**
 * Created by wangqiaoshi on 15/9/9.
 */
class ComputeResultAssistMedian extends Serializable with  Logging{

  private var dimesionNumMap=new TreeMap[Float,Int]()

  def substractAssist(dimensions:mutable.HashMap[Float,Int]): Unit ={
    dimensions.foreach {case (dimension:Float,oldNum:Int)=>
      dimesionNumMap.get(dimension) match {
        case None =>
        //此处需要报警
        case Some(num) =>


          val newNum = num - oldNum
          if (newNum == 0) {
            dimesionNumMap=dimesionNumMap.-(dimension)
          }
          else {
            dimesionNumMap=dimesionNumMap.+((dimension,newNum))
          }

      }
    }
  }

  def addAssist(dimensions:mutable.HashMap[Float,Int]): Unit ={
    dimensions.foreach {case (dimension:Float,newNum:Int)=>
      dimesionNumMap.get(dimension) match {
        case None =>
          if (dimension != null && !dimension.toString.equals("")) {
            dimesionNumMap=dimesionNumMap.+((dimension,newNum))
            //            result = 1 //如果该dimension不存在,说明添加新的不同dimension,返回 1
          }
        case Some(num) =>
          val updateNum = num + newNum
          dimesionNumMap=dimesionNumMap.+((dimension,updateNum))

      }
    }
  }

  def compute():Float={
    val it=dimesionNumMap.toIterator
    var dimensionIndex=0
    var median:Float=0
    var loopflag=true
    var num=dimesionNumMap.size
    while(it.hasNext&&loopflag){
      val item=it.next()

      dimensionIndex=dimensionIndex+item._2

      if(num%2==0){
        if(num/2+1<=dimensionIndex){
          median=item._1
          loopflag=false
        }
        else if(num/2<=dimensionIndex){
          median=(item._1+it.next()._1)/2
          loopflag=false
        }
      }
      else{
        if(num/2+1<=dimensionIndex){
          median=item._1
          loopflag=false
        }

      }
    }
    median

  }
}
