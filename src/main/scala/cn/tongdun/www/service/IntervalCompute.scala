package cn.tongdun.www.service

import cn.tongdun.www.metric.impl.{ComputeIntervalSum, DistinctCountString,ComputeIntervalCount,ComputeIntervalFloatMax,ComputeIntervalFloatMin}
import cn.tongdun.www.metric.impl.{ComputeIntervalFloatMedian,ComputeIntervalFloatStd}
import scala.collection.mutable

/**
 * Created by wangqiaoshi on 15/9/7.
 */
object IntervalCompute {

  def distinctcount(computeFiledName:String,lineValues: Iterable[mutable.Map[String, String]]): (Long,Long,String) ={

    val intervalDistinctCount=new DistinctCountString()
    var max=0l
    var min=Long.MaxValue
    for(linevalue<-lineValues){
      val time=linevalue.get("time").get.toLong
      if(time<min)min=time
      if(time>max)max=time
      intervalDistinctCount.add(linevalue.get(computeFiledName).get)
    }
    (min,max,intervalDistinctCount.toString)

  }

  def sum(computeFiledName:String,lineValues: Iterable[mutable.Map[String, String]]):(Long,Long,String)={
    val computeIntervalSum = new ComputeIntervalSum()
    var max=0l
    var min=Long.MaxValue
    for(linevalue<-lineValues){
      val time=linevalue.get("time").get.toLong
      if(time<min)min=time
      if(time>max)max=time
      computeIntervalSum.add(linevalue(computeFiledName).toFloat)
    }
    (min,max,computeIntervalSum.toString())

  }

  def count(computeFiledName:String,lineValues: Iterable[mutable.Map[String, String]]):(Long,Long,String)={
    val computeIntervalCount= new ComputeIntervalCount()
    var max=0l
    var min=Long.MaxValue
    for(linevalue<-lineValues){
      val time=linevalue.get("time").get.toLong
      if(time<min)min=time
      if(time>max)max=time
      computeIntervalCount.add(1)
    }
    (min,max,computeIntervalCount.toString())
  }

  def max(computeFiledName:String,lineValues: Iterable[mutable.Map[String, String]]):(Long,Long,String)={

    val computeIntervalFloatMax = new ComputeIntervalFloatMax()
    var max=0l
    var min=Long.MaxValue
    for(linevalue<-lineValues){
      val time=linevalue.get("time").get.toLong
      if(time<min)min=time
      if(time>max)max=time
      computeIntervalFloatMax.add(linevalue(computeFiledName).toFloat)
    }
    (min,max,computeIntervalFloatMax.toString())
  }

  /**
   *
   * @param computeFiledName
   * @param lineValues
   */

  def min(computeFiledName:String,lineValues: Iterable[mutable.Map[String, String]]): (Long,Long,String) ={
    val computeIntervalFloatMin = new ComputeIntervalFloatMin()
    var max=0l
    var min=Long.MaxValue
    for(linevalue<-lineValues){
      val time=linevalue.get("time").get.toLong
      if(time<min)min=time
      if(time>max)max=time
      computeIntervalFloatMin.add(linevalue(computeFiledName).toFloat)
    }
    (min,max,computeIntervalFloatMin.toString())
  }



  /**
   *
   * 计算平均数,输出结果sum和count,已:隔开
   * @param computeFiledName
   * @param lineValues
   */
  def avg(computeFiledName:String,lineValues: Iterable[mutable.Map[String, String]]): (Long,Long,String) ={
    val computeIntervalSum = new ComputeIntervalSum()
    val computeIntervalCount= new ComputeIntervalCount()
    var max=0l
    var min=Long.MaxValue
    for(linevalue<-lineValues){
      val time=linevalue.get("time").get.toLong
      if(time<min)min=time
      if(time>max)max=time
      computeIntervalSum.add(linevalue(computeFiledName).toFloat)
      computeIntervalCount.add(1)
    }
    (min,max,computeIntervalSum.toString()+":"+computeIntervalCount.toString())

  }

  /**
   *
   * @param computeFiledName
   * @param lineValues
   */
  def median(computeFiledName:String,lineValues: Iterable[mutable.Map[String, String]]): (Long,Long,String) ={
    val computeIntervalFloatMedian=new ComputeIntervalFloatMedian()
    var max=0l
    var min=Long.MaxValue
    for(linevalue<-lineValues){
      val time=linevalue.get("time").get.toLong
      if(time<min)min=time
      if(time>max)max=time
      computeIntervalFloatMedian.add(linevalue(computeFiledName).toFloat)
    }
    (min,max,computeIntervalFloatMedian.toString)

  }

  def std(computeFiledName:String,lineValues: Iterable[mutable.Map[String, String]]): (Long,Long,String) ={
    var max=0l
    var min=Long.MaxValue
    val computeIntervalFloatStd= new ComputeIntervalFloatStd()

    for(linevalue<-lineValues){
      val time=linevalue.get("time").get.toLong
      if(time<min)min=time
      if(time>max)max=time
      computeIntervalFloatStd.add(linevalue(computeFiledName).toFloat)
    }
    (min,max,computeIntervalFloatStd.toString)
  }



}
