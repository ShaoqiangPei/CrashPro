package com.crashlibrary.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.crashlibrary.app.CrashConfig;

/**
 * Title: 屏幕相关辅助类
 * description:
 * autor:pei
 * created on 2021/5/25
 */
public class ScreenUtil {

    private ScreenUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static DisplayMetrics getDisplayMetrics(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) CrashConfig.getInstance().getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        display.getMetrics(dm);

        return dm;
    }

    /**
     * 获取屏幕宽
     **/
    public static int getWidth() {
        DisplayMetrics dm=getDisplayMetrics();
        if(dm!=null){
            return dm.widthPixels;
        }
        return 0;
    }

    /**
     * 获取屏幕高度
     */
    public static int getHeight(){
        DisplayMetrics dm=getDisplayMetrics();
        if(dm!=null){
            return dm.heightPixels;
        }
        return 0;
    }


    /**
     * dp转px
     **/
    public static int dp2px(float dpVal, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
    }

    /**
     * sp转px
     **/
    public static int sp2px(float spVal, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources()
                .getDisplayMetrics());
    }

    /**
     * px转dp
     **/
    public static float px2dp(float pxVal, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     **/
    public static float px2sp(float pxVal, Context context) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * textSize缩放比
     **/
    public static float getTextSizeScale() {
        int width = getWidth();
        float rate = (float) width / 320;
        return rate;
    }

}
