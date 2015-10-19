package cn.bigdata.www.metric.impl

import scala.collection.mutable

/**
 * Created by wangqiaoshi on 15/9/9.
 */
class ComputeIntervalFloatStd {



  val dimensionNumHashMap=mutable.HashMap[Float,Int]()

  /**
   * 增加该维度的数量
   *
   * @param dimension
   */
  def add(dimension:Float): Unit ={

    dimensionNumHashMap.get(dimension) match{
      case Some(num)=>
        val newNum=num+1
        dimensionNumHashMap.put(dimension,newNum)
      case None=>
        dimensionNumHashMap.put(dimension,1)
    }
  }

  override
  def toString: String =
  {
    val stringBuilder:StringBuilder=new StringBuilder()
    var index:Int=0
    stringBuilder.append("{")
    dimensionNumHashMap.foreach { case (key, value) =>
      if(index==0)
        stringBuilder.append("\"").append(key).append("\":\"")
          .append(value).append("\"")
      else
        stringBuilder.append(",\"").append(key).append("\":\"")
          .append(value).append("\"")
      index=index+1

    }
    stringBuilder.append("}")
    stringBuilder.toString()
  }

}
