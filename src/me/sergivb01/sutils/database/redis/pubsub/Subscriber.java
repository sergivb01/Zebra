package me.sergivb01.sutils.database.redis.pubsub;

import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.utils.ConfigUtils;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;


public class Subscriber {
	private ServerUtils instance;
	@Getter private JedisPubSub jedisPubSub;
	private Jedis jedis;

	public Subscriber(ServerUtils instance) {
		this.instance = instance;
		this.jedis = new Jedis(ConfigUtils.REDIS_HOST, ConfigUtils.REDIS_PORT, ConfigUtils.REDIS_TIMEOUT);
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
				if (args.length > 3) {
					final String command = args[0].toLowerCase();
					final String sender = args[1];
					final String server = args[2];
					final String msg = args[3];
					switch (command) {
						case "koth":
							new FancyMessage("[Koth Alert] ")
									.color(YELLOW)
									.then(sender)
									.color(GOLD)
									.then(" is now running on ")
									.color(YELLOW)
									.then(server)
									.color(GOLD)
									.then("!")
									.color(YELLOW)
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
									.then("[" + server + "]")
									.color(DARK_AQUA)
									.then(sender + " " + msg)
									.color(AQUA)
									.send(getStaff());
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

	private List<Player> getStaff(){
		List<Player> staff = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.hasPermission("rank.staff")){
				staff.add(player);
			}
		}
		return staff;
	}

}