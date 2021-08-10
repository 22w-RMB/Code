import json
import unittest

import ddt

from common.excel_handler import ExcelHandler
from common.python_handler import ExcelConfig, OtherConfig
from common.request_handler import RequestHandler
from common.logger_handler import loggerInstance


@ddt.ddt
class TestRecharge(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.rechargeSheetName)

    def setUp(self) -> None:
        self.session = RequestHandler()
        pass


    def tearDown(self) -> None:
        self.session.close()
        pass


    @ddt.data(*testData)
    def test_rehcarge(self,data):

        url = OtherConfig.remoteHost + data['url']

        jsonData = self.session.visit(url=url,method=data['method'],headers=data['headers'],
                                      json=json.loads(data['data']))

        try:
            self.excelHandler.writeData(ExcelConfig.rechargeSheetName, int(data['case_id']) + 1, 9, jsonData['code'])
            self.assertEqual(jsonData['code'],data['expect'])
            loggerInstance.info("测试用例成功")
            self.excelHandler.writeData(ExcelConfig.rechargeSheetName, int(data['case_id']) + 1, 10, "通过")
        except AssertionError as e:
            loggerInstance.error("测试用例失败")
            self.excelHandler.writeData(ExcelConfig.rechargeSheetName,int(data['case_id'])+1,10, "失败")

