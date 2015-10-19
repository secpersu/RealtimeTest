package cn.bigdata.www.metric.impl

/**
 * Created by wangqiaoshi on 15/8/25.
 */
import scala.collection.mutable.HashMap
class DistinctCountString{

  val dimensionNumHashMap=new HashMap[String,Int]()

  /**
   * 增加该维度的数量
   *
   * @param dimension
   */
  def add(dimension:String): Unit ={

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


class DistinctCountInt{
  val dimensionNumHashMap=HashMap[Int,Int]()

  /**
   * 增加该维度的数量
   *
   * @param dimension
   */
  def add(dimension:Int): Unit ={

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

class DistinctCountLong{
  val dimensionNumHashMap=HashMap[Long,Int]()

  /**
   * 增加该维度的数量
   *
   * @param dimension
   */
  def add(dimension:Long): Unit ={

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



