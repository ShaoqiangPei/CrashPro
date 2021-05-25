package com.crashlibrary.local_crash;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.crashlibrary.R;
import com.crashlibrary.app.CrashConfig;
import com.crashlibrary.util.CrashLogUtil;
import com.crashlibrary.util.ScreenUtil;

/**
 * Title: 崩溃弹窗
 *
 * description: 需要权限
 *              <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
 *
 * autor:pei
 * created on 2021/5/20
 */
public class CrashPopUtil {

    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static boolean isShown = false;//是否显示pop

    //是否打开捕捉异常模式
    public static boolean mCatch=false;

    /**弹出pop**/
    private static void showPop(Object message,View.OnClickListener listener){
        if(isShown){
            CrashLogUtil.i("======已经弹出显示了======");
            return;
        }
        isShown=true;
        // 获取WindowManager
        mWindowManager = (WindowManager) CrashConfig.getInstance().getApplication().getSystemService(Context.WINDOW_SERVICE);
        mView = setUpView(message,listener);
        WindowManager.LayoutParams params=new WindowManager.LayoutParams();
        // 类型
        params.type = Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY : WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY; //兼容了8.0
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = ScreenUtil.getWidth()*3/4;
        params.height =  ScreenUtil.getHeight()*3/4;
        params.gravity = Gravity.CENTER;
        mWindowManager.addView(mView, params);
    }

    private static View setUpView(Object obj,View.OnClickListener listener) {
        View view = LayoutInflater.from(CrashConfig.getInstance().getApplication()).inflate(R.layout.crash_pop, null);
        TextView mTvContent = view.findViewById(R.id.mTvContent);
        Button mBtnExit = view.findViewById(R.id.mBtnExit);
        //显示内容
        if(obj!=null){
            if(obj instanceof SpannableString){
                mTvContent.setText((SpannableString) obj);
            }else {
                mTvContent.setText(obj.toString());
            }
        }else{
            mTvContent.setText("");
        }
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        //点击事件
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrashLogUtil.i("==========隐藏弹窗==========");
                hidePopupWindow();
                listener.onClick(v);
            }
        });
        return view;
    }

    /**关闭pop**/
    private static void hidePopupWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            isShown = false;
        }
    }

    /**弹出错误pop**/
    public static void canShow(Object message,View.OnClickListener listener) {
        if (mCatch) {
            if (hasPermission()) {
                showPop(message, listener);
            } else {
                //跳转设置悬浮窗界面
                goSetPop();
            }
        }
    }

    /**是否已开启悬浮窗权限**/
    public static boolean hasPermission(){
        if(mCatch) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(CrashConfig.getInstance().getApplication())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                //SDK在23以下，不用管
            }
        }
        return true;
    }

    /**跳转设置悬浮窗界面**/
    public static void goSetPop() {
        if(mCatch) {
            //跳转设置界面
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            CrashConfig.getInstance().getApplication().startActivity(intent);
        }
    }

    /**跳转设置悬浮窗界面**/
    public static void goSetPop(int requestCode,Context context){
        if(mCatch) {
            //跳转设置界面
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            ((AppCompatActivity)context).startActivityForResult(intent, requestCode);
        }
    }

}
