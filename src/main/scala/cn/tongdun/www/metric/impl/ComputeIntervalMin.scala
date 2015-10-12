package cn.tongdun.www.metric.impl

/**
 * Created by wangqiaoshi on 15/9/8.
 */
class ComputeIntervalFloatMin {

  var min=Float.MaxValue

  def add(dimension:Float): Unit ={
    if(dimension<min)
      min=dimension

  }

  override
  def toString():String={

    min.toString
  }

}
