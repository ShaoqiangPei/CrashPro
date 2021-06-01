package com.crashlibrary.bugly;

import android.content.Context;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
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
     * @param context: context上下文
     * @param appId: 注册时申请的APPID
     * @param debug: 输出详细的Bugly SDK的Log,建议在测试阶段建议设置成true，发布时设置为false
     */
    public static void init(String appId, boolean debug, Context context){
        Bugly.init(context, appId, debug);
    }

    /**用于测试bug**/
    public static void testCrash(){
        CrashReport.testJavaCrash();
    }

    /***
     * 手动检测新版本
     *
     * @param isManual：用户手动点击检查,非用户点击操作请传false
     * @param isSilence：是否显示弹窗等交互,[true:没有弹窗和toast] [false:有弹窗或toast]
     */
    public static void checkVersion(boolean isManual,boolean isSilence){
        Beta.checkUpgrade(isManual,isSilence);
    }

}
