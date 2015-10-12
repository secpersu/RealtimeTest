package cn.tongdun.www.Utils

import scala.util.Random


class SkipList[T <% Ordered[T]] {

  /**
   * 随机化的概率,每层节点拥有上一层指针的概率
   */
  private val P = 0.5

  /**
   * 最高层级为8，则 N的合适范围在 2^^8
   */
  private val MaxLevel = 8

  private val header: SkipNode[T] = new SkipNode[T](MaxLevel)

  def head=header.forward(0).value


  /**
   * 当前的最大层级编号，从0开始编号
   */
  private var level = 0

  /**
   * 随机产生插入节点的层高
   * @return
   */
  def randomLevel = {
    val lvl = (Math.log(1.0 - Math.random()) / Math.log(1 - P)).toInt
    Math.min(lvl, MaxLevel)
  }

  /**
   * 检查是否包含元素
   * @param o
   * @return
   */
  def contains(o: T) = {
    var x = header
    for (i <- level.to(0, -1)) {
      while (x.forward(i) != null && x.forward(i).value < o) {
        x = x.forward(i)
      }
    }
    x = x.forward(0)
    x != null && x.value.equals(o)
  }

  /**
   * 插入元素
   * @param o
   */
  def insert(o: T) {
    var x = header
    val update = new Array[SkipNode[T]](MaxLevel + 1)
    for (i <- level.to(0, -1)) {
      while (x.forward(i) != null && x.forward(i).value < o) {
        x = x.forward(i)
      }
      update(i) = x
    }
    x = x.forward(0)

    /**
     * 如果不存在，则创建节点
     */
    if (x == null || x.value != o) {
      val  lvl = randomLevel
      if (lvl > level) {
        for (i <- level to lvl) {
          update(i) = header
        }
        level = lvl
      }
      x = new SkipNode[T](o, lvl)
      for (i <- 0 to lvl) {
        x.forward(i) = update(i).forward(i)
        update(i).forward(i) = x
      }
    }

  }

  /**
   * 删除操作
   * @param o
   */
  def delete(o: T) {
    var x = header
    val update = new Array[SkipNode[T]](MaxLevel + 1)
    for (i <- level.to(0, -1)) {
      while (x.forward(i) != null && x.forward(i).value < o) {
        x = x.forward(i)
      }
      update(i) = x
    }
    x = x.forward(0)
    //元素存在的情况下才需要删除
    if (x != null && x.value == o) {
      for (i <- 0 to level) {
        if (update(i).forward(i) == x) {
          update(i).forward(i) = x.forward(i)
        }
      }
      while (level > 0 && header.forward(level) == null) {
        level = level - 1
      }
    }
  }
}


/**
 * 跳跃表中的节点
 * @tparam T
 */
private class SkipNode[T](val level: Int) {

  var value: T = _

  /**
   * 指向多个层级的下个节点的指针数组
   */
  val forward: Array[SkipNode[T]] = new Array[SkipNode[T]](level + 1)

  def this(ve: T, level: Int) = {
    this(level)
    value = ve
  }
  def head=forward(level).value

}

object SkipListApp {
  def main(args: Array[String]) {


    val sList = new SkipList[Int]


   for(index<-1 to 10){

     sList.insert(Random.nextInt(10000))
   }
    val beginTime=System.currentTimeMillis()
    sList.insert(0)
    val endTime=System.currentTimeMillis()
    println(sList.head)
    println(endTime-beginTime)


  }
}
