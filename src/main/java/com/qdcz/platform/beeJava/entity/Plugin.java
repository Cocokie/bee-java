package com.qdcz.platform.beeJava.entity;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import com.qdcz.spider.plugin.IPlugin;
import com.qdcz.spider.plugin.PluginClassLoader;

/**
 * 插件实体类
 * @author Administrator
 *
 */
public class Plugin {
	//插件id
	private String pluginId;
	//插件名称
	private String pluignName;
	//插件存储路径
	private String path;
	//故障通知邮件列表
	private List<String> authorEmaiList;
	//插件加载参数
	private String loadParam;
	//插件的实例类
	private String instanceClass;
	//插件类型
	private String type;
	//插件的并行度
	private int parallelism;
	//插件jar包存放路径
	private String JarFilePath;
	//插件依赖的jar包路径
	private String pluginLibPath;
	//插件依赖的pom文件路径
	private String pomPath;
	//插件对象
	private IPlugin pluginObj;
	//加载该插件的类加载器
	private PluginClassLoader pluginClassLoader;
	
	public String getPluginId() {
		return pluginId;
	}
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
	public String getPluignName() {
		return pluignName;
	}
	public void setPluignName(String pluignName) {
		this.pluignName = pluignName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<String> getAuthorEmaiList() {
		return authorEmaiList;
	}
	public void setAuthorEmaiList(List<String> authorEmaiList) {
		this.authorEmaiList = authorEmaiList;
	}
	public String getLoadParam() {
		return loadParam;
	}
	public void setLoadParam(String loadParam) {
		this.loadParam = loadParam;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getParallelism() {
		return parallelism;
	}
	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}
	
	public String getInstanceClass() {
		return instanceClass;
	}
	public void setInstanceClass(String instanceClass) {
		this.instanceClass = instanceClass;
	}
	public String getJarFilePath() {
		return JarFilePath;
	}
	public void setJarFilePath(String jarFilePath) {
		JarFilePath = jarFilePath;
	}
	public String getPluginLibPath() {
		return pluginLibPath;
	}
	public void setPluginLibPath(String pluginLibPath) {
		this.pluginLibPath = pluginLibPath;
	}
	public String getPomPath() {
		return pomPath;
	}
	public void setPomPath(String pomPath) {
		this.pomPath = pomPath;
	}
	
	public IPlugin getPluginObj() {
		return pluginObj;
	}
	public void setPluginObj(IPlugin pluginObj) {
		this.pluginObj = pluginObj;
	}
	public PluginClassLoader getPluginClassLoader() {
		return pluginClassLoader;
	}
	public void setPluginClassLoader(PluginClassLoader pluginClassLoader) {
		this.pluginClassLoader = pluginClassLoader;
	}
	
	public Plugin(JSONObject pluginInfo) {
		this.instanceClass = pluginInfo.getString("instanceClass");
		this.JarFilePath = pluginInfo.getString("jarPath");
		//this.pomPath = pluginInfo.getString("pomPath");
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((JarFilePath == null) ? 0 : JarFilePath.hashCode());
		result = prime * result + ((authorEmaiList == null) ? 0 : authorEmaiList.hashCode());
		result = prime * result + ((instanceClass == null) ? 0 : instanceClass.hashCode());
		result = prime * result + ((loadParam == null) ? 0 : loadParam.hashCode());
		result = prime * result + parallelism;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((pluginClassLoader == null) ? 0 : pluginClassLoader.hashCode());
		result = prime * result + ((pluginId == null) ? 0 : pluginId.hashCode());
		result = prime * result + ((pluginLibPath == null) ? 0 : pluginLibPath.hashCode());
		result = prime * result + ((pluginObj == null) ? 0 : pluginObj.hashCode());
		result = prime * result + ((pluignName == null) ? 0 : pluignName.hashCode());
		result = prime * result + ((pomPath == null) ? 0 : pomPath.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plugin other = (Plugin) obj;
		if (JarFilePath == null) {
			if (other.JarFilePath != null)
				return false;
		} else if (!JarFilePath.equals(other.JarFilePath))
			return false;
		if (authorEmaiList == null) {
			if (other.authorEmaiList != null)
				return false;
		} else if (!authorEmaiList.equals(other.authorEmaiList))
			return false;
		if (instanceClass == null) {
			if (other.instanceClass != null)
				return false;
		} else if (!instanceClass.equals(other.instanceClass))
			return false;
		if (loadParam == null) {
			if (other.loadParam != null)
				return false;
		} else if (!loadParam.equals(other.loadParam))
			return false;
		if (parallelism != other.parallelism)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (pluginClassLoader == null) {
			if (other.pluginClassLoader != null)
				return false;
		} else if (!pluginClassLoader.equals(other.pluginClassLoader))
			return false;
		if (pluginId == null) {
			if (other.pluginId != null)
				return false;
		} else if (!pluginId.equals(other.pluginId))
			return false;
		if (pluginLibPath == null) {
			if (other.pluginLibPath != null)
				return false;
		} else if (!pluginLibPath.equals(other.pluginLibPath))
			return false;
		if (pluginObj == null) {
			if (other.pluginObj != null)
				return false;
		} else if (!pluginObj.equals(other.pluginObj))
			return false;
		if (pluignName == null) {
			if (other.pluignName != null)
				return false;
		} else if (!pluignName.equals(other.pluignName))
			return false;
		if (pomPath == null) {
			if (other.pomPath != null)
				return false;
		} else if (!pomPath.equals(other.pomPath))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
