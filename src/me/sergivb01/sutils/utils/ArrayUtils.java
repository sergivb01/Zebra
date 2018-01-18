package me.sergivb01.sutils.utils;

public class ArrayUtils {

	public static String joinString(String[] args, String separator, int index){
		StringBuilder str = new StringBuilder();
		for(int i = index; i < args.length; i++){
			str.append(args[i]).append(separator);
		}
		return String.valueOf(str).substring(0, str.length() - 1);
	}

}
