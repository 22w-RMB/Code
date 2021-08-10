import requests
import json

'''
    使用 cookie 跟 session 访问需要 先登录才能操作的接口

'''

# 登录网站
# loginUrl = "http://8.129.91.152:8766/futureloan/member/login"
registerUrl = "http://test.lemonban.com/futureloan/mvc/api/member/register"
loginUrl = "http://test.lemonban.com/futureloan/mvc/api/member/login"
rechargeUrl = "http://test.lemonban.com/futureloan/mvc/api/member/recharge"

loginData = {
    "mobilephone": "13845223111",
    "pwd": "12345678"
}
rechargeData = {
    "mobilephone": "13845223111",
    "amount": 6300
}

# session方式
# session = requests.session()
# res = session.post(url=loginUrl,data=loginData)
# res1 = session.post(url=rechargeUrl,data=rechargeData)
# print(res.text)
# print(res1.text)
# session.close()

# cookie 方式
res = requests.post(url=loginUrl,data=loginData)
cookie = res.cookies
res1 = requests.post(url=rechargeUrl,data=rechargeData,cookies=cookie)
print(res.text)
print(res1.text)