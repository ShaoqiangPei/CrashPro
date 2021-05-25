package com.crashlibrary.util;

import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import com.crashlibrary.app.CrashConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Title:
 * description:
 *
 * 可能涉及到的权限：
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>   
 * <uses-permission android:name="android.permission.READ_PHONE_STATE"/> 
 * <uses-permission android:name="android.permission.CALL_PHONE" />
 *
 * autor:pei
 * created on 2021/5/24
 */
public class CrashUtil {

    /**
     * 获得当前日期及时间：年-月-日  时：分：秒(24小时制,若想用12小时制,将"HH:mm:ss"改为"hh:mm:ss")
     * @return eg: 2020-02-07 18:14:50
     */
    public static String getDateTime(){
        String template="yyyy-MM-dd HH:mm:ss";
        Date d = new Date();
        long longtime = d.getTime();
        SimpleDateFormat format = new SimpleDateFormat(template);
        String date = format.format(longtime);
        return date;
    }

    /**
     * 获取手机型号
     * eg:A31
     */
    public static String getDeviceModel(){
        return Build.MODEL;
    }

    /**
     * 获取手机品牌
     * eg:OPPO
     */
    public static String getDeviceBrand(){
        return Build.BRAND;
    }

    /**
     * 获取设备名称
     * eg:UBX
     */
    /**设备名称**/
    public static String getDeviceName() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取系统版本号(用户可见的版本字符串)
     * eg:4.4.4
     */
    public static String getDeviceVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前设备的sdk版本号
     * @return
     */
    public static int getSDKVersion(){
        return Build.VERSION.SDK_INT;
    }


    /**
     * 手机CPU信息
     */
    public static String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"",""};;
        String[] arrayOfString;

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + "  ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();

            return "CPU型号:"+cpuInfo[0]+"  CPU频率:"+cpuInfo[1];

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置文字前景色
     *
     * @param source 操作源，为SpannableString
     * @param index1 起始下标
     * @param index2 终止下标
     * @param color  设置的颜色,如：R.color.red
     * @return
     */
    public static SpannableString setTextFrontColor(SpannableString source, int index1, int index2, int color){
        if(source!=null) {
            int colorVaule = ContextCompat.getColor(CrashConfig.getInstance().getApplication(), color);
            ForegroundColorSpan fcs=new ForegroundColorSpan(colorVaule);
            source.setSpan(fcs,index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return source;
        }
        return null;
    }

    /**制造crash(ArithmeticException)**/
    public static void makeCrash(){
        int a=5;
        int b=0;
        int c=a/b;
    }



}
