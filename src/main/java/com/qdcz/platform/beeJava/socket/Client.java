package com.qdcz.platform.beeJava.socket;

public abstract class Client implements IClient{
	private String targetHost;
	private int targetPort;
	
	public Client(String targetHost, int targetPort) {
		setTargetHost(targetHost);
		setTargetPort(targetPort);
	}

	public String getTargetHost() {
		return targetHost;
	}

	public void setTargetHost(String targetHost) {
		this.targetHost = targetHost;
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}

	
}
