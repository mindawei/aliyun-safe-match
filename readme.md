#1 背景
该项目的代码是参加 [阿里云安全算法挑战赛](https://tianchi.aliyun.com/competition/information.htm?raceId=231585) 时写的。<br>
<br>
#2 成绩
第29名<br>
<br>
#3 解题思路:<br>
##3.1. 钓鱼网站判断<br>
1) 爬取“站长之家”的网站域名，建立白名单（中文关键字到网站的地址映射表）<br>
2) 解析网页,获取关键字<br>
3) 根据关键字查询白名单<br>
##3.2. WebShell通信检测<br>
1) 列举恶意代码，如：`eval call_user_func`等<br>
2) 写出恶意代码的正则表达式<br>
3) 进行正则表达式匹配<br>