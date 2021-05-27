package com.crashlibrary.bugly;

import android.content.Context;

import com.crashlibrary.app.CrashConfig;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Title: bugly捕捉异常帮助类
 *
 * description:
 * autor:pei
 * created on 2021/5/23
 */
public class BuglyUtil {

    /***
     * 初始化(在项目Application类onCreate()中调用)
     *
     * @param appId: 注册时申请的APPID
     * @param debug: 输出详细的Bugly SDK的Log,建议在测试阶段建议设置成true，发布时设置为false
     * @param context: context上下文
     */
    public static void init(String appId, boolean debug, Context context){
        CrashReport.initCrashReport(context, appId, debug);
    }

    /**用于测试bug**/
    public static void testCrash(){
        CrashReport.testJavaCrash();
    }

}
