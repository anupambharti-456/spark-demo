package com.fcbox.spark.common.redis

import com.fcbox.spark.common.util.ResourceUtils
import redis.clients.jedis._

object RedisUtils {


  def getJedisSentinelPool: JedisSentinelPool = {
    val jedisPoolConfig = new JedisPoolConfig
    val sentinels = new java.util.HashSet[String]
    sentinels.add(ResourceUtils.getConfig("redis.sentinel1"))
    sentinels.add(ResourceUtils.getConfig("redis.sentinel2"))
    sentinels.add(ResourceUtils.getConfig("redis.sentinel3"))
    new JedisSentinelPool(ResourceUtils.getConfig("redis.master"), sentinels, jedisPoolConfig, ResourceUtils.getInt("redis.timeout"), ResourceUtils.getConfig("redis.password"), ResourceUtils.getInt("redis.database"))
  }


  def destory(jedisSentinelPool: JedisSentinelPool): Unit = {
    jedisSentinelPool.destroy()
  }

  def invoke[T](call: Jedis => T): T = {
    val pool = getJedisSentinelPool
    try {
      call(pool.getResource)
    } finally {
      pool.destroy()
    }
  }

}
