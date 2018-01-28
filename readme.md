# 1 背景
该项目的代码是参加 
[阿里云安全算法挑战赛](https://tianchi.aliyun.com/competition/information.htm?raceId=231585) 时写的。主要利用规则和正则表达式，排名第29。

# 2 解题思路

## 2.1 钓鱼网站判断</h2>
* 爬取“站长之家”的网站域名，建立白名单（中文关键字到网站的地址映射表）<br>
* 解析网页,获取关键字<br>
* 根据关键字查询白名单<br>

## 2.2 WebShell通信检测</h2>
* 列举恶意代码，如：eval call_user_func 等<br>
* 写出恶意代码的正则表达式<br>
* 进行正则表达式匹配<br>


# 3 项目结构
1. `alibaba.safe.utls.decode` 包中是一些解码类。[TestDecoder](https://github.com/mindawei/aliyun-safe-match/blob/master/src/test/java/alibaba/safe/decode/TestDecoder.java) 类对解析进行了测试，也说明了如何使用它们。

2. `alibaba.safe.fish.find` 包中主要用来寻找钓鱼网站。

