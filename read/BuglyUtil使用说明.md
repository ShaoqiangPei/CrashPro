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
#### 三.更多
更多关于`Bugly异常上报`的使用，可参考文章
[Bugly捕获异常并上报](https://www.jianshu.com/p/98ce4475736d)

