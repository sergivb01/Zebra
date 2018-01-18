package me.sergivb01.sutils;

import me.sergivb01.sutils.commands.punishments.actions.*;
import me.sergivb01.sutils.commands.punishments.checks.AltsCommand;
import me.sergivb01.sutils.commands.punishments.checks.CheckbanCommand;
import me.sergivb01.sutils.commands.punishments.checks.HistoryCommand;
import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.player.PlayerListener;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class ServerUtils extends JavaPlugin{

	public void onEnable(){
		final File configFile = new File(this.getDataFolder() + "/config.yml");
		if (!configFile.exists()) {
			this.saveDefaultConfig();
		}
		this.getConfig().options().copyDefaults(true);

		ConfigUtils.updateConfig(this);

		new PlayerListener(this); //Registers as event itself

		new RedisDatabase(this);
		new MongoDBDatabase(this);

		//Actions
		getCommand("ban").setExecutor(new BanCommand());
		getCommand("blacklist").setExecutor(new BlacklistCommand());
		getCommand("kick").setExecutor(new KickCommand());
		getCommand("unban").setExecutor(new UnbanCommand());
		getCommand("unblacklist").setExecutor(new UnblacklistCommand());

		//Checks
		getCommand("alts").setExecutor(new AltsCommand());
		getCommand("checkban").setExecutor(new CheckbanCommand());
		getCommand("hist").setExecutor(new HistoryCommand());

		Map<String, Map<String, Object>> map = getDescription().getCommands();
		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			PluginCommand command = getCommand(entry.getKey());
			command.setPermission("sutils.command." + entry.getKey());
			command.setPermissionMessage(ChatColor.translateAlternateColorCodes('&', "&e&l⚠ &cYou do not have permissions to execute this command."));
		}
	}

	public void onDisable(){
		RedisDatabase.getSubscriber().getJedisPubSub().unsubscribe();
		RedisDatabase.getPool().destroy();
	}

}
