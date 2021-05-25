package com.crashlibrary.local_crash;

import android.content.Context;
import android.os.Looper;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import com.crashlibrary.R;
import com.crashlibrary.util.CrashLogUtil;
import com.crashlibrary.util.CrashUtil;
import com.crashlibrary.util.StringUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: 异常捕捉
 *
 * description:
 * autor:pei
 * created on 2021/5/20
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    //打印crash log的key-value
    private String mCrashKey = "startIndex";
    private String mCrashValue = "endIndex";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler = null;

    // 程序的Context对象
    private Context mContext = null;
    private OnCrashListener mOnCrashListener;

    //是否打开捕捉异常模式
    private boolean mCatch =false;

    private CrashHandler(){}

    private static class Holder {
        private static CrashHandler instance = new CrashHandler();
    }

    public static CrashHandler getInstance() {
        return Holder.instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context,boolean isCatch,OnCrashListener listener) {
        mCatch=isCatch;
        mContext = context;
        this.mOnCrashListener=listener;
        if(mCatch) {
            // 获取系统默认的UncaughtException处理器
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            // 设置该CrashHandler为程序的默认处理器
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        //设置是否弹出错误Pop的标志
        CrashPopUtil.mCatch=mCatch;
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        String info =dumpExceptionToFile(t,e);
        uploadExceptionToServer(info);

        if (StringUtil.isEmpty(info) &&mDefaultHandler != null) {
            //系统默认的异常处理器来处理,否则由自己来处理
            mDefaultHandler.uncaughtException(t, e);
        }else {
            CrashLogUtil.i("======test====app=====");

            if(mCatch){
                try {
                    Thread.sleep(1000*20);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            //杀死进程,退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }


    /**可以根据自己需求来,比如获取手机厂商、型号、系统版本、内存大小等等**/
    private String dumpExceptionToFile(Thread t,Throwable e){
        String message =null;
        if(e==null){
            return message;
        }
        StringBuffer buffer = new StringBuffer();
        //线程及异常
        buffer.append("系统:Android 时间:"+ CrashUtil.getDateTime()+"\n");
        buffer.append("ThreadId: "+t.getId()+"ThreadName: "+t.getName()+"\n");
        buffer.append("errorMessage: "+getErrorDetail(e)+"\n");
        //设备信息
        buffer.append("手机型号: "+ CrashUtil.getDeviceModel()+"\n");
        buffer.append("手机品牌: "+ CrashUtil.getDeviceBrand()+"\n");
        buffer.append("设备名称: "+ CrashUtil.getDeviceName()+"\n");
        buffer.append("系统版本号: "+ CrashUtil.getDeviceVersion()+"\n");
        buffer.append("sdk版本号: "+ CrashUtil.getSDKVersion()+"\n");
        buffer.append("CPU信息: "+ CrashUtil.getCpuInfo()+"\n");
        message=buffer.toString();

        //启动线程
        new Thread(new CrashRunnable(message)).start();

        return message;
    }

    class CrashRunnable implements Runnable {

        private String message;

        public CrashRunnable(String message){
            this.message=message;
        }

        @Override
        public void run() {
            Looper.prepare();

            //弹出错误提示pop
            SpannableString sp= translate(message);
            CrashPopUtil.canShow(sp,mContext, new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //退出app
                    if(mOnCrashListener!=null){
                        mOnCrashListener.exitApp();
                    }
                    //杀死app
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });

            Looper.loop();
        }
    }



    /**上传到Server**/
    private void uploadExceptionToServer(String info) {
        if(StringUtil.isEmpty(info)){
            return;
        }
        Log.e("pei","***************错误信息****************\n$info");
        if(mOnCrashListener!=null){
            mOnCrashListener.uploadInfo(info);
        }

    }

    /**获取错误的详细堆栈信息**/
    private String getErrorDetail(Throwable e){
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter, true));
        return stringWriter.getBuffer().toString();
    }

    private SpannableString translate(String message){
        SpannableString sp = null;

        String start="(";
        String end=")";

        CrashLogUtil.i("======1====message="+message);
        if(StringUtil.isNotEmpty(message)){
            List<Map<String,Integer>> indexList = checkList(message,start,end);
            CrashLogUtil.i("======2====indexList="+indexList);
            if(!indexList.isEmpty()){
                sp = new SpannableString(message);
                CrashLogUtil.i("======3====sp="+sp);
                for (Map map:indexList) {
                    CrashLogUtil.i("======it====key="+map.get(mCrashKey)+"    value="+map.get(mCrashValue));
                    sp = CrashUtil.setTextFrontColor(sp,(int)map.get(mCrashKey),((int)map.get(mCrashValue))+1, R.color.red,mContext);
                }
            }
        }
        CrashLogUtil.i("======translate=====sp="+sp);
        return sp;
    }

    private List<Map<String,Integer>> checkList(String message,String startFlag, String endFlag){
        List<Map<String,Integer>>list=new ArrayList<>();
        return checkString(message,startFlag,endFlag,list);
    }

    private List<Map<String,Integer>> checkString(String message, String startFlag, String endFlag, List<Map<String,Integer>> list){
        CrashLogUtil.i("====开始一个轮询===message="+message);

        String temp =null;
        if(message.contains(startFlag)&&message.contains(endFlag)){
            int startIndex =message.indexOf(startFlag);
            int endIndex =message.indexOf(endFlag);
            if(endIndex>startIndex){
                CrashLogUtil.i("====startIndex="+startIndex+"  endIndex="+endIndex);
                if(startIndex<endIndex-1){
                    CrashLogUtil.i("======合法输出======");
                    Map<String,Integer>map=new HashMap<>();
                    map.put(mCrashKey,startIndex);
                    map.put(mCrashValue,endIndex);
                    if(!list.contains(map)){
                        list.add(map);
                    }
                }else{
                    CrashLogUtil.i("======非法输出======");
                }
                temp = StringUtil.replaceByTarget(message,startFlag,"#");
                temp = StringUtil.replaceByTarget(temp,endFlag,"#");
                return checkString(temp,startFlag,endFlag,list);
            }else if(endIndex<startIndex){
                temp=StringUtil.replaceByTarget(message,endFlag,"#");
                return checkString(temp,startFlag,endFlag,list);
            }
        }
        CrashLogUtil.i("====结束===list="+list);
        return list;
    }

    /**异常捕获监听处理**/
    public interface OnCrashListener{

        //有必要的话，可以给服务器上传错误
        void uploadInfo(String info);

        //点击异常退出(一般是退出整个app)
        void exitApp();
    }

}