package me.sergivb01.sutils.database.redis.pubsub;

import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.payload.PayloadParser;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Subscriber{
	private ServerUtils instance;
	@Getter
	private JedisPubSub jedisPubSub;
	private Jedis jedis;

	public Subscriber(ServerUtils instance){
		this.instance = instance;
		this.jedis = new Jedis(ConfigUtils.REDIS_HOST, ConfigUtils.REDIS_PORT, ConfigUtils.REDIS_TIMEOUT);
		if(ConfigUtils.REDIS_AUTH_ENABLED){
			this.jedis.auth(ConfigUtils.REDIS_AUTH_PASSWORD);
		}
		this.init();
	}

	private void init(){
		jedisPubSub = this.get();
		new Thread(() -> jedis.subscribe(jedisPubSub, ConfigUtils.REDIS_CHANNEL)).start(); //Create subscriber in new Thread to avoid blocking main
	}

	private JedisPubSub get(){
		return new JedisPubSub(){
			@Override
			public void onMessage(final String channel, final String message){
				final String[] args = message.split(";");
				final String command = args[0].toLowerCase();
				//TODO: Implement encryption (?)

				if(command.equalsIgnoreCase("payload")){ //Only parse payloads - others not
					PayloadParser.parse(args[1]);
					return;
				}

				instance.getLogger().warning("Recived unknown redis message! " + Arrays.toString(args));
			}

			public void onPMessage(final String s, final String s1, final String s2){
			}

			public void onSubscribe(final String s, final int i){
			}

			public void onUnsubscribe(final String s, final int i){
			}

			public void onPUnsubscribe(final String s, final int i){
			}

			public void onPSubscribe(final String s, final int i){
			}
		};

	}

	private Collection<Player> getStaff(){
		return Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("rank.staff")).collect(Collectors.toList());
	}

}