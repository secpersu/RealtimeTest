package cn.tongdun.www.metric.impl

import org.apache.spark.Logging

import scala.collection.mutable
import scala.math._

/**
 * Created by wangqiaoshi on 15/9/9.
 */
class ComputeResultAssistStd   extends Serializable with  Logging{

  private val dimensionNum=new mutable.HashMap[Float,Int]()

  def substractAssist(dimensions:mutable.Map[String,Int]): Unit ={

    dimensions.foreach {case (dimension:String,oldNum:Int)=>
      dimensionNum.get(dimension.toFloat) match {
        case None =>
        //此处需要报警
        case Some(num) =>


          val newNum = num - oldNum
          if (newNum == 0) {
            dimensionNum.remove(dimension.toFloat)

          }
          else {
            dimensionNum.put(dimension.toFloat, newNum)//如果该dimension还存在
          }

      }
    }

  }

  def addAssist(dimensions:mutable.HashMap[String,Int]): Unit ={

    dimensions.foreach {case (dimension:String,newNum:Int)=>
      dimensionNum.get(dimension.toFloat) match {
        case None =>
          if (dimension != null && !dimension.equals("")) {
            dimensionNum.put(dimension.toFloat, newNum)
            //            result = 1 //如果该dimension不存在,说明添加新的不同dimension,返回 1
          }
        case Some(num) =>
          val updateNum = num + newNum
          dimensionNum.put(dimension.toFloat, updateNum)
      }
    }
  }

  def compute(median:Float): Double ={
    var sum:Double=0
    val num=dimensionNum.size
    dimensionNum.foreach {
      case (dimension, num) =>
        sum=sum+num*pow(dimension.toString.toFloat - median,2)
    }
    if(num==1)
      sum= -1
    else  if(num>1)
      sum=sqrt(sum/(num-1))
    sum

  }


}
