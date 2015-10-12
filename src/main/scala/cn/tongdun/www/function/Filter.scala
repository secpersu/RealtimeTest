package cn.tongdun.www.function

/**
 * Created by wangqiaoshi on 15/9/1.
 */
import scala.collection.immutable.Map
object Filter {

  def filter(values:Map[String,String]): Boolean ={
    var result=true
    values.get("time") match {
      case Some(time)=>
        result=true

      case None=>
        result=false
    }
    result


  }

}
