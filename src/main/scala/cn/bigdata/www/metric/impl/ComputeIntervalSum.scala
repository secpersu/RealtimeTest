package cn.bigdata.www.metric.impl

/**
 * Created by wangqiaoshi on 15/9/7.
 */
class ComputeIntervalSum {

  var sum=0f

  /**
   * 增加该维度的数量
   *
   * @param dimension
   */
  def add(dimension:Float): Unit ={
    sum=sum+dimension
  }
  override
  def toString():String={

    sum.toString
  }


}
