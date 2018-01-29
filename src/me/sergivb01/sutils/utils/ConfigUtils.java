package me.sergivb01.sutils.utils;

import me.sergivb01.sutils.ServerUtils;

public class ConfigUtils {
	public static boolean DEBUG = false;

	public static String REDIS_HOST = "localhost";
	public static int REDIS_PORT = 6379;
	public static int REDIS_TIMEOUT = 2000;
	public static String REDIS_CHANNEL = "serverutils";
	public static boolean REDIS_AUTH_ENABLED = false;
	public static String REDIS_AUTH_PASSWORD = "";

	public static String MONGO_HOST = "localhost";
	public static int MONGO_PORT = 6379;
	public static String MONGO_USERNAME = "";
	public static String MONGO_DATABASE = "serverutils";
	public static boolean MONGO_AUTH_ENABLED = false;
	public static String MONGO_AUTH_PASSWORD = "";


	public static String SERVER_NAME;


	public static void updateConfig(ServerUtils instance){
		REDIS_HOST = instance.getConfig().getString("database.redis.host");
		REDIS_PORT = instance.getConfig().getInt("database.redis.port");
		REDIS_TIMEOUT = instance.getConfig().getInt("database.redis.timeout");
		REDIS_CHANNEL = instance.getConfig().getString("database.redis.channel");
		REDIS_AUTH_ENABLED = instance.getConfig().getBoolean("database.redis.auth.enabled");
		REDIS_AUTH_PASSWORD = instance.getConfig().getString("database.redis.auth.password");


		MONGO_HOST = instance.getConfig().getString("database.mongo.host");
		MONGO_PORT = instance.getConfig().getInt("database.mongo.port");
		MONGO_USERNAME = instance.getConfig().getString("database.mongo.username");
		MONGO_DATABASE = instance.getConfig().getString("database.mongo.database");
		MONGO_AUTH_ENABLED = instance.getConfig().getBoolean("database.mongo.auth.enabled");
		MONGO_AUTH_PASSWORD = instance.getConfig().getString("database.mongo.auth.password");


		SERVER_NAME = instance.getConfig().getString("server.name");


	}


}
