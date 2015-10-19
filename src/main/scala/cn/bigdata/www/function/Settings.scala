package cn.bigdata.www.function

/**
 * Created by wangqiaoshi on 15/9/10.
 */
import akka.japi.Util.immutableSeq
import com.typesafe.config.ConfigFactory
object Settings {
  protected val config = ConfigFactory.load().getConfig("streaming-realtime")

  val SparkMaster: String = sys.props.get("spark.master").getOrElse(config.getString("spark.master"))

  val StreamingBatchInterval = config.getInt("spark.streaming.batch.interval")

//  val SparkExecutorMemory = config.getString("spark.executor.memory")
//  val SparkCoresMax = sys.props.get("spark.cores.max").getOrElse(config.getInt("spark.cores.max"))





  val sparkcassandraconnectiontimeout_ms=config.getInt("spark.cassandra.connection.timeout_ms")
  val sparkcassandraconnectionkeep_alive_ms=config.getInt("spark.cassandra.connection.keep_alive_ms")
  val CassandraSeedNodes: String = sys.props.get("spark.cassandra.connection.host") getOrElse
    immutableSeq(config.getStringList("spark.cassandra.connection.host")).mkString(",")

  val CassandraKeyspace: String = config.getString("spark.cassandra.keyspace")

  val CassandraTable: String = config.getString("spark.cassandra.table")
  val partitionNum=config.getInt("spark.cassandra.partitions")


}
