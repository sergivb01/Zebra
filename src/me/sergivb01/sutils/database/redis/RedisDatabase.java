package me.sergivb01.sutils.database.redis;

import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.redis.pubsub.Publisher;
import me.sergivb01.sutils.database.redis.pubsub.Subscriber;
import me.sergivb01.sutils.utils.ConfigUtils;

public class RedisDatabase {
	private ServerUtils instance;
	@Getter public static Publisher publisher;
	@Getter public static Subscriber subscriber;

	public RedisDatabase(ServerUtils instance){
		this.instance = instance;
		init();
	}

	private void init() {
		publisher = new Publisher(instance, ConfigUtils.REDIS_CHANNEL);
		subscriber = new Subscriber(instance);
	}


}
