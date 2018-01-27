package me.sergivb01.sutils;

import me.sergivb01.sutils.commands.staff.*;
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

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		new PlayerListener(this); //Registers as event itself

		new RedisDatabase(this);
		new MongoDBDatabase(this);

		//Staff
		getCommand("report").setExecutor(new ReportCommand());
		getCommand("request").setExecutor(new RequestCommand());
		getCommand("sc").setExecutor(new StaffChatCommand());
		getCommand("staffserver").setExecutor(new StaffServerCommand(this));

		//Test command
		getCommand("test").setExecutor(new TestCommand());


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

	public static void broadcastKoth(String kothName){
		RedisDatabase.getPublisher().write("koth;" + kothName + ";" + ConfigUtils.SERVER_NAME + ";placeholder");
	}

}
