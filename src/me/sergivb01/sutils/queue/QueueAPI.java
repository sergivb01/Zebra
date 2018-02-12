package me.sergivb01.sutils.queue;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class QueueAPI {
	public static HashMap<String, Document> statuses = new HashMap<>();
	public static HashMap<String, Map<String, Integer>> priorities = new HashMap<>();

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
		priorities.forEach((key, value) ->{
			if(value.containsKey(playerName)){
				priorities.get(key).remove(playerName);
			}
		});
	}

	public static void pauseQueue(String queue){
		Document document = new Document("type", "pause")
				.append("server", queue);
		RedisDatabase.getPublisher().write("payload;" + ConfigUtils.SERVER_NAME + ";" + document.toJson());
	}

	public static void updatePlayersInqueue (String server){
		Document players = (Document) statuses.get(server).get("priorities");
		players.keySet().forEach(str -> priorities.get(server).put(str, players.getInteger(str)));
	}

	public static Document isPlayerInQueue(String playerName){
		Optional<Document> optional = statuses.values().stream().filter(document -> document.containsKey(playerName)).findFirst();

		return optional.orElse(null);
	}



}
