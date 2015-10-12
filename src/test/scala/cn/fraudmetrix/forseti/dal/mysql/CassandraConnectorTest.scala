package cn.fraudmetrix.forseti.dal.mysql

/**
 * Created by wangqiaoshi on 15/9/12.
 */


class CassandraConnectorTest{


  def output(): Unit ={
    println("successs")
  }
}
object CassandraConnectorTest {

  def apply(): CassandraConnectorTest =
  {
     new CassandraConnectorTest()
  }

  def main(args: Array[String]) {
    val a= CassandraConnectorTest()
    a.output()
  }

}
