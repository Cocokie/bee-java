package com.qdcz.platform.beeJava;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import com.alibaba.fastjson.JSONObject;
import com.qdcz.platform.beeJava.entity.Plugin;
import com.qdcz.platform.beeJava.monitor.WrapperMonitor;
import com.qdcz.platform.beeJava.socket.IPCServer;
import com.qdcz.platform.beeJava.utils.LogUtils;
import com.qdcz.platform.beeJava.utils.PluginManager;

public class Wrapper {
	private  String serverIP;
	private  int serverPort;
	private  String clientIP;
	private  int clientPort;
	private String beeId;
	private String dataPath;
	private String logPath;
	private IPCServer wrapperServer;
	private LogUtils logUtil = null;
	public Wrapper(String serverIP, int serverPort, String clientIP, int clientPort,
			String beeId,String logPath,String dataPath,JSONObject pluginInfo,
			LogUtils logUtil) throws IOException {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.clientIP = clientIP;
		this.clientPort = clientPort;
		this.beeId = beeId;
		this.dataPath = dataPath;
		this.logPath = logPath;
		this.logUtil = logUtil;
		//生成日志工具对象
		logUtil.sysInfoLog("开启一个客户端定时向honeycomb发送心跳");
		//加载插件
		//创建一个父类加载器
		URLClassLoader parent = new URLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader());
		//插件管理器去加载插件到内存
		Plugin plugin = new Plugin(pluginInfo);
		logUtil.sysInfoLog("加载插件对象");
		new PluginManager(plugin, parent, logUtil);
		//开启一个客户端定时向honeycomb发送心跳
		logUtil.sysInfoLog("开启一个客户端定时向honeycomb发送心跳");
		WrapperMonitor.startWatch(clientIP,clientPort,beeId,serverPort,logUtil);
		
	}
	public void run(){
		try {
			logUtil.sysInfoLog("开启一个socket服务端，接收honeycomb任务信号");
			//1.开启一个socket服务端，接收honeycomb任务信号,根据任务的参数去开启一个线程去处理，是否返回数据
			RemoteProcedure procedure = new BeeProcedure(clientIP,clientPort,beeId,logUtil,dataPath,logPath);
			wrapperServer = new IPCServer(serverIP,serverPort,procedure,logUtil);
			wrapperServer.serve();
		} catch (IOException e) {
			logUtil.sysErrorLog("开启一个socket服务端失败");
			logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
		}
	}
	
}
