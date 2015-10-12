package cn.tongdun.www.metric.impl

import cn.tongdun.www.metric.{IntervalResultManager, AbstractIntervalResultManager}

import scala.collection.mutable.HashMap


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
  key=keys
}

class IntervalCountManager(keys:String) extends  Serializable with IntervalResultManager[Int]{
  key=keys
}

class IntervalMaxManager(keys:String) extends  Serializable with IntervalResultManager[Float]{

key=keys
}

class IntervalMinManager(keys:String) extends  Serializable with IntervalResultManager[Float]{
 key=keys

}


object Test{
  def main(args: Array[String]) {
    val intervalTest=new IntervalTest("wang")

    intervalTest.addIntervalResult(123L,78)

    val beginTime=System.currentTimeMillis()
    intervalTest.addIntervalResult(123L,78)
    val endTime=System.currentTimeMillis()
    println(endTime-beginTime)
  }
}
