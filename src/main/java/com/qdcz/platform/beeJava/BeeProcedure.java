package com.qdcz.platform.beeJava;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qdcz.platform.beeJava.socket.IPCServer;

import com.qdcz.platform.beeJava.entity.Plugin;
import com.qdcz.platform.beeJava.socket.IPClient;
import com.qdcz.platform.beeJava.utils.LogUtils;
import com.qdcz.platform.beeJava.utils.PluginManager;
import com.qdcz.spider.email.EmailMonitor;
import com.qdcz.spider.plugin.AResult;
import com.qdcz.spider.utils.Function;

public class BeeProcedure implements RemoteProcedure{
	private LogUtils logUtil;
	private IPClient client = null;
	private String dataPath;
	private String logPath;
	private String beeId;
	private String clientIP;
	private int clientPort;
	public BeeProcedure(String clientIP, int clientPort, String beeId, LogUtils logUtil, 
			String dataPath, String logPath) {
		this.clientIP = clientIP;
		this.clientPort = clientPort;
		this.dataPath = dataPath;
		this.logUtil = logUtil;
		this.beeId = beeId;
		this.logPath = logPath;
	}
	@Override
	public void process(byte[] bytes) {
		AResult pilotResult = new AResult();
		String taskId = "";
		try {
			logUtil.sysInfoLog("接收一次任务请求");
			if (bytes == null) {
				logUtil.sysErrorLog("bytes is null...");
				return;
			}
			String request = new String(bytes, Charset.forName("UTF-8"));
			logUtil.sysInfoLog("request: " + request);
			if (!request.startsWith("{") || !request.endsWith("}")) {
				logUtil.sysErrorLog("request is not a JSONObject...");
			}
			JSONObject requestObj = JSON.parseObject(request);
			if(!requestObj.containsKey("taskId")){
				logUtil.sysErrorLog("please input taskId");
			}else{
				taskId = requestObj.getString("taskId");
				
				sendTaskRateMessage(beeId,taskId,0,"开始运行插件");
				// TODO: wrapper不解析taskParam，直接传递字符串，由plugin自行解析。
				requestObj.put("taskParam", requestObj.getString("taskParam"));
				pilotResult.requsetJson = requestObj;
				
				Plugin plugin = PluginManager.getPlugin();
				String userLogPath = logPath + "/" + beeId + "/" + taskId;
				pilotResult.setUserLogPath(userLogPath);
				pilotResult.setTaskId(taskId);
				sendTaskRateMessage(beeId,taskId,10,"插件开始进行初始化方法");
				logUtil.sysInfoLog("taskId is " + taskId);
				logUtil.sysInfoLog("userLogPath is " + userLogPath);
				logUtil.sysInfoLog("beeId is " + beeId);
				logUtil.sysInfoLog("plugin path is " + plugin.getJarFilePath());

				loadPlugin(plugin, pilotResult, logUtil);
//				EmailMonitor.sendEmail("123","123");

				logUtil.sysInfoLog("开始进行结果写入操作");
				sendTaskRateMessage(beeId,taskId,95,"插件数据进行写入本地文件");
				byte [] data = pilotResult.getData();
				String path = dataPath + "/" + beeId + "/" + taskId + ".txt";
				try {
					File file = new File(path);
					if(!file.getParentFile().exists()) {  
						logUtil.sysInfoLog("目标文件所在目录"+dataPath+"/"+beeId+"不存在，准备创建它！"); 
				        if(!file.getParentFile().mkdirs()) {  
				        	logUtil.sysErrorLog("创建目标文件所在目录失败！");  
				        }  
				    }
					Function.printFile(data, path);
				} catch (Exception e) {
					logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
				}
				logUtil.sysInfoLog("结果写入操作完毕");
				sendTaskRateMessage(beeId,taskId,99,"插件数据写入本地文件完毕");
				sendTaskRateMessage(beeId,taskId,100,"插件运行完毕");
				logUtil.sysInfoLog("任务请求处理完毕");
			}
		} catch (Exception e) {
			sendTaskRateMessage(beeId, taskId,-1,"插件运行异常完毕");
			logUtil.sysErrorLog("任务请求异常");
			logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
		}
	}

	public void loadPlugin(Plugin plugin, AResult result, LogUtils logUtils) {
		try {
			logUtils.sysInfoLog("实例化任务 "+result.getTaskId()+" 的插件");
			if (plugin == null) {
				logUtils.sysErrorLog("Error:Can not find the configuration information of the url in configuration file!");
				return;
			}
			if (plugin.getPluginObj() == null) {
				logUtils.sysErrorLog("实例化插件失败!");
			}
			plugin.getPluginObj().init(result);
			sendTaskRateMessage(beeId,result.getTaskId(),25,"插件初始化完毕");
			sendTaskRateMessage(beeId,result.getTaskId(),30,"插件开始执行业务逻辑代码");
			logUtils.sysInfoLog("============调用插件的getResult方法==============");
			plugin.getPluginObj().getResult(result);
			sendTaskRateMessage(beeId,result.getTaskId(),65,"插件业务逻辑处理完毕");
			sendTaskRateMessage(beeId,result.getTaskId(),70,"插件开始对数据进行处理");
			plugin.getPluginObj().dealWithResult(result);
			sendTaskRateMessage(beeId,result.getTaskId(),80,"插件数据处理完毕");
			sendTaskRateMessage(beeId,result.getTaskId(),85,"插件开始进行清理资源");

		} catch (Exception e) {
//			EmailMonitor.sendEmail("webee","error");
			logUtils.sysErrorLog(e.getMessage() + "-----" + e.getCause());
		}finally {
			if (plugin!=null && plugin.getPluginObj()!=null){
				plugin.getPluginObj().finish(result);
			}
			sendTaskRateMessage(beeId,result.getTaskId(),90,"插件清理资源完毕");
			logUtils.sysInfoLog("调用 "+result.getTaskId()+"插件完毕");
		}
	}

	public void sendTaskRateMessage(String beeId, String taskId, int rate, String message){
		try {
			client = new IPClient(clientIP, clientPort);
			JSONObject object = new JSONObject();
			object.put("beeId", beeId);
			object.put("taskId", taskId);
			object.put("taskRate", rate);
			object.put("taskMsg", message);
			client.sendMessage(object.toString());
			client.close();
		} catch (IOException e) {
			logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
		} catch (InterruptedException e) {
			logUtil.sysErrorLog(e.getMessage() + "---" + e.getCause());
		}
	}

	public static void main(String[] args) throws Exception {
		/*LogUtils logUtil = new LogUtils("tmp/system");
		BeeProcedure bee = new BeeProcedure("0.0.0.0", 5003,"59164647a43c2", logUtil,"tmp/data","tmp/logs");

//		IPCServer wrapperServer = new IPCServer("0.0.0.0", 46667, bee, logUtil);
		String str = "{\"taskParam\": \"{\\\"identity_id\\\":\\\"c155a882-7718-4006-997a-37858a9c2198\\\",\\\"kafkaLog\\\":\\\"1\\\",\\\"req_type\\\":\\\"2\\\",\\\"sendMsgBack\\\":\\\"1\\\",\\\"plugin_param\\\":\\\"{\\\\\\\"account_name\\\\\\\":\\\\\\\"guoxin\\\\\\\",\\\\\\\"account_type\\\\\\\":\\\\\\\"khsb\\\\\\\",\\\\\\\"company_name\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"location_flag\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"plugins_number\\\\\\\":0,\\\\\\\"plugins_set\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"request_type\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"sys_type\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"uuid\\\\\\\":\\\\\\\"\\\\\\\"}\\\",\\\"outType\\\":[{\\\"mode\\\":\\\"default\\\",\\\"config\\\":{}}],\\\"target\\\":\\\"合肥轩安建筑工程有限公司\\\"}\", \"taskId\": \"20181207193632564157\"}";
//		str = str.replace("\\","");
		bee.process(str.getBytes());*/

	}


	public void backup() throws Exception {
		LogUtils logUtil = new LogUtils("tmp/system");
		BeeProcedure bee = new BeeProcedure("0.0.0.0", 5003,"59164647a43c2", logUtil,"tmp/data","tmp/logs");
		IPCServer wrapperServer = new IPCServer("0.0.0.0", 46667, bee, logUtil);
		wrapperServer.serve();
//		bee.sendTaskRateMessage("66666","2222",100,"运行完毕");
	}
}
