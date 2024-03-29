package cn.bigdata.www.metric.impl

import cn.bigdata.www.metric.{TimeSlice, IntervalResultManager, AbstractIntervalResultManager, IntervalHashMapManager}
import cn.bigdata.www.service.{ResultManager, TimeQueueManager}

import scala.collection.mutable
import scala.collection.mutable.{HashMap, Map}
import scala.util.Random


/**
 * Created by wangqiaoshi on 15/9/1.
 */
class IntervalDistinctCountResultStringManager extends  Serializable with AbstractIntervalResultManager[HashMap[String,Int]]{


}


class IntervalSumResultManager extends  Serializable with AbstractIntervalResultManager[Float]{


}

class IntervalCountResultManager extends  Serializable with AbstractIntervalResultManager[Int]{


}

class IntervalMaxResultManager extends  Serializable with AbstractIntervalResultManager[Float]{


}
class IntervalMinResultManager extends  Serializable with AbstractIntervalResultManager[Float]{


}

class IntervalMedianFloatManager extends  Serializable with AbstractIntervalResultManager[HashMap[Float,Int]]{


}


class IntervalStdFloatManager extends  Serializable with AbstractIntervalResultManager[HashMap[Float,Int]]{


}

class IntervalTest(keys:String) extends  Serializable with IntervalResultManager[Int]{
  key=keys
}


class IntervalSumManager(keys:String) extends  Serializable with IntervalResultManager[Float]{
  key=keys+",sum"
}

class IntervalCountManager(keys:String) extends  Serializable with IntervalResultManager[Int]{
  key=keys+",count"
}

class IntervalMaxManager(keys:String) extends  Serializable with IntervalResultManager[Float]{

key=keys+",max"
}

class IntervalMinManager(keys:String) extends  Serializable with IntervalResultManager[Float]{
 key=keys+",min"

}

class IntervalDistinctCountManager(keys:String) extends  Serializable with IntervalHashMapManager{

 key=keys+",distinct"
}
class IntervalMedianManager(keys:String) extends  Serializable with IntervalHashMapManager{

 key=keys+",median"
}

class IntervalStdManager(keys:String) extends  Serializable with IntervalHashMapManager{
  key=keys+",std"

}
//------------------------------------------------------------------------------------------------------
class IntervalRedisManager[T](keys:String) extends  Serializable with IntervalResultManager[T]{
  key=keys
}
abstract class  TimeSliceManager[T](key:String,rangTime:Long,nowTime:Long) extends TimeSlice[T](rangTime,nowTime)   with Serializable  {



  val intervalSumManager=new IntervalRedisManager[T](key)


  def subtractIntervalResult(outTime:Long): String={


      intervalSumManager.subtractIntervalResult(outTime)


  }

  def addIntervalResult(currTime:Long,dimension:T): Unit={

    if(check(currTime,addOp)){//先合并，在reids中增加
      super.addResult(currTime,dimension)
    val result=mergeTimeSlice()
      intervalSumManager.addIntervalResult(result._1,result._2)
       clear()
    }
    else{
       super.addResult(currTime,dimension)
    }
  }

    def  mergeTimeSlice():(Long,T)


}

class  TimeSliceSumManager(key:String,rangTime:Long,nowTime:Long) extends TimeSliceManager[Float](key,rangTime,nowTime){



  def  mergeTimeSlice():(Long,Float)={
    var endTime=0l
    var sum=0f
    historyMap.foreach{case (time:Long,dimensions:mutable.MutableList[Float])=>
      if(endTime<time)
        endTime=time
      dimensions.foreach(i=>sum=sum+i)
    }
    (endTime,sum)
  }

}

class TimeSliceCountManager(key:String,rangTime:Long,nowTime:Long) extends TimeSliceManager[Int](key,rangTime,nowTime){


  def  mergeTimeSlice():(Long,Int)={
    var endTime=0l
    var sum=0
    historyMap.foreach{case (time:Long,dimensions:mutable.MutableList[Int])=>
      if(endTime<time)
        endTime=time
      dimensions.foreach(i=>sum=sum+i)
    }
    (endTime,sum)
  }

}


class TimeSliceMaxManager(key:String,rangTime:Long,nowTime:Long) extends  TimeSliceHashManager(key,rangTime,nowTime:Long){

  def  mergeTimeSlice():(Long,HashMap[String, Int])={

    val result = new HashMap[String, Int]
    var endTime=0l
    historyMap.foreach { case (time: Long, dimensions: mutable.MutableList[Map[String, Int]]) =>
      if(endTime<time)
        endTime=time
      mergeMap(result,dimensions)
    }
    (endTime,result)
  }



  def mergeMap(result:HashMap[String, Int],dimensions: mutable.MutableList[Map[String, Int]]): Unit ={


    for(dimension<-dimensions){
      dimension.foreach{ case (key:String,resultNum:Int)=>
        result.get(key) match {
          case Some(num) =>
            val newNum = num + resultNum
            result.put(key, newNum)
          case None =>
            result.put(key, resultNum)
        }
      }
    }

  }


}


class TimeSliceMinManager(key:String,rangTime:Long,nowTime:Long) extends TimeSliceHashManager(key,rangTime,nowTime:Long){

//  def addIntervalResult(currTime:Long,dimension:Float): Unit ={
//    if(check(currTime,addOp)){//先合并，在reids中增加
//      super.addResult(currTime,dimension)
//      val result=mergeTimeSlice()
//      intervalManager.addIntervalResult(result._1,result._2)
//      clear()
//    }
//    else{
//      super.addResult(currTime,dimension)
//    }
//  }

  def  mergeTimeSlice():(Long,HashMap[String, Int])={

    val result = new HashMap[String, Int]
    var endTime=0l
    historyMap.foreach { case (time: Long, dimensions: mutable.MutableList[Map[String, Int]]) =>
      if(endTime<time)
        endTime=time
      mergeMap(result,dimensions)

    }
    (endTime,result)
  }



  def mergeMap(result:HashMap[String, Int],dimensions: mutable.MutableList[Map[String, Int]]): Unit ={


    for(dimension<-dimensions){
      dimension.foreach{ case (key:String,resultNum:Int)=>
        result.get(key) match {
          case Some(num) =>
            val newNum = num + resultNum
            result.put(key, newNum)
          case None =>
            result.put(key, resultNum)
        }
      }
    }

  }


}

class IntervalRedisHashManager(keys:String) extends  Serializable with IntervalHashMapManager{
  key=keys
}

abstract class  TimeSliceHashManager(key:String,rangTime:Long,nowTime:Long) extends TimeSlice[mutable.Map[String,Int]](rangTime,nowTime)   with Serializable  {



  val intervalManager=new IntervalRedisHashManager(key)


  def subtractIntervalResult(outTime:Long): mutable.Map[String,Int]={


    intervalManager.subtractIntervalResult(outTime)


  }

  def addIntervalResult(currTime:Long,dimension:mutable.Map[String,Int]): Unit={

    if(check(currTime,addOp)){//先合并，在reids中增加
      super.addResult(currTime,dimension)
      val result=mergeTimeSlice()
      intervalManager.addIntervalResult(result._1,result._2)
      clear()
    }
    else{
      super.addResult(currTime,dimension)
    }
  }

  def  mergeTimeSlice():(Long,HashMap[String,Int])


}

class TimeSliceDistinctManager(key:String,rangTime:Long,nowTime:Long) extends TimeSliceHashManager(key,rangTime,nowTime:Long){

  def  mergeTimeSlice():(Long,HashMap[String,Int])= {

    val result = new HashMap[String, Int]
    var endTime=0l
    historyMap.foreach { case (time: Long, dimensions: mutable.MutableList[Map[String, Int]]) =>
      if(endTime<time)
        endTime=time
        mergeMap(result,dimensions)
      }
    (endTime,result)
  }




  def mergeMap(result:HashMap[String, Int],dimensions: mutable.MutableList[Map[String, Int]]): Unit ={


    for(dimension<-dimensions){
      dimension.foreach{ case (key:String,resultNum:Int)=>
        result.get(key) match {
          case Some(num) =>
            val newNum = num + resultNum
            result.put(key, newNum)
          case None =>
            result.put(key, resultNum)
        }
      }
    }

  }


}


class TimeSliceStdManager(key:String,rangTime:Long,nowTime:Long) extends TimeSliceHashManager(key,rangTime,nowTime){

  def  mergeTimeSlice():(Long,HashMap[String,Int])= {

    val result = new HashMap[String, Int]
    var endTime=0l
    historyMap.foreach { case (time: Long, dimensions: mutable.MutableList[Map[String, Int]]) =>
      if(endTime<time)
        endTime=time
      mergeMap(result,dimensions)
    }
    (endTime,result)
  }




  def mergeMap(result:HashMap[String, Int],dimensions: mutable.MutableList[Map[String, Int]]): Unit ={


    for(dimension<-dimensions){
      dimension.foreach{ case (key:String,resultNum:Int)=>
        result.get(key) match {
          case Some(num) =>
            val newNum = num + resultNum
            result.put(key, newNum)
          case None =>
            result.put(key, resultNum)
        }
      }
    }

  }


}

class TimeSliceMedianManager(key:String,rangTime:Long,nowTime:Long) extends TimeSliceHashManager(key,rangTime,nowTime){

  def  mergeTimeSlice():(Long,HashMap[String,Int])= {

    val result = new HashMap[String, Int]
    var endTime=0l
    historyMap.foreach { case (time: Long, dimensions: mutable.MutableList[Map[String, Int]]) =>
      if(endTime<time)
        endTime=time
      mergeMap(result,dimensions)
    }
    (endTime,result)
  }




  def mergeMap(result:HashMap[String, Int],dimensions: mutable.MutableList[Map[String, Int]]): Unit ={


    for(dimension<-dimensions){
      dimension.foreach{ case (key:String,resultNum:Int)=>
        result.get(key) match {
          case Some(num) =>
            val newNum = num + resultNum
            result.put(key, newNum)
          case None =>
            result.put(key, resultNum)
        }
      }
    }

  }


}






object Test{



  def makeData(): (Long,Float) ={
    val time=System.currentTimeMillis()
    val value=Random.nextFloat()
    (time,value)
  }
  def main(args: Array[String]) {


    var xx=makeData()
    var index=0
    val manager = new ResultManager("test",1l,xx._1)
    while(true) {
      if(index>0)
      xx=makeData()
      manager.addResultMax(xx._1,xx._2)
      manager.inputEndtime=xx._1
      manager.substractResult("max")
      println("---"+xx._1+"-----"+xx._2+"---"+manager.computeResult("max"))
      index=index+1
      Thread.sleep(20)
    }
//    why.addIntervalResult(125l,123f)
//    why.addIntervalResult(86400123l,124f)
//    why.addIntervalResult(86400124l,124f)
//
//
//    why.subtractIntervalResult(86400123l)

//    val distinct = new TimeSliceDistinctManager("why,distinct",86400l)
//    distinct.addIntervalResult(123l,Map("192.168.6.1"->5,"192.168.6.2"->3))
//    distinct.addIntervalResult(123l,Map("192.168.47.4"->6,"192.168.6.1"->3))
//    distinct.addIntervalResult(1123l,Map("192.168.47.4"->6,"192.168.6.1"->3))







  }
}
