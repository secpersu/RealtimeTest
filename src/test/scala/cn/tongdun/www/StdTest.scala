package cn.tongdun.www

import java.io.File

import cn.tongdun.www.service.ResultManager

import scala.io.Source

import scala.collection.mutable
/**
 * Created by wangqiaoshi on 15/9/14.
 */
import cn.tongdun.www.metric.impl.ComputeIntervalFloatStd
import scala.util.Random
object StdTest {

  val payAmounts=List(19.8f,30.9f,11.7f,17.0f,9.8f,47.0f,99.7f,150.7f,44.0f)
  val length=payAmounts.size

  def makeHashMap(): (Long,mutable.HashMap[Float,Int]) ={

    val std=new ComputeIntervalFloatStd()
    for(index <- 0 to 1000){


      std.add(payAmounts(Random.nextInt(length)))

    }
    (System.currentTimeMillis(),std.dimensionNumHashMap)
  }


  def main(args: Array[String]) {

    val testObj= new ResultManager("xx")
    testObj.rangTime=7776000
    testObj.computeType="std"
    while(true) {

      val data=makeHashMap()
      val beginTime=System.currentTimeMillis()
      testObj.addResultStd(data._1,data._2)
      testObj.substractResult("std")
      testObj.computeResult("std")
      val endTime=System.currentTimeMillis()
      println("spend time is "+(endTime-beginTime))
      Thread.sleep(1000) //500ms发一次
    }
  }

}
