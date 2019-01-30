package com.qdcz.platform.beeJava.socket;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Server implements IServer{
	private String serverIP;
	private int serverPort;
	private static final char REQUEST_END_CHAR = '#';
	public Server(@JsonProperty("serverIP")String serverIP,
			@JsonProperty("serverPort")int serverPort) {
		setServerIP(serverIP);
		setServerPort(serverPort);
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
}
