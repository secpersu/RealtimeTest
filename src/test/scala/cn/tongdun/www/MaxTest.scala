package cn.tongdun.www

import cn.tongdun.www.service.ResultManager

/**
 * Created by wangqiaoshi on 15/9/14.
 */
object MaxTest {

  def main(args: Array[String]) {
    val testObj= new ResultManager("xx")
    testObj.rangTime=7776000
    testObj.computeType="max"
    while(true){
      val beginTime=System.currentTimeMillis()

      testObj.addResultMax(System.currentTimeMillis(),300.0f)
      testObj.substractResult("max")
      testObj.computeResult("max")
      val endTime=System.currentTimeMillis()
      println("spend time is "+(endTime-beginTime))
      Thread.sleep(1000) //500ms发一次

    }
  }

}
