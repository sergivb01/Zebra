package me.sergivb01.sutils.enums;

public enum ServerStatus {
	ONLINE("Online"),
	OFFLINE("Offline"),
	WHITELISTED("Whitelisted");

	private String toString;

	ServerStatus(String toString) {
		this.toString = toString;
	}

	@Override
	public String toString() {
		return this.toString;
	}
}
