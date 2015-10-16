package cn.tongdun.www

import cn.tongdun.www.service.ResultManager

/**
 * Created by wangqiaoshi on 15/9/14.
 */
object MaxTest {

  def main(args: Array[String]) {

//    println("330.0,331.0".split(",").head.asInstanceOf)
    val testObj= new ResultManager("xx",7776000l)
//    testObj.rangTime=7776000
    testObj.computeType="max"
    testObj.inputEndtime=1444704884576l
    testObj.addResultMax(1444704884576l,300.0f)
    testObj.substractResult("max")
    testObj.computeResult("max")

    testObj.inputEndtime=1444704884576l
    testObj.addResultMax(1444704884576l,301.0f)
    testObj.substractResult("max")
    testObj.computeResult("max")

    testObj.inputEndtime=1452480884579l
    testObj.addResultMax(1452480884579l,302.0f)
    testObj.substractResult("max")
    testObj.computeResult("max")

//    while(true){
//      val beginTime=System.currentTimeMillis()
//
//      testObj.addResultMax(System.currentTimeMillis(),300.0f)
//      testObj.substractResult("max")
//      testObj.computeResult("max")
//      val endTime=System.currentTimeMillis()
//      println("spend time is "+(endTime-beginTime))
//      Thread.sleep(1000) //500ms发一次
//
//    }
  }

}
