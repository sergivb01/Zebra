package me.sergivb01.sutils.database.redis;

import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.redis.pubsub.Publisher;
import me.sergivb01.sutils.database.redis.pubsub.Subscriber;
import me.sergivb01.sutils.utils.ConfigUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDatabase {
	private ServerUtils instance;
	@Getter private static JedisPool pool;
	@Getter public static Publisher publisher;
	@Getter public static Subscriber subscriber;

	public RedisDatabase(ServerUtils instance){
		this.instance = instance;
		init();
	}

	private void init() {
		if(instance.getConfig().getBoolean("redis.auth.enabled")) {
			pool = new JedisPool(new JedisPoolConfig(), ConfigUtils.REDIS_HOST, ConfigUtils.REDIS_PORT,  ConfigUtils.REDIS_TIMEOUT,  ConfigUtils.REDIS_AUTH_PASSWORD);
		}else {
			pool = new JedisPool(new JedisPoolConfig(), ConfigUtils.REDIS_HOST, ConfigUtils.REDIS_PORT,  ConfigUtils.REDIS_TIMEOUT);
		}

		publisher = new Publisher(this, ConfigUtils.REDIS_CHANNEL);
		subscriber = new Subscriber(instance);
	}


}
