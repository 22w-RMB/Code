'''

    前置条件

'''
import unittest
import requests

url = "http://8.129.91.152:8766/futureloan/member/login"

headers = {
    "Content-Type":"application/json",
    "X-Lemonban-Media-Type":"lemonban.v2"
}

json1 = {
    "mobile_phone": "13845223111",
    "pwd": "12345678"
}

print(requests.post(url=url, headers=headers, json=json1).json())
#
# class TestLogin(unittest.TestCase):
#
#     def setUp(self) -> None:
#         print("正在准备执行测试用例")
#     def tearDown(self) -> None:
#         print("已经执行完测试用例")
#
#     def test_login_1_success(self):
#         try:
#             self.assertEqual(1,1)
#         except AssertionError as e:
#             print("断言失败",e)
#
#     def test_login_2_error(self):
#         self.assertEqual(1, 3 - 3)


aa = '{"c":"A","a":"b"}'
print(aa)
import json

print(json.loads(aa))