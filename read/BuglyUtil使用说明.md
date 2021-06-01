## BuglyUtil使用说明

### 简介
`BuglyUtil`是一个集成了`Bugly异常上报`,`Bugly全量版本更新`的工具类。

### 使用说明
#### 一.初始化
在自定义`Application`类`onCreate()`中初始化:
```
//debug: 输出详细的Bugly SDK的Log,建议在测试阶段建议设置成true，发布时设置为false
//context: Application实例
BuglyUtil.init("你申请的AppId",debug,context);
```
#### 二.bugly异常上报能力
##### 2.1 测试bugly异常上报能力
在需要制造bug的地方调用以下方法：
```
BuglyUtil.testCrash();
```
##### 2.2.使用注意事项
若你的项目中同时集成了`BuglyUtil(即bugly功能)`和`CrashHandler`,请熟读以下文章  
[捕捉异常注意事项](https://github.com/ShaoqiangPei/CrashPro/blob/master/read/%E6%8D%95%E6%8D%89%E5%BC%82%E5%B8%B8%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9.md)
##### 2.3.bugly异常上报集成功能更多介绍
更多关于`Bugly异常上报`的使用，可参考文章
[Bugly捕获异常并上报](https://www.jianshu.com/p/98ce4475736d)

#### 三. Bugly全量版本更新
实际使用的是`Bugly全量更新`功能，便于app内部进行版本更新。要顺利接入，你需要做如下操作。  
##### 3.1 http通讯兼容
若你项目中涉及到`http通讯兼容`,你需要在`Androidmanifast.xml`中添加如下代码：
```
    <application
        //兼容http通讯
        android:networkSecurityConfig="@xml/bugly_config"
        
        //......
        //......
        >
        //......

    </application>
```
若你项目的`Androidmanifast.xml`中已经配置了`networkSecurityConfig`属性，请将文件替换成`@xml/bugly_config`
##### 3.2 注册BetaActivity
在`Androidmanifast.xml`中注册`Activity`:
```
<activity
    android:name="com.tencent.bugly.beta.ui.BetaActivity"
    android:configChanges="keyboardHidden|orientation|screenSize|locale"
    android:theme="@android:style/Theme.Translucent" />
```
##### 3.3 添加文件权限
`Andorid6.0+`以上系统，若你项目中未引用过文件相关权限，则需要在`Androidmanifast.xml`中配置`FileProvider`，如下：
```
 <provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.fileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths"/>
</provider>
```
注意,`FileProvider`类是在`support-v4`包中的，检查你的工程是否引入该类库。  
若你项目中已经引用过`FileProvider`, 则需要通过继承`FileProvider`类来解决合并冲突的问题，示例如下：
```
        <provider
            android:name="com.tencent.bugly.beta.utils.BuglyFileProvider"
            android:authorities="${applicationId}.fileProvider"
            android:exported="false"
            android:grantUriPermissions="true"
            tools:replace="name,authorities,exported,grantUriPermissions">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/bugly_provider_path"
                tools:replace="name,resource"/>
        </provider>
```
##### 3.4 手动检测版本
在已经初始化的前提下。`Bugly.init(getApplicationContext(), "注册时申请的APPID", false);`方法会在app启动后自动检测版本。  
当我们的版本更新采用的是推荐模式，这时用户可能会选择下次更新，接着在`App`内部，我们可以给用户提供一个手动检测版本的方法。  
在你需要点击检测版本的地方调用以下代码：
```
        //检测版本
        BuglyUtil.checkVersion(true,false);
```
##### 3.5 更多
更多关于`BUlgy`全量更新集成的使用可参考以下文章  
[Bugly全量更新详解](https://www.jianshu.com/p/60c2a67da1c7)


