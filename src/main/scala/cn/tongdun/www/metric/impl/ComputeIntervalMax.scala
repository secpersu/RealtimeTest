package cn.tongdun.www.metric.impl

/**
 * Created by wangqiaoshi on 15/9/8.
 */
class ComputeIntervalFloatMax {
  var max=0f
  /**
   * 增加该维度的数量
   *
   * @param dimension
   */
  def add(dimension:Float): Unit ={
    if(dimension>max)
      max=dimension

  }

  override
  def toString():String={

    max.toString
  }



}
