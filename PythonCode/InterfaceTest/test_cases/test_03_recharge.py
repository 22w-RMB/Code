import json
import unittest

from common import my_ddt
import jsonpath

from common.db_handler import DBHandler
from common.excel_handler import ExcelHandler
from common.python_handler import ExcelConfig, OtherConfig, SqlConfig
from common.request_handler import RequestHandler
from common.logger_handler import loggerInstance
from precondition.helper import Context


@my_ddt.ddt
class TestRecharge(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.rechargeSheetName)

    @classmethod
    def setUpClass(cls) -> None:
        cls.db = DBHandler(host=SqlConfig.host, port=SqlConfig.port,
                            user=SqlConfig.user, password=SqlConfig.password,
                            charset=SqlConfig.charset,  # 不能是 utf-8
                            database=SqlConfig.database, cursorclass=SqlConfig.cursorclass)

    def setUp(self) -> None:
        self.session = RequestHandler()
        self.context = Context()

        #save_token()
        pass


    def tearDown(self) -> None:
        self.session.close()
        # self.db.close()
        pass

    @classmethod
    def tearDownClass(cls) -> None:
        cls.db.close()


    @my_ddt.data(*testData)
    def test_rehcarge(self,data):

        url = OtherConfig.remoteHost + data['url']
        headers = json.loads(data['headers'])
        token = self.context.token
        print(token)
        headers['Authorization'] = token

        # 查询未充值前账户的余额
        sql = "select * from member where id=%s;"
        beforeMoney = self.db.query(sql,args=[self.context.member_id,])['leave_amount']
        print("beforeMoney：",beforeMoney)

        if "#member_id#" in data['data']:
            data['data'] = data['data'].replace("#member_id#",str(self.context.member_id))

        if "#wrong_member_id#" in data['data']:
            data['data'] = data['data'].replace("#member_id#", str(self.context.member_id+1))

        print(data['data'])
        print(data['expect'])
        print(headers)


        jsonData = self.session.visit(url=url,method=data['method'],headers=headers,
                                      json=json.loads(data['data']))
        print(jsonData['code'])



        try:
            # self.excelHandler.writeData(ExcelConfig.rechargeSheetName, int(data['case_id']) + 1, 9, jsonData['code'])
            self.assertEqual(data['expect'],jsonData['code'])
            if jsonData['code'] == 0:
                expectMoney = float(beforeMoney) + float(json.loads(data['data'])['amount'])
                print("expectMoney：", expectMoney)
                actualMoney = jsonpath.jsonpath(jsonData, "$..leave_amount")[0]
                print(type(actualMoney))
                # actualMoney = Decimal(actualMoney)
                print("actualMoney：", actualMoney)
                self.assertEqual(expectMoney,actualMoney)

            loggerInstance.info("测试用例成功")
            self.excelHandler.writeData(ExcelConfig.rechargeSheetName, int(data['case_id']) + 1, 10, "通过")
        except AssertionError as e:
            loggerInstance.error("测试用例失败")
            self.excelHandler.writeData(ExcelConfig.rechargeSheetName,int(data['case_id'])+1,10, "失败")
            raise e

