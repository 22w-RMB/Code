

import unittest
import ddt
import json

from common.random_phone import generateMobile
from common.python_handler import ExcelConfig, OtherConfig
from common.excel_handler import ExcelHandler
from common.request_handler import RequestHandler
from common.logger_handler import loggerInstance

testData = ExcelHandler(ExcelConfig.excelAbsPath).readSheetAllData(ExcelConfig.registerSheetName)
# print(testData)



@ddt.ddt
class TestRegister(unittest.TestCase):

    def setUp(self) -> None:
        print("准备测试用例")
        self.session = RequestHandler()


    def tearDown(self) -> None:
        print("执行完测试用例")
        self.session.close()


    @classmethod
    def tearDownClass(cls) -> None:
        # print(testData)
        pass

    @ddt.data(*testData)
    def test_register(self,data):

        url = OtherConfig.remoteHost + data['url']


        if "#exist_phone#" in data['data']:
            data['data'] = data['data'].replace("#exist_phone#",generateMobile())
        elif "#new_phone#" in data['data']:
            data['data'] = data['data'].replace("#exist_phone#", generateMobile())

        jsonData = self.session.visit(method=data['method'],url=url,
                                      headers=json.loads(data['headers']),
                                      json=json.loads(data['data']))
        data['method'] = "get"
        # print(jsonData)
        try:
            self.assertEqual(jsonData['code'], 1)
        except AssertionError as e:
            loggerInstance.error("测试用例失败： %s" % e)
            raise e

if __name__ == '__main__':
    print(testData)