# 1 背景
1. 该项目的代码是参加 
[阿里云安全算法挑战赛](https://tianchi.aliyun.com/competition/information.htm?raceId=231585) 时写的。主要利用规则和正则表达式，排名第29。
2. 为了更好地理解项目，对项目进行了重构，保留了处理的主体部分。

# 2 QuickStart
1. 需要配置maven环境。
2. 下载并导入项目，将lib下的sun.misc.BASE64Decoder.jar添加项目中。
3. 运行 [`src/test/java/safe/fish/TestFishFilter.java`](https://github.com/mindawei/aliyun-safe-match/blob/master/src/test/java/safe/fish/TestFishFilter.java) 可使用钓鱼网站检测功能。钓鱼网站检测功能接口如下。
```
FishFilter.isFish(String URL,String HTML)  // 静态方法，传入url和网页内容
```
4. 运行 [`src/test/java/safe/webshell/TestWebShellDetector.java`](https://github.com/mindawei/aliyun-safe-match/blob/master/src/test/java/safe/webshell/TestWebShellDetector.java) 可使用WebShell检测功能。WebShell检测功能接口如下。
```
WebShellDetector.isWebShell(String postData)  // 静态方法，传入网址后面带的参数或者上传内容
```


# 3 大致解题思路

## 3.1 钓鱼网站判断</h2>
* 爬取“站长之家”的网站域名，建立白名单（中文关键字到网站的地址映射表）<br>
* 解析网页,获取关键字<br>
* 根据关键字查询白名单<br>

## 3.2 WebShell通信检测</h2>
* 列举恶意代码，如：eval call_user_func 等<br>
* 写出恶意代码的正则表达式<br>
* 进行正则表达式匹配<br>


# 4 项目结构
* 测试文件夹 `test` 中的代码可以帮助理解一些代码的使用。
* `data` 文件下存放了一些项目需要使用的数据。
* 跟钓鱼网站相关的代码都在 `safe.fish` 包中，其中 `FishFilter.java` 是入口类。
* 跟 WebShell检测相关的代码都在 ` safe.webshell` 包中，其中 `WebShellDetector.java` 是入口类。

以下是 `src/main` 中的代码说明：
```
| - safe.fish 判断钓鱼网站的主体逻辑包

| ----  | DomainConflictChecker.java 跟网页本身的一些链接冲突进行判断

| ----  | FishFilter.java 判断钓鱼网站的入口类，主体逻辑

| ----  | PageInfo.java 根据URL、HTML解析出的网页信息对象

| ----  | PageKeyChecker.java 根据网页文本中的关键字进行检查

| ----  | Result.java 检测结果枚举类

| - safe.fish.utils  判断钓鱼网站的工具类

| ----  | DataLoader.java 加载文件中的数据

| ----  | TitleDivider.java 从网页标题中获取关键字

| ----  | XnTool.java 解析xn--的网址中的中文

| ----  | ZhushiUtil.java 去掉一些注释

| - safe.fish.whitelist  中文到域名的映射包

| ----  | ChineseDns.java 构建中文到域名的映射

| ----  | SpiderOfChinaz.java 爬取站长之家的域名数据

| - safe.webshell 判断webshell的主体逻辑包

| ----  | Decoder.java 解码类

| ----  | TextChecker.java 判断文本是否可以执行

| ----  | WebShellDetector.java 对网址参数进行检测
```

以下是 `src/test` 中的代码说明：
```
| - safe.fish

| ----  | TestFishFilter.java 测试钓鱼网站检测功能

| - safe.fish.utils  

| ----  | TestXnTool.java 测试解析xn--的网址中的中文

| - safe.webshell 

| ----  | TestDecoder.java 测试解码功能

| ----  | TestTextChecker.java 测试判断文本是否可以执行功能

| ----  | TestWebShellDetector.java 测试 WebShell 检测功能
```


# 5 代码重构
本次代码重构的方法如下所示。

|问题 | 处理 |
|--- | --- | 
| 注释掉的代码太多，舍不得扔掉代码，影响可读性 | 简单功能直接删去，复杂的功能可以重构独立到工具类中 |
| 测试和源代码混合在一起 | 将测试代码移入到 `src/test` 中去 |
| 代码中硬嵌手数据太多（比赛平台原因） | 移入到外部数据源中 |
| 类和函数名过于简单随意，难以理解 | 根据类和函数功能，结合百度翻译等工具，取合适的名字 |
| 一些状态无关、没有复用逻辑的类被设计成了单例，多次一举 | 改为静态函数 |
| 钓鱼网站部分代码混乱，存在重复工作部分 | 将解析和判断分离，主要解析在创建 `PageInfo` 类时全部完成 |
| 基于规则（比赛样本较少，也是不得已为之）的结构会导致：逻辑冲突、难以扩展等问题 | 样本充足时可使用机器学习训练的方法（重构中未实现，但已将部分特征集中在 `PageInfo` 类中）|
| 文档不完善 | 完善文档 |
