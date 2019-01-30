package com.qdcz.platform.beeJava.socket;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class IPClient extends Client{
	//selector 管理多个通道
	private Selector selector = null;
	private SocketChannel channel;
	
	public IPClient(String targetHost, int targetPort) throws IOException {
		super(targetHost, targetPort);
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
		//将channel注册到selector上
		channel.register(selector, SelectionKey.OP_CONNECT);
		channel.connect(new InetSocketAddress(targetHost, targetPort));
		selector.select();  
	}

	public boolean sendMessage(String message) throws IOException, InterruptedException {
		Set<SelectionKey> selectionKeys;  
		Iterator<SelectionKey> iterator;  
		SelectionKey selectionKey;  
		SocketChannel client = null;  
		while (true) {  
			//选择一组键，其相应的通道已为 I/O 操作准备就绪。  
			//此方法执行处于阻塞模式的选择操作。  
//			selector.select();  
			//返回此选择器的已选择键集。  
			selectionKeys = selector.selectedKeys();  
			iterator = selectionKeys.iterator();  
			while (iterator.hasNext()) { 
				selectionKey = iterator.next();  
				if (selectionKey.isConnectable()) { 
					client = (SocketChannel) selectionKey.channel();  
					// 判断此通道上是否正在进行连接操作。  
					// 完成套接字通道的连接过程。  
					if (client.isConnectionPending()) {  
						client.finishConnect(); 
					}  
					doWrite(client,message);
					return true;
				}else{
					//链接失败，进程推出
                    System.exit(1);
				} 
			}  
		}
	}

	public void doWrite(SocketChannel sc, String data) throws IOException {
		byte[] req = data.getBytes();
		ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
		byteBuffer.put(req);
		byteBuffer.flip();
		sc.write(byteBuffer);
		if (!byteBuffer.hasRemaining()) {
			System.out.println("Send client successed");
		}
	}

	public void close() throws IOException {
		channel.close();
		selector.close();
	}

	public static void main(String[] args) {
		try {
//			IPClient client = new IPClient("192.168.2.110", 5555);
			IPClient client = new IPClient("0.0.0.0", 5555);
			while(true){
				System.out.println("开始发送.....");
				JSONObject object = new JSONObject ();
				object .put("taskId", "111111");  //任务id
				object .put("pluginId", "222222");  //任务进度
//				object .put("taskRate", 100);  //任务进度
				client.sendMessage(object.toString());
				System.out.println("休息三秒继续发送.....");
				Thread.sleep(60*60*1000);
			}
//			client.close();
		} catch (Exception e) {
			System.out.println("can not listen to:" + e);// 出错，打印出错信息
		}
	}

}
