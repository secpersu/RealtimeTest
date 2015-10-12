package cn.tongdun.www

/**
 * Created by wangqiaoshi on 15/9/15.
 */
import scala.collection.mutable
import scala.util.Random

object ComputeMaxTest {


  def main(args: Array[String]) {
    val it= new mutable.MutableList[mutable.Map[String,Long]]
    for(i<- 0 to 8000){
      val map= mutable.Map[String,Long]()
      map.put("time",Random.nextLong())
      it.+=(map)
    }
    val begime=System.currentTimeMillis()
    for(i<-it){
      i.get("time").get
    }
    val endTime=System.currentTimeMillis()
    println(endTime-begime)


  }

}
