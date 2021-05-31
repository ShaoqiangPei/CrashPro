## BuglyUtil使用说明

### 简介
`BuglyUtil`是一个集成了`Bugly异常上报`的工具类，便于开发者快速集成`Bugly异常上报`功能。

### 使用说明
#### 一.初始化
在自定义`Application`类`onCreate()`中初始化:
```
//debug: 输出详细的Bugly SDK的Log,建议在测试阶段建议设置成true，发布时设置为false
//context: Application实例
BuglyUtil.init("你申请的AppId",debug,context);
```
#### 二.测试bugly异常上报能力
在需要制造bug的地方调用以下方法：
```
BuglyUtil.testCrash();
```
#### 三.使用注意事项
若你的项目中同时集成了`BuglyUtil(即bugly功能)`和`CrashHandler`,请熟读以下文章  
[捕捉异常注意事项](https://github.com/ShaoqiangPei/CrashPro/blob/master/read/%E6%8D%95%E6%8D%89%E5%BC%82%E5%B8%B8%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9.md)
#### 四.更多
更多关于`Bugly异常上报`的使用，可参考文章
[Bugly捕获异常并上报](https://www.jianshu.com/p/98ce4475736d)

