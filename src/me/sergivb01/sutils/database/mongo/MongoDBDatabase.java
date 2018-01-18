package me.sergivb01.sutils.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.enums.PunishmentType;
import me.sergivb01.sutils.player.data.PlayerProfile;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoDBDatabase {
	private ServerUtils instance;
	private MongoClient mongoClient;
	private static MongoDatabase mongoDatabase;
	public static MongoCollection<Document> playercollection;
	public static MongoCollection<Document> punishCollection;

	public MongoDBDatabase (ServerUtils instance){
		this.instance = instance;
		init();
	}

	private void init() {
		MongoClientURI uri;
		if(ConfigUtils.MONGO_AUTH_ENABLED){
			uri = new MongoClientURI("mongodb://" + ConfigUtils.MONGO_USERNAME + ":" + ConfigUtils.MONGO_AUTH_PASSWORD +"@" + ConfigUtils.MONGO_HOST + ":" +  ConfigUtils.MONGO_PORT +"/?authSource=" + ConfigUtils.MONGO_DATABASE);
		}else{
			uri = new MongoClientURI("mongodb://" + ConfigUtils.MONGO_HOST + ":" +  ConfigUtils.MONGO_PORT + "/?authSource=" + ConfigUtils.MONGO_DATABASE);
		}
		mongoClient = new MongoClient(uri);
		mongoDatabase = mongoClient.getDatabase(ConfigUtils.MONGO_DATABASE);
		playercollection = mongoDatabase.getCollection("playerdata");
		punishCollection = mongoDatabase.getCollection("punishments");

	}

	public static void saveProfileToDatabase(PlayerProfile playerProfile, boolean online){
		Document doc = new Document("uuid", playerProfile.getPlayer().getUniqueId())
		.append("nickname", playerProfile.getPlayer().getName())
		.append("address", playerProfile.getPlayerData().getAddress())
		.append("lastconn", System.currentTimeMillis())
		.append("version", playerProfile.getPlayerData().getPlayerVersion().toString())
		.append("online", online)
		.append(ConfigUtils.SERVER_NAME, new Document("server", ConfigUtils.SERVER_NAME)
				.append("balance", playerProfile.getPlayerData().getBalance())
		);

		Document found = playercollection.find(new Document("uuid", playerProfile.getPlayer().getUniqueId())).first();
		if(found != null){
			Bson updateOperation = new Document("$set", doc);
			playercollection.updateOne(found, updateOperation);
		}else{
			playercollection.insertOne(doc);
		}
	}

	//Used by /alts command
	public static List<String> getPlayerAlts(UUID playerUUID){
		List<String> toReturn = new ArrayList<>();
		String address = playercollection.find(new Document("uuid", playerUUID)).first().getString("address");
		FindIterable<Document> findIterable = playercollection.find(new Document("address", address));
		for(Document doc : findIterable){
			toReturn.add(doc.getString("nickname"));
		}
		return toReturn;
	}

	//Used by /alts command
	public static List<String> getIPAlts(String address){
		List<String> toReturn = new ArrayList<>();
		FindIterable<Document> findIterable = playercollection.find(new Document("address", address));
		for(Document doc : findIterable){
			toReturn.add(doc.getString("nickname"));
		}
		return toReturn;
	}

	//Used by /blacklist command
	public static void setBlacklisted(UUID uuid, boolean blacklist){
		Document found = playercollection.find(new Document("uuid", uuid)).first();
		if(found != null){
			Bson updateOperation = new Document("$set", new Document("blacklisted", blacklist));
			playercollection.updateOne(found, updateOperation);
		}
	}

	//Used by /checkban command
	public static boolean isBlacklisted(UUID uuid){
		Document found = playercollection.find(new Document("uuid", uuid)).first();
		if(found != null){
			return found.getBoolean("blacklisted", false);
		}
		return false;
	}

	//Used by /ban - Offline player
	public static void addPunishment(PunishmentType punishmentType, String target, UUID sender, String reason, long duration, boolean isIp){
		Document doc = new Document("uuid", (isIp ? target : UUID.fromString(target)))
				.append("type", punishmentType.toString().toLowerCase())
				.append("sender", sender)
				.append("reason", reason)
				.append("expires", (duration == -1 ? -1 : System.currentTimeMillis() + duration))
				.append("server", ConfigUtils.SERVER_NAME)
				.append("created", System.currentTimeMillis());
		punishCollection.insertOne(doc);
	}

	public static void unbanPlayer(UUID uuid, UUID sender){
		Document find1 = new Document("uuid", uuid)
				.append("type", "ban");
		Document find2 = new Document("uuid", uuid)
				.append("type", "tempban");
		Document find3 = new Document("uuid", uuid)
				.append("type", "ipban");

		Bson updateOperation = new Document("$set", new Document("expires", 0)
				.append("unbanned-by", sender)
				.append("unbanned-at", ConfigUtils.SERVER_NAME)
				.append("unbanned-time", System.currentTimeMillis())
		);
		punishCollection.updateMany(find1, updateOperation);
		punishCollection.updateMany(find2, updateOperation);
		punishCollection.updateMany(find3, updateOperation);
	}

	//Used for kick on login
	public static Document getLastBan(UUID target){
		FindIterable<Document> findIterable1 = punishCollection.find(new Document("uuid", target).append("type", "ban"));
		FindIterable<Document> findIterable2 = punishCollection.find(new Document("uuid", target).append("type", "ipban"));
		ArrayList<Document> total = new ArrayList<>();

		for(Document doc : findIterable1) total.add(doc);
		for(Document doc2 : findIterable2) total.add(doc2);

		Document toReturn = new Document("expires", (long)0);
		for(Document document : total){
			if(String.valueOf(document.get("expires")).equals("0")){
				continue;
			}
			long docExpires = document.getLong("expires");
			if(docExpires > ((long)toReturn.get("expires")) || docExpires == (long)-1){
				toReturn = document;
			}
		}
		return toReturn;
	}

	//Used by hist command
	public static List<Document> getPunishmentsByPunished(UUID playerUUID){
		List<Document> toReturn = new ArrayList<>();
		FindIterable<Document> findIterable = punishCollection.find(new Document("uuid", playerUUID));
		for(Document doc : findIterable){
			toReturn.add(doc);
		}
		return toReturn;
	}

	public static List<Document> getPunishmentByAddress(String address){
		List<Document> toReturn = new ArrayList<>();
		FindIterable<Document> findIterable = punishCollection.find(new Document("uuid", address));
		for(Document doc : findIterable){
			toReturn.add(doc);
		}
		return toReturn;
	}

	public static List<Document> getPunishmentsBySender(UUID uuid){
		List<Document> toReturn = new ArrayList<>();
		FindIterable<Document> findIterable = punishCollection.find(new Document("sender", uuid));
		for(Document doc : findIterable){
			toReturn.add(doc);
		}
		return toReturn;
	}

	public static Document getPunishmentByID(int id){
		Document find = punishCollection.find(new Document("id", id)).first();
		return find;
	}

}
