# 1 背景
1. 该项目的代码是参加 
[阿里云安全算法挑战赛](https://tianchi.aliyun.com/competition/information.htm?raceId=231585) 时写的。主要利用规则和正则表达式，排名第29。
2. 为了更好地理解项目，对项目进行了重构，保留了处理的主体部分。

# 2 大致解题思路

## 2.1 钓鱼网站判断</h2>
* 爬取“站长之家”的网站域名，建立白名单（中文关键字到网站的地址映射表）<br>
* 解析网页,获取关键字<br>
* 根据关键字查询白名单<br>

## 2.2 WebShell通信检测</h2>
* 列举恶意代码，如：eval call_user_func 等<br>
* 写出恶意代码的正则表达式<br>
* 进行正则表达式匹配<br>


# 3 项目结构

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
# 4 其它
* 测试文件夹 `test` 中的代码可以帮助理解一些代码的使用。
* `data` 文件下存放了一些项目需要使用的数据。
