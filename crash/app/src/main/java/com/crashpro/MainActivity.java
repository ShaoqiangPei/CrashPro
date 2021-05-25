package com.crashpro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlibrary.local_crash.CrashPopUtil;
import com.crashlibrary.util.CrashLogUtil;
import com.crashlibrary.util.CrashUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //请求全局window code
    private int mRequestWindowCode=345;

    private TextView mTvName;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTvName=findViewById(R.id.mTvName);
        mBtn=findViewById(R.id.mBtn);

        initData();
        setListener();
    }

    private void initData(){

    }

    private void setListener(){
        mBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtn:
                test();
                break;
            default:
                break;
        }
    }


    private void test(){
        if (!CrashPopUtil.hasPermission()) {
            //跳转设置界面
            CrashPopUtil.goSetPop(mRequestWindowCode,this);
        }else {
            CrashLogUtil.i("======有悬浮窗权限======");
            //接着申请用户权限
            //......

            CrashUtil.makeCrash();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==mRequestWindowCode){
            //悬浮窗申请
            if(CrashPopUtil.hasPermission()){
                CrashLogUtil.i("======有悬浮窗权限======");
                //接着申请用户权限
                //......


            }else{
                CrashLogUtil.i("======悬浮窗权限授权失败,请退出程序======");
            }
        }
    }
}
