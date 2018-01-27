package me.sergivb01.sutils.player.data;

import lombok.Getter;
import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.player.Cache;
import org.bukkit.entity.Player;

public class PlayerProfile {
	@Getter private Player player;
	@Getter private PlayerData playerData;

	public PlayerProfile(Player player){
		this.player = player;
		this.playerData = new PlayerData(player);
	}

	public void remove(){
		Cache.removeProfile(player.getUniqueId());
	}

	public void save( boolean online){
		MongoDBDatabase.saveProfileToDatabase(this, online);
	}

}
