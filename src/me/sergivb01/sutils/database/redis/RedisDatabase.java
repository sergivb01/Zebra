package me.sergivb01.sutils.database.redis;

import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.redis.pubsub.Publisher;
import me.sergivb01.sutils.database.redis.pubsub.Subscriber;

public class RedisDatabase{
	@Getter
	public static ServerUtils instance;
	@Getter
	public static Publisher publisher;
	@Getter
	public static Subscriber subscriber;

	public RedisDatabase(ServerUtils instance){
		RedisDatabase.instance = instance;
		init();
	}

	private void init(){
		publisher = new Publisher();
		subscriber = new Subscriber(instance);
	}


}
