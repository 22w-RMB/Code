# # # from aa import a
# #
# #
# # class Demo():
# #     token = ""
# #     i = 2
# #
# #
# # d = Demo()
# # d.i = 5
# # Demo.i = 4
# # print(Demo.i)
#
#
# import re
# str = '"#member#"dvdvdsadadcdvf6412354987356123546"#id#"'
#
# patternStr = r'#\w+#'
#
# pattern = re.compile(patternStr)
#
# aa = re.findall(patternStr,str)
# print(aa)
#
#
# # f = re.finditer(pattern,str)
# # l = {"#member#":"www",
# #      "#id#":"didd"}
# # print(f)
# # for ff in f:
# #     print(ff.group())
# #     print(type(ff.group()))
# #     print(l[ff.group()])
# #     str = str.replace(ff.group(),l[ff.group()])
#
# print(str)



import re



# strr = '{"member_id": "#member_id#", "loan_id": "#loan_id#", , "token": "#token#"}'
#
#
# def replace_label(target):
#
#     re_pattern = r'#(.*?)#'
#     while re.findall(re_pattern,target):
#
#         key = re.search(re_pattern,target).group(1)
#         print(key)
#         target = re.sub(re_pattern, str(getattr(Context,key)), target, 1)
#     return target
#
# t = replace_label(strr)
# # print(t)
#
# re_patternr = r'#(.*?)#'
#
# # re.search(pattern, str) ：查找第一个匹配到的对象( 匹配到的是正则表达式括号内的 )
# keyg = re.search(re_patternr,strr)   # <re.Match object; span=(15, 26), match='#member_id#'>
# # 通过  .group(1) ： 可以获取到匹配到的值
# keygggg = re.search(re_patternr,strr).group(1)   # member_id
#
# # re.findall(pattern, str) ：匹配字符串所有符合正则表达式的 ( 匹配到的是正则表达式括号内的 )
# alll = re.findall(re_patternr,strr)  # ['member_id', 'loan_id', 'token']
#
#
# # re.sub(pattern, 替换的值, 包含要被替换的字符串的字符串, 替换的个数)
# # 替换是替换整个正则表达式，而非只替换正则表达式括号里面的
# # 如字符串：strr = '{"member_id": "#member_id#"}'，正则表达式为 #(.*?)#
# # ，像 findall，匹配到的是 member_id，而 sub 替换是替换 #member_id#
# # 替换的个数不填的话，默认替换匹配到所有符合正则表达式的对象；填 1 的话，默认替换第一个匹配到的
# ress1 = re.sub(re_patternr, "我是替换的值", strr)
# # 结果：{"member_id": "我是替换的值", "loan_id": "我是替换的值", , "token": "我是替换的值"}
#
# ress2 = re.sub(re_patternr, "我只替换一个", strr, 1) #
# # 结果：{"member_id": "我只替换一个", "loan_id": "#loan_id#", , "token": "#token#"}


# class Context:
#     member_id = 123
#     token = "cacdafe"
#
#     @property
#     def loan_id(self):
#
#
#
#         pass












# d = { "member_id":123517697, "title":"报名 Java 全栈自动dsacascs化课程9.1","amount":6300.00, "loan_rate":12.0, "loan_term":12, "loan_date_type":1, "bidding_days":5 }
#
#
# url = "http://8.129.91.152:8766/futureloan/loan/add"
#
# headers = {'Content-Type': 'application/json', 'X-Lemonban-Media-Type': 'lemonban.v2', 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJfaWQiOjEyMzUxNzY5NywiZXhwIjoxNjMwNTAyMjM0fQ.V3sSotqjVApAMdeUDVg7uGDXFn4RY52mZFNRgLi2PDs5654A_BV3vYH_pNwPIx2Nu806TlYFXYy-J15F_SY4tA'}
# import requests
# import json
# res = requests.post(url=url,headers=headers,json=json.loads(d))
# print(res.json())

# investd = '{"member_id":123517697, "loan_id":103757,"amount":"100"}'
#
# investurl = "http://8.129.91.152:8766/futureloan/member/invest"
#
# investheaders = {'Content-Type': 'application/json', 'X-Lemonban-Media-Type': 'lemonban.v2',
#                  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJfaWQiOjEyMzUxNzY5NywiZXhwIjoxNjMwNTA0MTU5fQ.dxeh-Eae-r0whIKrp9ruClRovq1xm6dB6pZ5LDO3ghjB7JrAFWwVcDbHvDucRXzxrfvWl-o9XTED-9BoJNMgbA'}
# import requests
# import json
# res = requests.post(url=investurl,headers=investheaders,json=json.loads(investd))
# print(res.json())

# registerd = '{"mobile_phone": "13888465211","pwd": "12345678","type":0}'
# registerurl = "http://8.129.91.152:8766/futureloan/member/register"
registerheaders = {'Content-Type': 'application/json', 'X-Lemonban-Media-Type': 'lemonban.v2'}
# import requests
# import json
# res = requests.post(url=registerurl,headers=registerheaders,json=json.loads(registerd))
# print(res.json())
#

print(type(str(registerheaders)))