package me.sergivb01.sutils.enums;

public enum PlayerVersion{
	Version_1_7("1.7"),
	Version_1_8("1.8"),
	UNKOWN("Unknown");


	private String toString;

	PlayerVersion(String toString){
		this.toString = toString;
	}

	@Override
	public String toString(){
		return this.toString;
	}
}
