package me.sergivb01.sutils;

import me.sergivb01.sutils.commands.*;
import me.sergivb01.sutils.database.agent.AgentManager;
import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.payload.PayloadSender;
import me.sergivb01.sutils.player.PlayerListener;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class ServerUtils extends JavaPlugin{

	public static void broadcastKoth(String kothName){
		PayloadSender.sendKoth(kothName);
	}

	public void onEnable(){
		final File configFile = new File(this.getDataFolder() + "/config.yml");
		if(!configFile.exists()){
			this.saveDefaultConfig();
		}
		this.getConfig().options().copyDefaults(true);

		ConfigUtils.updateConfig(this);

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		new PlayerListener(this); //Registers as event itself

		new RedisDatabase(this);
		new MongoDBDatabase();
		new AgentManager(this);

		//Staff
		getCommand("report").setExecutor(new ReportCommand());
		getCommand("request").setExecutor(new RequestCommand());
		getCommand("sc").setExecutor(new StaffChatCommand());
		getCommand("staffserver").setExecutor(new StaffServerCommand(this));
		getCommand("dhist").setExecutor(new DeathHistoryCommand());
		getCommand("status").setExecutor(new StatusCommand());
		getCommand("rstatus").setExecutor(new RequestStatusCommand());

		//Test command
		getCommand("test").setExecutor(new TestCommand());
		getCommand("debug").setExecutor(new DebugCommand());


		Map<String, Map<String, Object>> map = getDescription().getCommands();
		for(Map.Entry<String, Map<String, Object>> entry : map.entrySet()){
			PluginCommand command = getCommand(entry.getKey());
			command.setPermission("sutils.command." + entry.getKey());
			command.setPermissionMessage(ChatColor.translateAlternateColorCodes('&', "&e&l⚠ &cYou do not have permissions to execute this command."));
		}

		Bukkit.getScheduler().runTaskLater(this, () -> PayloadSender.sendData(true), 5 * 20L);
	}

	public void onDisable(){
		PayloadSender.sendData(false);

		RedisDatabase.getSubscriber().getJedisPubSub().unsubscribe();
		RedisDatabase.getPublisher().getPool().destroy();
	}


}
