package com.qdcz.platform.beeJava;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qdcz.platform.beeJava.entity.Plugin;
import com.qdcz.platform.beeJava.utils.LogUtils;
import com.qdcz.platform.beeJava.utils.PluginManager;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Hunan qidian chuangzhi data technology co. LTD
 * User: Guan Xiangqing
 * Date: 2019/1/29 0029
 * Time: 20:01
 */
public class WrapperStartSingleMain {
    private static LogUtils logUtil;

    public static void main(String[] args) {
        String pluginClassInfo = "";
        String logPath = "";
        String beeId = "";
        String pluginPath = "";

        if(args.length == 0) {
            logPath = "/home/qidian/webee-webee1.1/conf/../logs/bees/log";
            beeId = "5b1689848fe61";
//        String pluginPath = "D:\\abc-123\\20190129170002.jar";
//            pluginPath = "/data/ftp/queen/99c1d2f4/20190129104303.jar";
            pluginPath = "/data/ftp/queen/99c1d2f4/20190130094444.jar ";
            pluginClassInfo = "{\"instanceClass\":\"com.qdcz.plugin.BusinessSQCollPlugin\"}";
        } else {
            logPath = args[0];
            beeId = args[1];
            pluginPath = args[2];
            pluginClassInfo = args[3];
        }

        //创建一个父类加载器

        URLClassLoader parent = new URLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader());
        logUtil = new LogUtils(logPath+"/"+beeId+"/system");
        JSONObject pluginInfo = JSON.parseObject(pluginClassInfo);
        pluginInfo.put("jarPath", pluginPath);

        Plugin plugin = new Plugin(pluginInfo);
        logUtil.sysInfoLog("加载插件对象");
        new PluginManager(plugin, parent, logUtil);

        BeeProcedure bee = new BeeProcedure("0.0.0.0", 5003,"59164647a43c2", logUtil,"tmp/data","tmp/logs");
        String str = "{\"taskParam\": \"{\\\"identity_id\\\":\\\"c155a882-7718-4006-997a-37858a9c2198\\\",\\\"kafkaLog\\\":\\\"1\\\",\\\"req_type\\\":\\\"2\\\",\\\"sendMsgBack\\\":\\\"1\\\",\\\"plugin_param\\\":\\\"{\\\\\\\"account_name\\\\\\\":\\\\\\\"guoxin\\\\\\\",\\\\\\\"account_type\\\\\\\":\\\\\\\"khsb\\\\\\\",\\\\\\\"company_name\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"location_flag\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"plugins_number\\\\\\\":0,\\\\\\\"plugins_set\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"request_type\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"sys_type\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"uuid\\\\\\\":\\\\\\\"\\\\\\\"}\\\",\\\"outType\\\":[{\\\"mode\\\":\\\"default\\\",\\\"config\\\":{}}],\\\"target\\\":\\\"深圳市中合银融资担保有限公司\\\"}\", \"taskId\": \"20181207193632564157\"}";
//        String str = "{\"taskParam\": \"{\\\"identity_id\\\":\\\"c155a882-7718-4006-997a-37858a9c2198\\\",\\\"kafkaLog\\\":\\\"1\\\",\\\"req_type\\\":\\\"2\\\",\\\"sendMsgBack\\\":\\\"1\\\",\\\"plugin_param\\\":\\\"{\\\\\\\"account_name\\\\\\\":\\\\\\\"guoxin\\\\\\\",\\\\\\\"account_type\\\\\\\":\\\\\\\"khsb\\\\\\\",\\\\\\\"company_name\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"location_flag\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"plugins_number\\\\\\\":0,\\\\\\\"plugins_set\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"request_type\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"sys_type\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"uuid\\\\\\\":\\\\\\\"\\\\\\\"}\\\",\\\"outType\\\":[{\\\"mode\\\":\\\"default\\\",\\\"config\\\":{}}],\\\"target\\\":\\\"华为技术有限公司\\\"}\", \"taskId\": \"20181207193632564157\"}";
        bee.process(str.getBytes());
    }
}
