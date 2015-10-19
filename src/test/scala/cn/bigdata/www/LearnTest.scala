package cn.bigdata.www

import scala.collection.{immutable, mutable}

/**
 * Created by wangqiaoshi on 15/9/15.
 */

import cn.bigdata.www.function.Utils._
object LearnTest {
  class DummyObject(var i: Int) {
    def toInt = i
  }

  def main(args: Array[String]) {
    println("2,3".split(",").slice(1,2).mkString(","))

//    val json="{\"policys\":[\"ip:payamount:avg:120\",\"ip:payamount:min:86400\",\"ip:payamount:max:60\",\"ip:payamount:count:60\",\"ip:payamount:sum:2592000\",\"ip:city:distinctcount:86400\",\"username:payamount:sum:3600\",\"username:city:distinctcount:60\",\"username:city:distinctcount:1\"],\"datas\":{\"ip\":\"192.168.6.55\",\"payamount\":\"249.34\",\"username\":\"liuyang\",\"city\":\"hangzhou\",\"time\":\"1441852585852\"}}"
//    val list=new mutable.MutableList[(String,String)]()
//    list.+=(("",json))
//    list.+=(("","{\"policys\":[\"ip:payamount:avg:120\",\"ip:payamount:min:86400\",\"ip:payamount:max:60\",\"ip:payamount:count:60\",\"ip:payamount:sum:2592000\",\"ip:city:distinctcount:86400\",\"username:payamount:sum:3600\",\"username:city:distinctcount:60\",\"username:city:distinctcount:1\"],\"datas\":{\"ip\":\"192.168.47.213\",\"payamount\":\"249.34\",\"username\":\"liangshiwei\",\"city\":\"hangzhou\",\"time\":\"1441852585852\"}}"))
//    parseToPolicyAndDataV2(list.toIterator).foreach(r=>println(r._2))


//    val obj=new DummyObject(0)
//    val resultList=new mutable.MutableList[Int]()
//    val result=(0 to 1000).flatMap{item=>{
//      resultList.clear()
//      resultList.+=(item)
//      resultList
//    }}
//    println(result)
  }



}
