package me.sergivb01.sutils.queue;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bson.Document;

import java.util.HashMap;
import java.util.Optional;

public class QueueAPI {
	public static HashMap<String, Document> statuses = new HashMap<>();

	public static void addPlayer(String playerName, String server, int priority){
		Document document = new Document("type", "addplayer")
				.append("player", playerName)
				.append("server", server)
				.append("priority", priority);
		RedisDatabase.getPublisher().write("payload;" + ConfigUtils.SERVER_NAME + ";" + document.toJson());
	}

	public static void removePlayer(String playerName){
		Document document = new Document("type", "removeplayer")
				.append("player", playerName);
		RedisDatabase.getPublisher().write("payload;" + ConfigUtils.SERVER_NAME + ";" + document.toJson());
	}

	public static Document isPlayerInQueue(String playerName){
		Optional<Document> optional = statuses.values().stream().filter(document -> document.containsKey(playerName)).findFirst();

		return optional.orElse(null);
	}



}
