package me.sergivb01.sutils.payload;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.server.ServerCache;
import me.sergivb01.sutils.utils.ConfigUtils;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

public class PayloadParser{

	public static void parse(Document doc){
		String type = doc.getString("type");

		switch(type.toLowerCase()){
			case "reqserverstatus":{
				if(doc.getString("server").equalsIgnoreCase(ConfigUtils.SERVER_NAME)){
					RedisDatabase.sendStatus(true);
				}
				break;
			}

			case "srvdata":{
				ServerCache.handleData(doc.getString("server"), doc);
				break;
			}

			case "staffchat":{
				String server = doc.getString("server");
				String sender = doc.getString("sender");
				String msg = doc.getString("message");
				new FancyMessage("(Staff) ")
						.color(BLUE)
						.then("[" + server + "] ")
						.color(DARK_AQUA)
						.command("/staffserver " + server)
						.tooltip(GRAY + "Click to teleport to " + server)
						.then(doc.getString("sender") + ": ")
						.command("/tp " + doc)
						.tooltip(GRAY + "Click to teleport to " + sender)
						.then(msg)
						.send(getStaff());
				break;
			}

			case "koth":{
				String server = doc.getString("server");
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
				String server = doc.getString("server");
				String sender = doc.getString("sender");
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
						.text("\n")
						.then("   Reason: ")
						.color(BLUE)
						.then(msg)
						.color(GRAY)
						.send(getStaff());
				break;
			}

			case "report":{
				String server = doc.getString("server");
				String reportedPlayer = doc.getString("reported");
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
						.text("\n")
						.then("   Reason: ")
						.color(RED)
						.then(msg.replace(reportedPlayer + " ", ""))
						.color(GRAY)
						.send(getStaff());
				break;
			}

			case "staffswitch":{
				String server = doc.getString("server");
				String sender = doc.getString("sender");
				String msg = doc.getString("message");

				new FancyMessage("(Staff) ")
						.color(BLUE)
						.then("[" + server + "] ")
						.color(DARK_AQUA)
						.command("/staffserver " + server)
						.then(sender + " has " + msg + ".")
						.color(AQUA)
						.send(getStaff());
				break;
			}

		}

	}

	private static Collection<Player> getStaff(){
		return Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("rank.staff")).collect(Collectors.toList());
	}

}
