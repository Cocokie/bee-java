package com.qdcz.platform.beeJava.monitor;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import com.alibaba.fastjson.JSONObject;
import com.qdcz.platform.beeJava.socket.IPClient;
import com.qdcz.platform.beeJava.utils.LogUtils;

public class WrapperMonitor {
	public static void main(String[] args) {
		startWatch("0.0.0.0",5555,"",5556,null);
	}

	public static String getPid() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		String processName = runtimeMXBean.getName();
		if (processName.indexOf('@') != -1)
			return processName.substring(0, processName.indexOf('@'));
		else
			return null;
	}

	public static void startWatch(String clientIP, int clientPort , String beeId, 
			int serverPort,LogUtils logUtil){
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						IPClient client = new IPClient(clientIP,clientPort);
						JSONObject json = new JSONObject();
						json.put("heartBeat", "alive");
						json.put("beeId", beeId);
						json.put("beeRecvPort", serverPort);
						json.put("pid", getPid());
						client.sendMessage(json.toString());
						client.close();
					} catch (IOException e) {
						logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
					} catch (InterruptedException e) {
						logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
					}
				}
			};
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
			service.scheduleAtFixedRate(runnable, 0, 2000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
		}
	}

}
