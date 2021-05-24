package com.crashlibrary.app;

import android.app.Application;
import com.crashlibrary.util.CrashLogUtil;

/**
 * Title:crashLibrary库初始化类
 * description:
 * autor:pei
 * created on 2020/1/9
 */
public class CrashConfig {

    private Application mApplication;
    private boolean mDebug=false;//是否开启调试模式(默认不开启)

    private CrashConfig(){}

    private static class Holder {
        private static CrashConfig instance = new CrashConfig();
    }

    public static CrashConfig getInstance() {
        return Holder.instance;
    }

    /**初始化赋值(在项目的自定义Application中初始化)**/
    public CrashConfig init(Application application){
        this.mApplication=application;
        return CrashConfig.this;
    }

    /**
     * 是否打开Log打印
     *
     * @param debug true:打开调试log,  false:关闭调试log
     * @return
     */
    public void setDebug(boolean debug){
        this.mDebug=debug;
        //设置自定义打印开关
        CrashLogUtil.setDebug(mDebug);
    }

    /**获取项目上下文**/
    public Application getApplication() {
        if(mApplication==null){
            throw new NullPointerException("====CrashLibrary需要初始化：CrashConfig.getInstance.init(Application application)===");
        }
        return mApplication;
    }

    /**
     * 获取log打印开关
     * @return true:打开调试log,  false:关闭调试log
     */
    public boolean isDebug(){
        return mDebug;
    }

}
