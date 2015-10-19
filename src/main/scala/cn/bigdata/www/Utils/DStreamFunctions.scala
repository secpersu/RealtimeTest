package cn.bigdata.www.Utils

import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.DStream

/**
 * Created by wangqiaoshi on 15/10/8.
 */
class DStreamFunctions[T](dstream:DStream[T]) extends Serializable{

//  override  def sparkContext:SparkContext = dstream.context.sparkContext
//  def conf = sparkContext.getConf



}
