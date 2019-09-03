package com.qdcz.platform.beeJava;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qdcz.platform.beeJava.utils.LogUtils;
import com.qdcz.spider.utils.Function;

public class WrapperStartMain {
	private static LogUtils logUtil;
	//0.0.0.0 5556 192.168.2.110 5555

	/**
	 *  ['/usr/bin/java', '-jar', '/home/qidian/webee-webee1.1/honeycomb/../bee/beeJava/bee.jar',
	 *  '127.0.0.1', '55266', '127.0.0.1', '5003', '5b1689848fe61',
	 *  '/home/qidian/webee-webee1.1/conf/../logs/bees/log',
	 *  '/home/qidian/webee-webee1.1/conf/../logs/bees/data',
	 *  '/data/ftp/queen/99c1d2f4/20190129104303.jar',
	 *  '{"instanceClass":"com.qdcz.plugin.BusinessSQCollPlugin"}']1
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			if (args == null || args.length == 0) {
				throw new RuntimeException("请输入参数");
			}
			String serverIP = args[0];
			int serverPort = Integer.parseInt(args[1]);
			String clientIP = args[2];
			int clientPort = Integer.parseInt(args[3]);
			String beeId = args[4];
			String logPath = args[5];
			String dataPath = args[6];
			String pluginPath = args[7];
			//String pomPath = args[8];
			String pluginInfo = args[8];
			JSONObject plugin = JSON.parseObject(pluginInfo);
			plugin.put("jarPath", pluginPath);
			//plugin.put("pomPath",pomPath);
			logUtil = new LogUtils(logPath+"/"+beeId+"/system");
			logUtil.sysInfoLog("serverIP:"+serverIP+" serverPort:"+serverPort+" clientIP:"+clientIP
					+" clientPort:"+clientPort+" beeId:"+beeId+" logPath:"+logPath +" dataPath:"+dataPath);
			logUtil.sysInfoLog("pluginPath:"+pluginPath+" pluginInfo:"+pluginInfo);
			Wrapper wrapper = new Wrapper(serverIP,serverPort,clientIP,clientPort,
					beeId,logPath,dataPath,plugin,logUtil);
			wrapper.run();
		} catch (IOException e) {
			logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
		}
	}
}