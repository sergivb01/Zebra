package me.sergivb01.sutils.player.data;

import lombok.Getter;
import lombok.Setter;
import me.sergivb01.sutils.enums.PlayerVersion;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {
	@Getter private String address;
	@Getter private UUID playerUUID;
	@Getter private String currentServer;
	@Getter @Setter PlayerVersion playerVersion;

	//Dynamic stuff (Balance, faction name, ...)
	//@Getter @Setter private int balance = 0;

	public PlayerData (Player player){
		this.address = player.getAddress().getHostString();
		this.playerUUID = player.getUniqueId();
		this.currentServer = ConfigUtils.SERVER_NAME;
		this.playerVersion = getVersionForPlayer(player);
	}

	private PlayerVersion getVersionForPlayer (Player player){
		int version = ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion();
		if(version >= 47){
			return  PlayerVersion.Version_1_8;
		}else if (version == 5 || version == 4){
			return PlayerVersion.Version_1_7;
		}
		return PlayerVersion.UNKOWN;
	}

}
