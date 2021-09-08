

import unittest
import json

from common.db_handler import DBHandler
from common.random_phone import generateMobile
from common.python_handler import ExcelConfig, OtherConfig, SqlConfig
from common.excel_handler import ExcelHandler
from common.request_handler import RequestHandler
from precondition.helper import register
from common.logger_handler import loggerInstance
from common import my_ddt

# print(testData)



@my_ddt.ddt
class TestLogin(unittest.TestCase):
    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    loginData = ExcelHandler(ExcelConfig.excelAbsPath).readSheetAllData(ExcelConfig.loginSheetName)

    @classmethod
    def setUpClass(cls) -> None:
        cls.db = DBHandler(host=SqlConfig.host, port=SqlConfig.port,
                           user=SqlConfig.user, password=SqlConfig.password,
                           charset=SqlConfig.charset,  # 不能是 utf-8
                           database=SqlConfig.database, cursorclass=SqlConfig.cursorclass)
        cls.newPhoneData = register()

    def setUp(self) -> None:
        print("准备测试用例")
        self.session = RequestHandler()

    def tearDown(self) -> None:
        print("执行完测试用例")
        self.session.close()


    @classmethod
    def tearDownClass(cls) -> None:
        # print(testData)
        cls.db.close()
        pass

    @my_ddt.data(*loginData)
    def test_login(self,singleLoginData):


        data = self.replaceLoginData(singleLoginData['data'])
        print(data)
        url = OtherConfig.remoteHost + singleLoginData['url']
        resultJson = self.session.visit(method=singleLoginData['method'],
                                        headers=json.loads(singleLoginData['headers']),url= url,
                                        json = json.loads(data))

        print(resultJson)
        try:
            self.assertEqual(resultJson['code'],singleLoginData['expect'])
            print("测试用例通过")
            self.excelHandler.writeData(ExcelConfig.loginSheetName, int(singleLoginData['case_id']) + 1, 10, "通过")
        except AssertionError as e:
            loggerInstance.error("测试用例失败，报错信息： %s" % e)
            self.excelHandler.writeData(ExcelConfig.loginSheetName, int(singleLoginData['case_id']) + 1, 10, "失败")
            raise e

    def replaceLoginData(self,requestData):

        if "#pwd#" in requestData:
            requestData = requestData.replace("#pwd#",self.newPhoneData['pwd'])

        if "#new_phone#"  in requestData:
            requestData = requestData.replace("#new_phone#", self.newPhoneData['mobile_phone'])

        if "#not_exist_phone#" in requestData:
            phone = register(isNeedRegister=False)['mobile_phone']

            requestData = requestData.replace("#not_exist_phone#", phone)

        return requestData

