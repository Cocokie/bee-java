package com.qdcz.platform.beeJava.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.qdcz.platform.beeJava.RemoteProcedure;
import com.qdcz.platform.beeJava.utils.LogUtils;

public class IPCServer extends Server{
	private ServerSocketChannel serverChannel;
	private InetSocketAddress localAddr;
	private Selector selector;
	private  int BLOCK = 4096;
	private RemoteProcedure procedure;
	private LogUtils logUtil;
	public IPCServer(String serverIP, int serverPort,RemoteProcedure procedure,
			LogUtils logUtil) throws IOException {
		super(serverIP, serverPort);
		//通过open方法来打开一个未绑定的ServerSocketChannel实例
		serverChannel = ServerSocketChannel.open();
		// 创建地址对象
		localAddr = new InetSocketAddress(serverIP, serverPort);
		// 服务器绑定地址
		serverChannel.bind(localAddr);
		// 设置为非阻塞
		serverChannel.configureBlocking(false);
		//打开一个用于检测所有的Channel状态的selector
		selector = Selector.open();
		//将serverChannel注册到指定的selector对象,accept表示一个server socket channel准备号接收新进入的连接称为"接收就绪"
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		this.procedure = procedure;
		this.logUtil = logUtil;
	}

	@Override
	public void serve() {
		while (true) {
			try {
				//调用select，阻塞在这里，直到有注册的channel满足条件
				selector.select();
				// 如果走到这里，有符合条件的channel
				// 可以通过selector.selectedKeys().iterator()拿到符合条件的迭代器
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				// 处理满足条件的keys
				while (keys.hasNext()) {
					// 取出一个key并移除
					SelectionKey key = keys.next();
					//注意每次迭代remove()调用，Selector不会自己从已选择集中移除SelectioKey实例，必须在处理完通道时自己移除
					keys.remove();
					try {
						if (key.isAcceptable()) {
							// 有accept可以返回
							// 取得可以操作的channel
							ServerSocketChannel server = (ServerSocketChannel) key.channel();
							// 调用accept完成三次握手，返回与客户端可以通信的channel
							SocketChannel channel = server.accept();
							// 将该channel置非阻塞
							channel.configureBlocking(false);
							// 注册进selector，当可读或可写时将得到通知，select返回
							channel.register(selector, SelectionKey.OP_READ);
						} else if (key.isReadable()) {
							// 有channel可读,取出可读的channel
							SocketChannel channel = (SocketChannel) key.channel();
							// 创建读取缓冲区,一次读取4096字节
							ByteBuffer buffer = ByteBuffer.allocate(BLOCK);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							int readBytes;
							while ((readBytes = channel.read(buffer)) > -1){ //确保读完
								buffer.flip();
								baos.write(buffer.array(), 0, readBytes);
								buffer.clear(); //每次清空 对应上面flip()
							}
							baos.flush();
							procedure.process(baos.toByteArray());
							key.cancel();
							try {
								key.channel().close();
							} catch (IOException e) {
								logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
							}
						} 
					} catch (IOException e) {
						logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
						// 当客户端Socket关闭时，会走到这里，清理资源
						key.cancel();
						try {
							key.channel().close();
						} catch (IOException e1) {
							logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
						}
					}
				}
			} catch (IOException e) {
				logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
			}
		}
	}

	public static void main(String[] args) {
		try {
			IPCServer server = new IPCServer("0.0.0.0", 5555,null,null);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
