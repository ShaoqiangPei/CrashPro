package com.crashpro;

import android.app.Application;

import com.crashlibrary.app.CrashConfig;
import com.crashlibrary.local_crash.CrashHandler;
import com.crashlibrary.util.CrashLogUtil;

/**
 * Title:
 * description:
 * autor:pei
 * created on 2021/5/25
 */
public class AppContext extends Application {

    private static AppContext instance;

    public static synchronized AppContext getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化异常捕捉库
        CrashConfig.getInstance().init(this)
                //true:打开log调试模式,默认为false
                .setDebug(true);

        //初始化本地crash捕捉
        CrashHandler.getInstance().init(true, new CrashHandler.OnCrashListener() {
            @Override
            public void uploadInfo(String info) {
                CrashLogUtil.i("=======给服务器上传错误信息====info="+info);
            }

            @Override
            public void exitApp() {
                //关闭所有Activity,退出app
                CrashLogUtil.i("=======关闭所有Activity,退出app=======");
            }
        });


    }

}
