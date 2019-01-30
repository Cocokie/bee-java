package com.qdcz.platform.beeJava.utils;

import com.qdcz.platform.beeJava.entity.Plugin;
import com.qdcz.spider.plugin.IPlugin;
import com.qdcz.spider.plugin.PluginClassLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginManager {
	private  URLClassLoader parent;
	private  static Plugin plugin;
	private  LogUtils logUtil;
	public PluginManager(Plugin plugin,URLClassLoader parent,LogUtils logUtil){
		this.parent = parent;
		this.plugin = plugin;
		this.logUtil = logUtil;
		loadPlugin();
	}
	
	public static  Plugin getPlugin() {
		return plugin;
	}

	public  void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public  void loadPlugin(){
		try {
			logUtil.sysInfoLog("begin loading  plugin ,jar path " + plugin.getJarFilePath());
			
			// 设置lib文件路径

//			String path = plugin.getPomPath();
//			int lastSplit = path.lastIndexOf('/');
//			String libpath = path.substring(0, lastSplit);
//			libpath = libpath+"/lib/";
//			plugin.setPluginLibPath(libpath);
			
//			logUtil.sysInfoLog("begin download lib,libpath is :"+libpath);
//			File libFile = new File(plugin.getPluginLibPath());
//			File[] libDir = libFile.listFiles();
//			if (libDir != null && libDir.length > 0) {
//				logUtil.sysErrorLog("lib dir is not null or already download");
//			} else {
//				getPluginLibPackage(plugin);
//				logUtil.sysInfoLog("finish download lib");
//			}
//			logUtil.sysInfoLog("plugin.getJarFilepath() is   " + plugin.getJarFilePath());

			URL[] urls = new URL[] { new File(plugin.getJarFilePath()).toURI().toURL() };
			PluginClassLoader pluginClassLoader = new PluginClassLoader(urls, parent);

//			logUtil.sysInfoLog("begin load classpath");
//			loadJarUtil.loadClassPath(plugin.getPluginLibPath(), pluginClassLoader);
			
			logUtil.sysInfoLog("begin load plugin instance class " + plugin.getInstanceClass());
			Class<?> clazz = pluginClassLoader.loadClass(plugin.getInstanceClass());
			logUtil.sysInfoLog("load plugin instance class info" + clazz);
			IPlugin pluginObj = (IPlugin) clazz.newInstance();
			logUtil.sysInfoLog("load plugin pluginObj info" + pluginObj);
			plugin.setPluginObj(pluginObj);
			plugin.setPluginClassLoader(pluginClassLoader);
			
			logUtil.sysInfoLog("finish load plugin");
		} catch (MalformedURLException e) {
			logUtil.sysErrorLog(e.toString());
		} catch (ClassNotFoundException e) {
			logUtil.sysErrorLog(e.toString());
		} catch (InstantiationException e) {
			logUtil.sysErrorLog(e.toString());
		} catch (IllegalAccessException e) {
			logUtil.sysErrorLog(e.toString());
		}
	}
	
	/**
	 * <p> createOneWorkProcess<br> Description: 执行shell下载依赖</p> @param @author
	 * bijl @date 2018年1月7日 下午12:26:19 @version V1.4 @return void 返回类型 @throws
	 */
	public void getPluginLibPackage(Plugin plugin) {
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			logUtil.sysInfoLog(plugin.getPluginId() + "  pom path is " + plugin.getPomPath());
			String shellCmd = "mvn -f " + plugin.getPomPath()
					+ " dependency:copy-dependencies -DoutputDirectory=lib   -DincludeScope=compile";
			logUtil.sysInfoLog("shell cmd :"+shellCmd);
			String[] cmd = new String[] { "/bin/sh", "-c", shellCmd };
			Process ps = Runtime.getRuntime().exec(cmd);
			int status = ps.waitFor();
			
			bufrIn = new BufferedReader(new InputStreamReader(ps.getInputStream(), "UTF-8"));
			bufrError = new BufferedReader(new InputStreamReader(ps.getErrorStream(), "UTF-8"));
			
			String bufrIn_result = bufrIn.readLine();
			String bufrError_result = bufrError.readLine();
			
			logUtil.sysInfoLog("call shell's command and the return buffer in result's is:"+bufrIn_result);
			logUtil.sysErrorLog("call shell's command and the return buffer error result's is:"+bufrError_result);
			
			if (status !=0 ){
				logUtil.sysErrorLog("Failed to call shell's command and the return status's is:"+status);
			}
			
		} catch (Exception e) {
			logUtil.sysErrorLog(e.toString());
		}
	}
}
