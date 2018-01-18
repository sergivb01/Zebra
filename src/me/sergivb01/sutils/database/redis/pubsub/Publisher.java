package me.sergivb01.sutils.database.redis.pubsub;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import redis.clients.jedis.Jedis;

public class Publisher {
	private RedisDatabase redisDatabase;
	private String channel;

	public Publisher(RedisDatabase redisDatabase, String channel){
		this.redisDatabase = redisDatabase;
		this.channel = channel;
	}

	public void write(final String message) {
		Jedis jedis = null;
		try {
			jedis = redisDatabase.getPool().getResource();
			jedis.publish(channel, message);
			redisDatabase.getPool().returnResource(jedis);
		}
		finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}


}
