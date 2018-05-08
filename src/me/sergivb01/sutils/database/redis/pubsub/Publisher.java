package me.sergivb01.sutils.database.redis.pubsub;

import lombok.Getter;
import me.sergivb01.sutils.utils.ConfigUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Publisher{
	@Getter
	private JedisPool pool;
	private String channel;

	public Publisher(){
		if(ConfigUtils.REDIS_AUTH_ENABLED){ //Handle auth
			pool = new JedisPool(new JedisPoolConfig(), ConfigUtils.REDIS_HOST, ConfigUtils.REDIS_PORT, ConfigUtils.REDIS_TIMEOUT, ConfigUtils.REDIS_AUTH_PASSWORD);
		}else{
			pool = new JedisPool(new JedisPoolConfig(), ConfigUtils.REDIS_HOST, ConfigUtils.REDIS_PORT, ConfigUtils.REDIS_TIMEOUT);
		}
		this.channel = ConfigUtils.REDIS_CHANNEL;
	}

	public void write(final String message){
		Jedis jedis = null;
		try{
			jedis = pool.getResource();
			if(ConfigUtils.REDIS_AUTH_ENABLED){ //Need to auth every single time we write a message
				jedis.auth(ConfigUtils.REDIS_AUTH_PASSWORD);
			}
			jedis.publish(channel, message);
			pool.returnResource(jedis);
		}finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}


}
