package cn.tongdun.www.metric.impl

import cn.tongdun.www.metric.{IntervalResultManager, AbstractIntervalResultManager,IntervalHashMapManager}

import scala.collection.mutable
import scala.collection.mutable.{HashMap,Map}


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

class IntervalDistinctCountManager(keys:String) extends  Serializable with IntervalHashMapManager{

 key=keys
}
class IntervalMedianManager(keys:String) extends  Serializable with IntervalHashMapManager{

 key=keys
}

class IntervalStdManager(keys:String) extends  Serializable with IntervalHashMapManager{
  key=keys

}

object Test{
  def main(args: Array[String]) {
    val distinct = new IntervalDistinctCountManager("distinct")
    var beginTime=System.currentTimeMillis()
    val hashMap=new HashMap[String,Int]
    hashMap.put("192.168",13)
    distinct.addIntervalResult(123l,hashMap)
    var endTime=System.currentTimeMillis()
    println(endTime-beginTime)

    beginTime=System.currentTimeMillis()
    distinct.subtractIntervalResult(123l)
    endTime=System.currentTimeMillis()
    println("substract "+(endTime-beginTime))




  }
}
