package me.sergivb01.sutils.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.sergivb01.sutils.enums.PlayerVersion;
import me.sergivb01.sutils.utils.ConfigUtils;
import net.veilmc.base.BasePlugin;
import net.veilmc.hcf.HCF;
import net.veilmc.hcf.deathban.Deathban;
import net.veilmc.hcf.faction.type.PlayerFaction;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoDBDatabase{
	private static MongoCollection<Document> playercollection;
	private static MongoCollection<Document> factionCollection;
	private static MongoCollection<Document> deathCollection;

	public MongoDBDatabase(){
		init();
	}

	private void init(){
		MongoClientURI uri;
		if(ConfigUtils.MONGO_AUTH_ENABLED){
			uri = new MongoClientURI("mongodb://" + ConfigUtils.MONGO_USERNAME + ":" + ConfigUtils.MONGO_AUTH_PASSWORD + "@" + ConfigUtils.MONGO_HOST + ":" + ConfigUtils.MONGO_PORT + "/?authSource=" + ConfigUtils.MONGO_DATABASE);
		}else{
			uri = new MongoClientURI("mongodb://" + ConfigUtils.MONGO_HOST + ":" + ConfigUtils.MONGO_PORT + "/?authSource=" + ConfigUtils.MONGO_DATABASE);
		}

		MongoClient mongoClient = new MongoClient(uri);

		MongoDatabase mongoDatabase = mongoClient.getDatabase(ConfigUtils.MONGO_DATABASE);
		playercollection = mongoDatabase.getCollection("playerdata");
		factionCollection = mongoDatabase.getCollection("factions");
		deathCollection = mongoDatabase.getCollection("deaths");

	}

	public static void addDeathSave(Document document){
		deathCollection.insertOne(document);
		if(ConfigUtils.DEBUG){
			System.out.println("Added death save! " + document.toJson());
		}
	}

	private static void saveFactionToDatabase(PlayerFaction playerFaction){
		List<String> allies = new ArrayList<>();
		playerFaction.getAlliedFactions().forEach(playerFaction1 -> allies.add(playerFaction.getName()));
		List<String> members = new ArrayList<>();
		playerFaction.getMembers().forEach((x, y) -> members.add(x.toString()));

		Document doc = new Document("uuid", playerFaction.getUniqueID()) //Unique identifier by faction, as player UUID
				.append("server", ConfigUtils.SERVER_NAME)
				.append("name", playerFaction.getName())
				.append("players", playerFaction.getMembers().keySet())
				.append("players_str", members)
				.append("leader", playerFaction.getLeader().getUniqueId())
				.append("allies", allies)
				.append("balance", playerFaction.getBalance())
				.append("dtr", playerFaction.getDeathsUntilRaidable());

		Document found = factionCollection.find(new Document("uuid", playerFaction.getUniqueID())).first();
		if(found != null){
			Bson updateOperation = new Document("$set", doc);
			factionCollection.updateOne(found, updateOperation);
		}else{
			factionCollection.insertOne(doc);
		}

	}

	public static void saveProfileToDatabase(Player player, boolean online){
		UUID uuid = player.getUniqueId();
		PlayerFaction faction = HCF.getPlugin().getFactionManager().getPlayerFaction(uuid);
		if(faction != null){
			saveFactionToDatabase(faction);
		}

		Deathban deathban = HCF.getPlugin().getUserManager().getUser(uuid).getDeathban();

		Document profile = new Document("spawn_tokens", HCF.getInstance().getUserManager().getUser(uuid).getSpawnTokens())
				.append("kills", player.getStatistic(Statistic.PLAYER_KILLS))
				.append("deaths", player.getStatistic(Statistic.DEATHS))
				.append("playtime", BasePlugin.getPlugin().getPlayTimeManager().getTotalPlayTime(uuid))
				.append("notes", BasePlugin.getPlugin().getUserManager().getUser(uuid).getNotes())
				.append("lives", HCF.getPlugin().getDeathbanManager().getLives(uuid))
				.append("deathban",
						//Save deathban
						new Document("reason", (deathban != null) ? deathban.getReason() : "none")
								.append("created", (deathban != null) ? deathban.getCreationMillis() : "none")
								.append("expires", (deathban != null) ? deathban.getExpiryMillis() : "none")
								.append("location", (deathban != null) ? deathban.getDeathPoint().getBlockX() + ";" + deathban.getDeathPoint().getBlockY() + ";" + deathban.getDeathPoint().getBlockZ() : "none"))
				.append("faction",
						//Save faction
						new Document("name", (faction != null) ? faction.getName() : "none")
								.append("role", (faction != null) ? faction.getMember(uuid).getRole().toString() : "none")
								.append("id", (faction != null) ? faction.getUniqueID() : "none"))
				.append("ores",
						//Save player ores
						new Document("diamonds", player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE))
								.append("gold", player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE))
								.append("coal", player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE))
								.append("iron", player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE))
								.append("lapiz", player.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE))
								.append("redstone", player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE))
								.append("emerald", player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE))
				);


		//Includes all player profile (faction, )
		Document doc = new Document("uuid", uuid)
				.append("uuid_str", uuid.toString())
				.append("rank", PermissionsEx.getUser(player).getGroups()[0].getName())
				.append("nickname", player.getName())
				.append("address", player.getAddress().getHostString())
				.append("lastconn", System.currentTimeMillis())
				.append("server", ConfigUtils.SERVER_NAME)
				.append("version", getVersionForPlayer(player).toString())
				.append("online", online)
				.append(ConfigUtils.SERVER_NAME, new Document("profile", profile));

		Document found = playercollection.find(new Document("uuid", uuid)).first();
		if(found != null){
			Bson updateOperation = new Document("$set", doc);
			playercollection.updateOne(found, updateOperation);
		}else{
			playercollection.insertOne(doc);
		}
	}

	public static FindIterable<Document> getRecentDeaths(UUID uuid){
		return deathCollection.find(
				new Document("server", ConfigUtils.SERVER_NAME)
						.append("dead", uuid)
		).sort(new Document("timestamp", -1)).limit(20);
	}

	private static PlayerVersion getVersionForPlayer(Player player){
		int version = ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion();
		if(version >= 47){
			return PlayerVersion.Version_1_8;
		}else if(version == 5 || version == 4){
			return PlayerVersion.Version_1_7;
		}
		return PlayerVersion.UNKOWN;
	}


}
