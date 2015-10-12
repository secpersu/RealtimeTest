package cn.tongdun.www.metric.impl

import scala.collection.mutable


/**
 * Created by wangqiaoshi on 15/9/2.
 */
class ResultAssistDistinctCount extends Serializable{

  private val dimensionNum=new mutable.HashMap[String,Int]()

  def substractAssist(dimensions:mutable.HashMap[String,Int]): Unit ={

    dimensions.foreach {case (dimension:String,oldNum:Int)=>
      dimensionNum.get(dimension) match {
        case None =>
        //此处需要报警
        case Some(num) =>


          val newNum = num - oldNum
          if (newNum == 0) {
            dimensionNum.remove(dimension)

          }
          else {
            dimensionNum.put(dimension, newNum)//如果该dimension还存在
          }

      }
    }

  }

  def addAssist(dimensions:mutable.HashMap[String,Int]): Unit ={

    dimensions.foreach {case (dimension:String,newNum:Int)=>
      dimensionNum.get(dimension) match {
        case None =>
          if (dimension != null && !dimension.toString.equals("")) {
            dimensionNum.put(dimension, newNum)
//            result = 1 //如果该dimension不存在,说明添加新的不同dimension,返回 1
          }
        case Some(num) =>
          val updateNum = num + newNum
          dimensionNum.put(dimension, updateNum)
      }
    }
  }

  def compute(): Int ={
    dimensionNum.size
  }

}
