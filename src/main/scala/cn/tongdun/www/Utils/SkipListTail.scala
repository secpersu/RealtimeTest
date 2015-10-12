package cn.tongdun.www.Utils

/**
 * Created by wangqiaoshi on 15/9/19.
 */
import scala.collection.mutable
import scala.util.Random

class SkipListTail {

  /**
   * 随机化的概率,每层节点拥有上一层指针的概率
   */
  private val P = 0.5

  /**
   * 最高层级为8，则 N的合适范围在 2^^8
   */
  private val MaxLevel = 4

  private val tailer: SkipNode = new SkipNode(MaxLevel)
  /**
   * 当前的最大层级编号，从0开始编号
   */
  private var level = 0

  /**
   * 随机产生插入节点的层高
   * @return
   */
  def randomLevel = {
    Random.nextInt(MaxLevel)
//    val lvl = (Math.log(1.0 - Math.random()) / Math.log(1 - P)).toInt
//    Math.min(lvl, MaxLevel)
  }
  /**
   * 插入元素
   * @param o
   */
  def insert(o: Long) {
    var x = tailer

    val update = new Array[SkipNode](MaxLevel + 1)




    if(x.forward(0) == null || x.forward(0).value < o){

      val  lvl = randomLevel
      x = new SkipNode(o, lvl)
      for(i<-0 to lvl) {
        x.forward(i) = tailer.forward(i)
        tailer.forward(i) = x

      }
    }


    else {
      for (i <- 0 to level) {
        while (x.forward(i) != null && x.forward(i).value > o) {
          x = x.forward(i)
        }
        update(i) = x
      }
      x = x.forward(0)

      /**
       * 如果不存在，则创建节点
       */
      if (x == null || x.value != o) {
        val lvl = randomLevel
        if (lvl > level) {
          for (i <- level to lvl) {
            update(i) = tailer
          }
          level = lvl
        }
        x = new SkipNode(o, lvl)
        for (i <- 0 to lvl) {
          x.forward(i) = update(i).forward(i)
          update(i).forward(i) = x
        }
      }
    }

  }

  def toList(): mutable.MutableList[Long] ={
   val list=new mutable.MutableList[Long]()
    var item=tailer.forward(0)
   while(item!=null){
     list.+=(item.value)
     item=item.forward(0)
   }
    list
  }

  private class SkipNode(val level: Int) {

    var value: Long = _

    /**
     * 指向多个层级的下个节点的指针数组
     */

    val forward: Array[SkipNode] = new Array[SkipNode](level + 1)

    def this(ve: Long, level: Int) = {
      this(level)
      value = ve
    }
    def head=forward(level).value

  }


}

object SkipListTailApp{
  def main(args: Array[String]) {

    val timeQueue=new mutable.Queue[Int]()
    timeQueue.enqueue(10)
    var beginTime=System.currentTimeMillis()
    timeQueue.enqueue(10)
    var endTime=System.currentTimeMillis()
//    println(endTime-beginTime)





    val tail =new SkipListTail()
    beginTime=System.currentTimeMillis()
    tail.insert(10)
    endTime=System.currentTimeMillis()
    println(endTime-beginTime)




    tail.insert(11)
    tail.insert(11)
    tail.insert(9)
    println(tail.toList())
  }

}