package cn.bigdata.www.service

import cn.bigdata.www.function.Utils._
import cn.bigdata.www.metric.impl._

import scala.collection.{immutable, mutable}
import scala.collection.mutable.HashMap

/**
 * Created by wangqiaoshi on 15/9/1.
 */


class ResultManager(key:String,rangTime:Long,nowTime:Long) extends Serializable{

  private val resultDistinctCountAssist=new ResultAssistDistinctCount()
//  private val intervalDistinctCountResultStringManager=new IntervalDistinctCountResultStringManager()
//  private val intervalDistinctCountResultStringManager=new IntervalDistinctCountManager(key)
  private val intervalDistinctCountResultStringManager=new TimeSliceDistinctManager(key+",distinct",rangTime,nowTime)



  //
//  private val intervalSumResultManager=new IntervalSumResultManager()
//  private val intervalSumResultManager=new IntervalSumManager(key)
  private val intervalSumResultManager=new TimeSliceSumManager(key+",sum",rangTime,nowTime)
  private val computeResultAssistSum=new ComputeResultAssistSum()

  //
//  private val intervalCountResultManager = new IntervalCountResultManager()
//  private val intervalCountResultManager = new IntervalCountManager(key)
  private val intervalCountResultManager = new TimeSliceCountManager(key+",count",rangTime,nowTime)
  private val computeResultAssistCount=new ComputeResultAssistCount()


  //最小值
//  private val intervalMinResultManager = new IntervalMinResultManager()
//  private val intervalMinResultManager = new IntervalMinManager(key)
  private val intervalMinResultManager = new TimeSliceMinManager(key+",min",rangTime,nowTime)
  private val computeResultAssistMin=new ComputeResultAssistMin()

  // 中位数
//  private val intervalMedianFloatManager = new IntervalMedianManager()
//  private val intervalMedianFloatManager = new IntervalMedianManager(key)
  private val intervalMedianFloatManager = new TimeSliceMedianManager(key+",median",rangTime,nowTime)
  private val computeResultAssistMedian = new ComputeResultAssistMedian()

  //均值
//  private val intervalStdFloatManager= new IntervalStdFloatManager()
//  private val intervalStdFloatManager=new IntervalStdManager(key)
  private val intervalStdFloatManager=new TimeSliceStdManager(key+",std",rangTime,nowTime)
  private val computeResultAssistStd = new ComputeResultAssistStd()

  //最大值
//  private val intervalMaxResultManager=new IntervalMaxResultManager()
//  private val intervalMaxResultManager=new IntervalMaxManager(key)
  private val intervalMaxResultManager=new TimeSliceMaxManager(key+",max",rangTime,nowTime)
  private val computeResultAssistMax=new ComputeResultAssistMax()

//  private val timeQueue=new mutable.Queue[Long]
  private val timeQueue=new TimeQueueManager(key,rangTime,nowTime)


//  var rangTime:Long=0
  var computeType:String=null

  //
  var inputBegintime:Long=0
  var inputEndtime:Long=0
  var computeTime:Long=0


//  def setRangTime(rangTimes:Long): Unit ={
//    rangTime=rangTimes
//    timeQueue.timeRange=rangTimes
//
//  }



  def addResultDistinctCount(time:Long,dimension:mutable.HashMap[String,Int]): Unit ={
    //1.加上超出时间范围的阶段性结果,2
    timeQueue.enqueue(time)
    intervalDistinctCountResultStringManager.addIntervalResult(time,dimension)
    resultDistinctCountAssist.addAssist(dimension)

  }

  /**
   *
   * 求和
   * @param time
   * @param dimension
   */
  def addResultSum(time:Long,dimension:Float): Unit ={
    timeQueue.enqueue(time)
    intervalSumResultManager.addIntervalResult(time,dimension)
    computeResultAssistSum.addAssist(dimension)
  }

  def addResultCount(time:Long,dimension:Int): Unit ={
    timeQueue.enqueue(time)
    intervalCountResultManager.addIntervalResult(time,dimension)
    computeResultAssistCount.addAssist(dimension)
  }




  def addResultMax(time:Long,dimension:Float): Unit ={
    timeQueue.enqueue(time)
    intervalMaxResultManager.addIntervalResult(time,mutable.Map(dimension.toString->1))
    computeResultAssistMax.addAssist(dimension)
  }

  def addResultMin(time:Long,dimension:Float): Unit ={
    timeQueue.enqueue(time)
    intervalMinResultManager.addIntervalResult(time,mutable.Map(dimension.toString->1))
    computeResultAssistMin.addAssist(dimension)
  }

  def addResultAvg(time:Long,sum:Float,count:Int): Unit ={
    timeQueue.enqueue(time)

    intervalSumResultManager.addIntervalResult(time,sum)
    computeResultAssistSum.addAssist(sum)

    intervalCountResultManager.addIntervalResult(time,count)
    computeResultAssistCount.addAssist(count)

  }

  def addResultMedian(time:Long,dimensions:mutable.HashMap[String,Int]): Unit ={
    timeQueue.enqueue(time)
    intervalMedianFloatManager.addIntervalResult(time,dimensions)
    computeResultAssistMedian.addAssist(dimensions)
  }

  def addResultStd(time:Long,dimensions:mutable.HashMap[String,Int]): Unit ={

    timeQueue.enqueue(time)
    intervalStdFloatManager.addIntervalResult(time,dimensions)
    computeResultAssistStd.addAssist(dimensions)
    var sum=0f
    var count=0
    dimensions.foreach { case (dimension: String, newNum: Int) =>
        sum=sum+dimension.toFloat*newNum
        count=count+newNum
    }

    intervalSumResultManager.addIntervalResult(time,sum)
    computeResultAssistSum.addAssist(sum)

    intervalCountResultManager.addIntervalResult(time,count)
    computeResultAssistCount.addAssist(count)

  }








  def addResult(computeType:String,newValues: Seq[( Long, Long, String)]): Unit ={
    if(newValues.size>0)
    computeType match {
      case "distinctcount"=>
        val newHashMap=new HashMap[String,Int]()

          for (newvalue <- newValues) {
            inputBegintime = newvalue._1
            inputEndtime = newvalue._2

            jsonstrToMap (newvalue._3).foreach {
              case (dimension: String, newNum: String) => {
                newHashMap.get (dimension) match {
                  case None =>
                    if (dimension != null && ! dimension.toString.equals ("") ) {
                      newHashMap.put (dimension, newNum.toInt)
                      //            result = 1 //如果该dimension不存在,说明添加新的不同dimension,返回 1
                    }
                  case Some (num) =>
                    val updateNum = num + newNum.toInt
                    newHashMap.put (dimension, updateNum)
                }
              }

          }
        }

        addResultDistinctCount(inputEndtime,newHashMap)


      case "median"=>
        val newHashMap=new HashMap[String,Int]()

        for (newvalue <- newValues) {
          inputBegintime = newvalue._1
          inputEndtime = newvalue._2

          jsonstrToMap (newvalue._3).foreach {
            case (dimension: String, newNum: String) => {
              newHashMap.get (dimension) match {
                case None =>
                  if (dimension != null && ! dimension.equals ("") ) {
                    newHashMap.put (dimension, newNum.toInt)
                    //            result = 1 //如果该dimension不存在,说明添加新的不同dimension,返回 1
                  }
                case Some (num) =>
                  val updateNum = num + newNum.toInt
                  newHashMap.put (dimension, updateNum)
              }
            }

          }
          addResultMedian(inputEndtime,newHashMap)
        }

      case "sum"=>
        var allsum=0f
        for (newvalue <- newValues) {
          inputBegintime = newvalue._1
          inputEndtime = newvalue._2

          allsum=allsum+newvalue._3.toFloat
        }
        addResultSum(inputEndtime,allsum)

      case "count"=>
        var allCount=0
        for (newvalue <- newValues) {
          inputBegintime = newvalue._1
          inputEndtime = newvalue._2
          allCount=allCount+newvalue._3.toInt
        }
        addResultCount(inputEndtime,allCount)

      case "max"=>
        var allMax=0f
        for (newvalue <- newValues) {
          inputBegintime = newvalue._1
          inputEndtime = newvalue._2
          if(allMax<newvalue._3.toFloat)
            allMax=newvalue._3.toFloat
        }
        addResultMax(inputEndtime,allMax)

      case "min"=>
        var allMin=Float.MaxValue
        for (newvalue <- newValues) {
          inputBegintime = newvalue._1
          inputEndtime = newvalue._2
          if(allMin>newvalue._3.toFloat)
            allMin=newvalue._3.toFloat

        }
        addResultMin(inputEndtime,allMin)
      case "avg"=>
        var allSum=0f
        var allCount=0
        for (newvalue <- newValues) {
          inputBegintime = newvalue._1
          inputEndtime = newvalue._2
          val Array(sum,count)=newvalue._3.split(":")
          allSum=allSum+sum.toFloat
          allCount=allCount+count.toInt
        }
        addResultAvg(inputEndtime,allSum,allCount)

      case "std"=>
        val newHashMap=new HashMap[String,Int]()

        for (newvalue <- newValues) {
          inputBegintime = newvalue._1
          inputEndtime = newvalue._2

          jsonstrToMap(newvalue._3).foreach {
            case (dimension: String, newNum: String) => {
              newHashMap.get(dimension) match {
                case None =>
                  if (dimension != null && !dimension.equals("")) {
                    newHashMap.put(dimension, newNum.toInt)
                    //            result = 1 //如果该dimension不存在,说明添加新的不同dimension,返回 1
                  }
                case Some(num) =>
                  val updateNum = num + newNum.toInt
                  newHashMap.put(dimension, updateNum)
              }
            }

          }
        }
        addResultStd(inputEndtime,newHashMap)

      case _=>

    }
    computeTime = System.currentTimeMillis ()

  }


//务必注意，先加后减
  def substractResult(computeType:String): Unit = {


    while (timeQueue.size > 0 && (inputEndtime - timeQueue.front) / 1000.0 > rangTime) {


      computeType match{
        case "distinctcount"=>
          val jsonStr= intervalDistinctCountResultStringManager.subtractIntervalResult(timeQueue.front)
          //1.减去超出时间范围的阶段性结果数据,2.减去结果辅助类结果
          resultDistinctCountAssist.substractAssist(jsonStr)
        case "sum"=>
         val outSum= intervalSumResultManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistSum.substractAssist(outSum.toFloat)

        case "count"=>
          val outCount=intervalCountResultManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistCount.substractAssist(outCount.toInt)

        case "max"=>
          val outMax=intervalMaxResultManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistMax.substractAssist(outMax)
        case "min"=>
          val outMin=intervalMinResultManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistMin.substractAssist(outMin)
        case "avg"=>
          val outSum= intervalSumResultManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistSum.substractAssist(outSum.toFloat)
          val outCount=intervalCountResultManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistCount.substractAssist(outCount.toInt)
        case "median"=>
         val outMedian= intervalMedianFloatManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistMedian.substractAssist(outMedian)

        case "std"=>
         val outStd= intervalStdFloatManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistStd.substractAssist(outStd)
          val outSum= intervalSumResultManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistSum.substractAssist(outSum.toFloat)
          val outCount=intervalCountResultManager.subtractIntervalResult(timeQueue.front)
          computeResultAssistCount.substractAssist(outCount.toInt)
        case _=>
      }
      timeQueue.dequeue()
    }

  }

//  def substractResultStd(): Unit ={
//    val outStd= intervalStdFloatManager.subtractIntervalResult(timeQueue.front)
//    computeResultAssistStd.substractAssist(outStd)
//    val outSum= intervalSumResultManager.subtractIntervalResult(timeQueue.front)
//    computeResultAssistSum.substractAssist(outSum)
//    val outCount=intervalCountResultManager.subtractIntervalResult(timeQueue.front)
//    computeResultAssistCount.substractAssist(outCount)
//    timeQueue.dequeue()
//  }
  def computeResult(computeType:String): Any ={
    computeType match{
      case "distinctcount"=>
      resultDistinctCountAssist.compute()
      case "sum"=>
        computeResultAssistSum.compute()
      case "count"=>
        computeResultAssistCount.compute()
      case "max"=>
        computeResultAssistMax.compute()
      case "min"=>
        computeResultAssistMin.compute()
      case "avg"=>
       val avg= computeResultAssistSum.compute()/computeResultAssistCount.compute()
        avg
      case "median"=>
        computeResultAssistMedian.compute()
      case "std"=>
        val avg= computeResultAssistSum.compute()/computeResultAssistCount.compute()
        computeResultAssistStd.compute(avg)
      case _=>
    }

  }

  override def toString: String ={

    if(computeType==null){
     "0"
    }
    else {
     computeResult(computeType).toString
    }
  }

  def toResult:(Long,Long,String)={
    (inputBegintime,inputEndtime,computeResult(computeType).toString)

  }



}

object ResultManagerTest{

  def main(args: Array[String]) {
    val jsonStr= new  mutable.HashMap[String,Int]()
    jsonStr.put("wangqiaoshi",11)
    jsonStr.put("zhangshan",4)
    jsonStr.put("wangwu",6)
  }

}



