package me.sergivb01.sutils.enums;

public enum PunishmentType {
	BAN("ban"),
	IPBAN("ipban"),
	TEMP_BAN("tempban"),
	MUTE("mute"),
	TEMP_MUTE("tempmute"),
	KICK("kick");

	private String toString;

	PunishmentType(String toString) {
		this.toString = toString;
	}

	@Override
	public String toString() {
		return this.toString;
	}
}
