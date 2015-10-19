package cn.bigdata.www.redis

/**
 * Created by wangqiaoshi on 15/10/8.
 */

import com.redis.RedisClient
import com.redis.RedisClientPool
import org.apache.spark.Logging

class RedisConnector(host:String,port:Int) extends Logging with Serializable{



}

object RedisConnector extends  Logging{

  val clients = new RedisClientPool("192.168.6.52",6379)

}
