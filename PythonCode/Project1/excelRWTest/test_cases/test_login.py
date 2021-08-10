

import unittest

from excelRWTest.lib import ddt

from excelRWTest.excel import Handler
from excelRWTest.excel import demo_excel

'''
    ddt : 数据驱动思想： data driven testing
    现在这里使用的是 python 中的一个叫做  ddt 的库
    ddt 库 是 unittest 的一个插件，需要跟 unittest 搭配起来使用
    python / unittest / ddt 自动化测试框架
'''


testData = [{"url":"http://8.129.91.152:8766/futureloan/member/register",
             "method":"post",
             "json":{"mobile_phone": "13845223112",
                     "pwd": "12345678"},
             "headers":{"Content-Type":"application/json",
                        "X-Lemonban-Media-Type":"lemonban.v2"},
             "expected":"hello",
             "title":"登录邓公"},
            {"url": "http://8.129.91.152:8766/futureloan/member/register",
             "method": "post",
             "json": {"mobile_phone": "13845223113",
                      "pwd": "12345678"},
             "headers": {"Content-Type": "application/json",
                         "X-Lemonban-Media-Type": "lemonban.v2"},
             "expected": "hello",
             "title":"登录失败"}]

testData = demo_excel.ExcelHandler(r"D:\code\PythonCode\Project1\excelRWTest\cases.xlsx").readAllData("登录")

@ddt.ddt
class TestLogin(unittest.TestCase):
    def setUp(self) -> None:
        print("正在准备执行测试用例")
    def tearDown(self) -> None:
        print("已经执行完测试用例")

    @ddt.data(*testData)
    def test_login(self,data):
        # try:
            res = Handler.HttpHandler().visit(
                data["url"],
                data["method"],
                json=data["json"],
                headers=data["headers"]
            )
            self.assertEqual(res,data["expected"])
        # except AssertionError as e:
        #     print("断言失败",e)
        #     raise AssertionError


