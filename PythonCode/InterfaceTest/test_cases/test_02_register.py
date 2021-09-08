import json
import unittest
from common import my_ddt

from common.db_handler import DBHandler
from common.python_handler import ExcelConfig, OtherConfig, SqlConfig
from common.excel_handler import ExcelHandler
from common.random_phone import generateMobile
from common.request_handler import RequestHandler
from common.logger_handler import loggerInstance


# print(testData)



@my_ddt.ddt
class TestRegister(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.rechargeSheetName)


    def setUp(self) -> None:
        print("准备测试用例")
        self.session = RequestHandler()
        self.db = DBHandler(host= SqlConfig.host , port=SqlConfig.port,
                       user = SqlConfig.user, password=SqlConfig.password,
                       charset=SqlConfig.charset,  # 不能是 utf-8
                       database=SqlConfig.database,cursorclass=SqlConfig.cursorclass)



    def tearDown(self) -> None:
        print("执行完测试用例")
        self.session.close()
        self.db.close()

    @my_ddt.data(*testData)
    def test_register(self,data):
        url = OtherConfig.remoteHost + data['url']

        if "#exist_phone#" in data['data']:
            user = self.db.query("select * from  member limit 1;")
            if user:
                print(user)
                data['data'] = data['data'].replace("#exist_phone#", user['mobile_phone'])
            else:
                pass
        elif "#new_phone#" in data['data']:
            while True:
                gen_mobile = generateMobile()
                mobile = self.db.query("select * from  member where mobile_phone=%s limit 1;",args=[gen_mobile])
                if not mobile:
                    break
            data['data'] = data['data'].replace("#exist_phone#", gen_mobile)
        jsonData = self.session.visit(method=data['method'], url=url,
                                      headers=json.loads(data['headers']),
                                      json=json.loads(data['data']))
        # print(jsonData)
        try:
            self.excelHandler.writeData(ExcelConfig.registerSheetName,int(data['case_id'])+1,9,jsonData['code'])
            self.assertEqual(jsonData['code'], 1)
            self.excelHandler.writeData(ExcelConfig.registerSheetName, int(data['case_id']) + 1, 10, "通过")
        except AssertionError as e:
            loggerInstance.error("测试用例失败")
            self.excelHandler.writeData(ExcelConfig.registerSheetName, int(data['case_id']) + 1, 10, "失败")
            raise e


if __name__ == '__main__':
    # print(testData)
    pass