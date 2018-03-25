package me.sergivb01.sutils.payload;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.server.ServerCache;
import me.sergivb01.sutils.utils.ConfigUtils;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

public class PayloadParser{

	public static void parse(String docStr){
		Document doc = Document.parse(docStr);
		String type = doc.getString("type");
		String server = (String) doc.getOrDefault("server", "none");

		switch(type.toLowerCase()){
			case "reqdata":{
				if(doc.getString("req-server").equalsIgnoreCase(ConfigUtils.SERVER_NAME)){
					PayloadSender.sendData(true);
				}
				break;
			}

			case "staffimportant":{
				String player = doc.getString("player");
				String str = doc.getString("command");

				new FancyMessage("(Staff Abuse) ")
						.color(DARK_RED)
						.then(server)
						.color(RED)
						.then(" - " + BOLD + "" + AQUA + player + " ")
						.then("executed " + BOLD)
						.then(LIGHT_PURPLE + "" + BOLD + str)
						.send(Bukkit.getOnlinePlayers().stream()
								.filter(p -> p.hasPermission("sutils.alerts")).collect(Collectors.toList()));
				break;
			}

			case "sendmessage":{
				Player player = Bukkit.getPlayer(doc.getString("player"));
				String message = doc.getString("message");
				if(player != null && player.isOnline()){
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				}
				break;
			}

			case "data":{
				ServerCache.handleData(doc.getString("server"), doc);
				break;
			}

			case "staffchat":{
				String sender = doc.getString("player");
				String msg = doc.getString("message");
				new FancyMessage("(Staff) ")
						.color(BLUE)
						.then("[" + server + "] ")
						.color(DARK_AQUA)
						.command("/staffserver " + server)
						.tooltip(GRAY + "Click to teleport to " + server)
						.then(doc.getString("player") + ": ")
						.command("/tp " + doc)
						.tooltip(GRAY + "Click to teleport to " + sender)
						.then(msg)
						.send(getStaff());
				break;
			}

			case "koth":{
				String koth = doc.getString("koth");

				new FancyMessage(DARK_GRAY + "[" + BLUE + server + DARK_GRAY + "]")
						.then("Event ")
						.color(BLUE)
						.then(koth)
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
			}

			case "request":{
				String sender = doc.getString("player");
				String msg = doc.getString("message");
				new FancyMessage("[Request] ")
						.color(BLUE)
						.then("[" + server + "] ")
						.color(GRAY)
						.command("/staffserver " + server)
						.tooltip(GRAY + "Click to teleport to " + server)
						.then(sender)
						.command("/tp " + sender)
						.tooltip(GRAY + "Click to teleport to " + sender)
						.color(AQUA)
						.then(" has requested assistance.")
						.color(GRAY)
						.send(getStaff());
				new FancyMessage("   Reason: ")
						.color(BLUE)
						.then(msg)
						.color(GRAY)
						.send(getStaff());
				break;
			}

			case "report":{
				String reportedPlayer = doc.getString("target");
				String sender = doc.getString("sender");
				String msg = doc.getString("message");

				new FancyMessage("[Report] ")
						.color(RED)
						.then("[" + server + "] ")
						.color(GRAY)
						.command("/staffserver " + server)
						.tooltip(GRAY + "Click to teleport to " + server)
						.then(reportedPlayer)
						.command("/tp " + reportedPlayer)
						.tooltip(GRAY + "Click to teleport to " + reportedPlayer)
						.color(AQUA)
						.then(" has been reported.")
						.tooltip(GRAY + "Report submitted by " + AQUA + sender)
						.color(GRAY)
						.send(getStaff());
				new FancyMessage("   Reason: ")
						.color(RED)
						.then(msg.replace(reportedPlayer + " ", ""))
						.color(GRAY)
						.send(getStaff());
				break;
			}

			case "staffswitch":{
				String sender = doc.getString("player");
				String msg = doc.getString("action");

				new FancyMessage("(Staff) ")
						.color(BLUE)
						.then("[" + server + "] ")
						.color(DARK_AQUA)
						.command("/staffserver " + server)
						.then(sender + " has " + msg + " " + server + ".")
						.color(AQUA)
						.send(getStaff());
				break;
			}
			default:
				RedisDatabase.instance.getLogger().warning("UNKNOWN TYPE OF PAYLOAD: " + type + " Payload JSON: " + doc.toJson());
				break;

		}

	}

	private static Collection<Player> getStaff(){
		return Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("rank.staff")).collect(Collectors.toList());
	}

}
