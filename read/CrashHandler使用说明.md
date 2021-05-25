## CrashHandler使用说明

### 简介
CrashHandler 时用于本地捕捉异常，使用它可以在程序崩溃时实现`崩溃日志淋湿处理`及`界面上显示具体崩溃日志`,方便开发者查找`bug`。

### 使用说明
#### 一.初始化
在你项目自定义`Application`的`onCreate()`中进行异常捕获类初始化：
```
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
```
对于CrashHandler.getInstance().init(boolean isCatch,OnCrashListener listener)参数说明：
- isCatch：是否捕获异常。设置true表示使用异常捕获功能，false表示不使用。默认情况为false，即不使用。
- listener：异常发生时的处理监听。里面有两个方法`uploadInfo(String info)`和`exitApp()`。
  当我们想在异常发生时给服务器上传错误信息或本地写文件的话，可以在`uploadInfo(String info)`中执行你想处理的逻辑。
  `exitApp()`是错误弹窗时的点击事件，一般我们在`exitApp()`中的操作是关闭所有`Activity`并退出程序。
  
接着我们要手动申请`全局Popwindow权限`,这部分逻辑我们一般在`App`的启动界面处理。当涉及到异常捕获功能时，我们先申请`全局Popwindow权限`，
若此权限申请成功，则进行其他用户权限申请(一般是调用第三方权限库)，若无其他权限申请，则进入正常的`app`业务逻辑。若申请失败则做界面提示并退出程序。
在启动页`(Activity)`申请全局`Popwindow`权限示例代码如下。
##### 1.1 定义一个申请全局Popwindow权限的code
```
    //请求全局window code
    private int mRequestWindowCode=345;
```
##### 1.2 申请全局Popwindow权限
在启动页执行完毕后，先申请全局Popwindow权限,此权限申请ok后再去申请app所需用户权限(一般是调用第三方权限库)
```
        //请求crash所需全局window权限
        if (!CrashPopUtil.hasPermission()) {
            //跳转设置界面
            CrashPopUtil.goSetPop(mRequestWindowCode,this);
        }else {
            CrashLogUtil.i("======有悬浮窗权限======");
            //接下来的流程
            doNext();
        }
```
##### 1.3 在Activity的onActivityResult方法中做回调处理
然后在Activity的onActivityResult方法中做回调处理：
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==mRequestWindowCode){
            //悬浮窗申请
            if(CrashPopUtil.hasPermission()){
                CrashLogUtil.i("======有悬浮窗权限======");

                //接下来的流程
                doNext();
            }else{
                CrashLogUtil.i("======悬浮窗权限授权失败,请退出程序======");
            }
        }
    }
```
##### 1.4 在doNext()中处理接下来的流程
待悬浮窗权限授权成功后，再去执行app用户权限申请，或进入app流程。
```
    /**接下来的流程**/
    private void doNext(){
        //进行本app第三方用户权限库申请 或 进入app正常流程
        //......

    }
```
#### 二. 测试异常捕捉能力
你可以在需要用的地方做一个bug捕捉测试。在想制造Crash的地方调用以下方法：
```
   //制造一个bug
   CrashUtil.makeCrash();
```

