package me.sergivb01.sutils.database.redis.pubsub;

import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.queue.QueueAPI;
import me.sergivb01.sutils.utils.ConfigUtils;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;


public class Subscriber {
	private ServerUtils instance;
	@Getter private JedisPubSub jedisPubSub;
	private Jedis jedis;

	public Subscriber(ServerUtils instance) {
		this.instance = instance;
		this.jedis = new Jedis(ConfigUtils.REDIS_HOST, ConfigUtils.REDIS_PORT, ConfigUtils.REDIS_TIMEOUT);
		if(ConfigUtils.REDIS_AUTH_ENABLED){
			this.jedis.auth(ConfigUtils.REDIS_AUTH_PASSWORD);
		}
		this.init();
	}

	private void init() {
		jedisPubSub = this.get();
		new Thread(() -> jedis.subscribe(jedisPubSub, ConfigUtils.REDIS_CHANNEL)).start();
	}

	private JedisPubSub get() {
		return new JedisPubSub() {
			@Override
			public void onMessage(final String channel, final String message) {
				final String[] args = message.split(";");
				final String command = args[0].toLowerCase();
				if(command.equalsIgnoreCase("reqserverstatus") && args[1].equalsIgnoreCase(ConfigUtils.SERVER_NAME)){
					RedisDatabase.sendStatus(true);
					return;
				}
				if(command.equalsIgnoreCase("reqsend")){
					final String sender = args[1];
					final String server = args[2];
					Player player = Bukkit.getPlayer(sender);

					if(player != null) {
						final ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
						dataOutput.writeUTF("Connect");
						dataOutput.writeUTF(server);
						player.sendMessage(YELLOW + "Sending you to " + GOLD + server);
						player.sendPluginMessage(instance, "BungeeCord", dataOutput.toByteArray());
					}
					return;
				}

				if (args.length > 3) {
					final String sender = args[1];
					final String server = args[2];
					final String msg = args[3];
					switch (command) {
						case "queuestatus":
							if(QueueAPI.queues.containsKey(sender)) {
								QueueAPI.queues.remove(sender);
							}
							QueueAPI.queues.put(sender, Document.parse(server));
							break;
						case "statusof":
							if(QueueAPI.statuses.containsKey(sender)) {
								QueueAPI.statuses.remove(sender);
							}
							QueueAPI.statuses.put(sender, Document.parse(server));
							break;
						case "koth":
							new FancyMessage(DARK_GRAY + "[" + BLUE + server + DARK_GRAY + "]")
									.then("Event ")
									.color(BLUE)
									.then(sender)
									.color(WHITE)
									.then(" is now running on ")
									.color(BLUE)
									.then(server)
									.command("/staffserver " + server)
									.tooltip(AQUA + "Click to go to " + server)
									.color(WHITE)
									.then("!")
									.color(BLUE)
									.send(Bukkit.getOnlinePlayers());
							break;
						case "staffchat":
							new FancyMessage("(Staff) ")
									.color(BLUE)
									.then("[" + server + "] ")
									.color(DARK_AQUA)
									.command("/staffserver " + server)
									.tooltip(GRAY + "Click to teleport to " + server)
									.then(sender + ": ")
									.color(AQUA)
									.then(msg).send(getStaff());
							break;
						case "request":
							new FancyMessage("[Request] ")
									.color(BLUE)
									.then("[" + server + "] ")
									.color(GRAY)
									.command("/staffserver " + server)
									.tooltip(GRAY + "Click to teleport to " + server)
									.then(sender)
									.color(AQUA)
									.then(" has requested assistance.")
									.color(GRAY)
									.then("\n   Reason: ")
									.color(BLUE)
									.then(msg)
									.color(GRAY)
									.send(getStaff());
							break;
						case "report":
							final String reportedPlayer = args[4];
							new FancyMessage("[Report] ")
									.color(RED)
									.then("[" + server + "] ")
									.color(GRAY)
									.command("/staffserver " + server)
									.tooltip(GRAY + "Click to teleport to " + server)
									.then(reportedPlayer)
									.color(AQUA)
									.then(" has been reported.")
									.tooltip(GRAY + "Report submitted by " + AQUA + sender)
									.color(GRAY)
									.then("\n   Reason: ")
									.color(RED)
									.then(msg.replace(reportedPlayer + " ", ""))
									.color(GRAY)
									.send(getStaff());
							break;
						case "staffswitch":
							new FancyMessage("(Staff) ")
									.color(BLUE)
									.then("[" + server + "] ")
									.color(DARK_AQUA)
									.command("/staffserver " + server)
									.then(sender + " has " + msg + ".")
									.color(AQUA)
									.send(getStaff());
							break;
						default:
							System.out.println("I don't know how to handle this dude! [" + message + "]");
							break;
					}
				}
			}

			public void onPMessage(final String s, final String s1, final String s2) { }
			public void onSubscribe(final String s, final int i) { }
			public void onUnsubscribe(final String s, final int i) { }
			public void onPUnsubscribe(final String s, final int i) { }
			public void onPSubscribe(final String s, final int i) {}
		};

	}

	private Collection<Player> getStaff(){
		return Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("rank.staff")).collect(Collectors.toList());
	}

}