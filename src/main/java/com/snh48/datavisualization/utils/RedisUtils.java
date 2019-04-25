package com.snh48.datavisualization.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisUtils {
    private static JedisPool jedisPool = null;

    //初始化redis连接池
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //配置最大jedis实例数
        config.setMaxTotal(1000);
        //配置资源池最大闲置数
        config.setMaxIdle(200);
        //等待可用连接的最大时间
        config.setMaxWaitMillis(10000);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool("127.0.0.1", 6379);
    }

    //获取Jedis实例
    public static Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return jedis;
    }
}
