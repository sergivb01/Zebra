package me.sergivb01.sutils.database.redis;

import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.redis.pubsub.Publisher;
import me.sergivb01.sutils.database.redis.pubsub.Subscriber;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bson.Document;
import org.bukkit.Bukkit;

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

	public static void sendStatus(boolean up){
		Document document = new Document("type", "serverstatus")
				.append("name", ConfigUtils.SERVER_NAME)
				.append("up", up)
				.append("tps", new Document("tps0", Bukkit.spigot().getTPS()[0])
						.append("tps1", Bukkit.spigot().getTPS()[1])
						.append("tps2", Bukkit.spigot().getTPS()[2]))
				.append("online", Bukkit.getOnlinePlayers().size())
				.append("whitelist", Bukkit.hasWhitelist())
				.append("maxplayers", Bukkit.getMaxPlayers());
		RedisDatabase.getPublisher().write("payload;" + ConfigUtils.SERVER_NAME + ";" + document.toJson());
	}


}
