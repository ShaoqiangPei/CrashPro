# CrashPro  
[![](https://jitpack.io/v/ShaoqiangPei/CrashPro.svg)](https://jitpack.io/#ShaoqiangPei/CrashPro)

## 简介
本工具库具有以下能力:  
- 捕获本地异常
- `bugly`捕获远程异常
- `bugly`全量更新(版本升级)

## 使用说明
### 一. 库依赖
在你project对应的buid.gradle中添加如下代码：
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在你要使用的module对应的buid.gradle中添加如下代码(以0.0.1版本为例)：
```

	dependencies {
	        implementation 'com.github.ShaoqiangPei:CrashPro:0.0.1'
	}
```
**需要注意的是,若你项目中之前已经集成过`Bugly`库,如以下引用:**
```
        implementation 'com.tencent.bugly:crashreport_upgrade:latest.release'
        implementation 'com.tencent.bugly:nativecrashreport:latest.release'
```
**则你需要把以上依赖给注释掉，因为本库依赖中已涵盖以上功能。**  

在你项目的自定义`Application`类的`onCreate()`中初始化本库：
```
public class AppContext extends MyApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化异常捕捉库
        CrashConfig.getInstance().init(this)
                //true:打开log调试模式,默认为false
                .setDebug(true);

    }

}
```
### 二. 主要功能类
[BuglyUtil](https://github.com/ShaoqiangPei/CrashPro/blob/master/read/BuglyUtil%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.md) ———— 集成`Bugly`异常上报功能, `Bugly`全量更新app版本功能  
[CrashHandler](https://github.com/ShaoqiangPei/CrashPro/blob/master/read/CrashHandler%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.md) ———— 本地异常捕捉工具类  

### 三. Log开关及查看Log
#### 3.1 开启/关闭 本库内部log打印
为了方便大家在调试期间查找问题，本库提供库内部Log日志的开关。

//设置本库内部Log打印开关
CrashConfig.getInstance().setDebug(true);//是否开启本库内部log打印(默认false,不开启)
一般此设置结合本库的初始化，一起放到你项目中自定义的Application类中一起进行。

#### 3.2 查看本库内部log打印
在开启本库内部log打印的情况下，你可以将你logcat的tag设置为crash,用以查看本库内部log打印日志。
本库内部日志 tag=“crash”，log打印级别多为 i。

### 四. 添加混淆
如若项目需要，为了避免混淆SDK，在Proguard混淆文件中增加以下配置：
```
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
```
