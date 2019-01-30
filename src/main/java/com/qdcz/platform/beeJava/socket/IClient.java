package com.qdcz.platform.beeJava.socket;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface IClient {
	
	boolean sendMessage(String message) throws IOException, InterruptedException;
	
	void doWrite(SocketChannel sc, String data) throws IOException;
	
	void close() throws IOException;
}
