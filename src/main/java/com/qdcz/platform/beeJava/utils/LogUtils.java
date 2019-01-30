package com.qdcz.platform.beeJava.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Hunan qidian chuangzhi data technology co. LTD
 * User: Guan Xiangqing
 * Date: 2018/12/18 0018
 * Time: 9:48
 *  功能说明：该日志方法给新版本CI平台用（采集平台保留使用原来的日志系统-故新增这个类-20181218）
 */
public class LogUtils {
    private InetAddress netAddress = null;
    private  String host = "unknown host";
    private  String ip = "unknown ip";
    private  String logHome;
    private  Logger logger = null;
    private  Logger sysLogger = null;
    private  LoggerContext ctx = null;

    public LogUtils(String logHome){
        this.logHome = logHome;
        initLogConfig();
        logger = LogManager.getLogger("USERLOG");
        sysLogger = LogManager.getLogger("SYSTEMLOG");
        //为false时，返回多个LoggerContext对象，   true：返回唯一的单例LoggerContext
        ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
    public  void initLogConfig() {
        try {
            netAddress = InetAddress.getLocalHost();
            host = netAddress.getHostName();
            ip = netAddress.getHostAddress();
            System.setProperty("host", host);
            System.setProperty("ip", ip);
            System.setProperty("logHome", logHome);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public  void userDebugLog(String message){
        logger.debug(getInfo()+" - "+message);
    }
    public  void userErrorLog(String message){
        logger.error(getInfo()+" - "+message);
    }
    public  void userInfoLog(String message){
        logger.info(getInfo()+" - "+message);
    }
    public  void sysDebugLog(String message){
        sysLogger.debug(getInfo()+" - "+message);
    }
    public  void sysInfoLog(String message){
        sysLogger.info(getInfo()+" - "+message);
    }
    public  void sysErrorLog(String message){
        sysLogger.error(getInfo()+" - "+message);
    }
    private String getInfo(){
        StackTraceElement lvStacks=Thread.currentThread().getStackTrace()[3];
        return "[main-"+ lvStacks.getClassName()
                + "." + lvStacks.getMethodName() +"("+lvStacks.getFileName()+ ":" +lvStacks.getLineNumber()+")";
    }
}
