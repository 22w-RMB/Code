import json
import unittest
# import ddt
import jsonpath

from common import my_ddt
from common.db_handler import DBHandler
from common.excel_handler import ExcelHandler
from common.logger_handler import loggerInstance
from common.python_handler import ExcelConfig, OtherConfig, SqlConfig
from common.request_handler import RequestHandler
from precondition.helper import Context, replaceLabel, recharge, addAndVerifyLoan


@my_ddt.ddt
class TestInvest(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    investData = ExcelHandler(ExcelConfig.excelAbsPath).readSheetAllData(ExcelConfig.investSheetName)

    @classmethod
    def setUpClass(cls) -> None:
        cls.db = DBHandler(host=SqlConfig.host, port=SqlConfig.port,
                            user=SqlConfig.user, password=SqlConfig.password,
                            charset=SqlConfig.charset,  # 不能是 utf-8
                            database=SqlConfig.database, cursorclass=SqlConfig.cursorclass)

    def setUp(self) -> None:
        self.session = RequestHandler()
        self.context = Context()
        # 执行用例之前先充值
        recharge()

    def tearDown(self) -> None:
        self.session.close()

    @classmethod
    def tearDownClass(cls) -> None:
        cls.db.close()


    @my_ddt.data(*investData)
    def test_invest(self,singleInvestData):
        # 拼接 请求地址 url
        url = OtherConfig.remoteHost + singleInvestData['url']
        # 请求头添加 token
        headers = json.loads(singleInvestData['headers'])
        headers['Authorization'] = self.context.token
        print(headers)
        print(singleInvestData['data'])

        # data = replaceLabel(singleInvestData['data'])
        # print(data)

        data = self.replaceInvestData(singleInvestData['data'])
        print(data)

        resultJson = self.session.visit(method=singleInvestData['method'],
                                        headers=headers,url= url,
                                        json = json.loads(data))
        print(resultJson)
        try:
            self.assertEqual(resultJson['code'],singleInvestData['expect'])
            self.excelHandler.writeData(ExcelConfig.investSheetName,int(singleInvestData['case_id'])+1,10,"通过")
        except AssertionError as e:
            loggerInstance.error("测试用例失败")
            self.excelHandler.writeData(ExcelConfig.investSheetName, int(singleInvestData['case_id']) + 1, 10, "失败")
            raise e



    def replaceInvestData(self,requestData) -> str:

        requestData = replaceLabel(requestData)

        if "$loan_id$" in requestData:

            # 获取投资项目的信息() --- 获取 loan_id , 和 amount
            infoDict = addAndVerifyLoan()
            requestData = requestData.replace("$loan_id$",str(infoDict['id']))
            amount = infoDict['amount']

            # 如果投资金额大于标的余额，否则就是刚好等于标的余额
            if "$greater_loan_balance$" in requestData:
                amount = amount + 100
                requestData = requestData.replace("$greater_loan_balance$",str(amount))
            else:
                #  投资金额等于标的余额
                requestData = requestData.replace("$loan_balance$", str(amount))

            return requestData

        # 投资金额刚好等于用户余额
        if "$user_balance$" in requestData:
            print("刚好等于用户余额")
            requestData = requestData.replace("$user_balance$",str(self.context.user_balance))
            return requestData

        # 错误的用户名
        if "$wrong_member_id$" in requestData:
            requestData = requestData.replace("$wrong_member_id$", str(self.context.member_id + 2))
            return requestData

        # 投资不存在的项目
        if "$not_exist_loan_id$" in requestData:
            not_exist_loan_id = self.db.query("select * from loan order by id desc limit 1;")['id'] + 10
            print("======= not_exist_loan_id is %d =========" % (not_exist_loan_id -10))
            requestData = requestData.replace("$not_exist_loan_id$", str(not_exist_loan_id))
            return requestData

        if "$finished_loan_id$" in requestData:
            finished_loan_id = self.db.query("select * from loan where status = 5;")['id']
            print("======= finished_loan_id is %d =========" % (finished_loan_id ))
            requestData = requestData.replace("$finished_loan_id$",str(finished_loan_id))

        return requestData

