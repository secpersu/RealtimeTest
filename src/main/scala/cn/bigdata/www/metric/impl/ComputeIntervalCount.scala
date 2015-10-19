package cn.bigdata.www.metric.impl

/**
 * Created by wangqiaoshi on 15/9/8.
 */
class ComputeIntervalCount {
  var count=0

  /**
   * 增加该维度的数量
   *
   * @param dimension
   */
  def add(dimension:Int): Unit ={
    count=count+dimension
  }

  override
  def toString():String={

    count.toString
  }


}
