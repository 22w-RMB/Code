import json
import unittest

from common import my_ddt
from common.db_handler import DBHandler
from common.excel_handler import ExcelHandler
from common.python_handler import ExcelConfig, OtherConfig
from common.request_handler import RequestHandler
from precondition.helper import Context, replaceLabel


@my_ddt.ddt
class TestWithdraw(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.withdrawSheetName)


    @classmethod
    def setUpClass(cls) -> None:
        cls.db = DBHandler()

        pass

    def setUp(self) -> None:
        self.context = Context()
        self.session = RequestHandler()
        self.updateAmount()
        pass

    def tearDown(self) -> None:
        self.session.close()
        pass

    @classmethod
    def tearDownClass(cls) -> None:
        cls.db.close()

        pass

    @my_ddt.data(*testData)
    def test_withdraw(self, singleData):

        url = OtherConfig.remoteHost + singleData['url']

        headers = json.loads(singleData['headers'])
        headers['Authorization'] = self.context.token

        requestData = self.replaceRequestData(singleData['data'])
        print("requestData: ", requestData)

        responseData = self.session.visit(method=singleData['method'],
                           url = url,
                           json = json.loads(requestData),
                           headers = headers)
        print(responseData)

        try:
            self.assertEqual(responseData['code'],singleData['expect'])
            print("用例通过")
        except AssertionError as e:
            print("用例不通过，%s" % e)
            raise e




    def replaceRequestData(self, requestData):

        requestData = replaceLabel(requestData)

        if "$wrong_member_id$" in requestData:
            requestData = requestData.replace("$wrong_member_id$", str(self.context.member_id + 2))
            return requestData

        if "$greater_50w$" in requestData:
            amount = self.updateAmount(500000.01)
            requestData = requestData.replace("$greater_50w$", amount)
            return requestData

        if "$euqal_50w$" in requestData:
            amount = self.updateAmount(500000)
            requestData = requestData.replace("$euqal_50w$", amount)
            return requestData

        if "$less_50w$" in requestData:
            amount = self.updateAmount(499999.99)
            requestData = requestData.replace("$less_50w$", amount)
            return requestData


        return requestData

    def updateAmount(self, defaultAmount = OtherConfig.addLoanAmount):

        amount = str(defaultAmount)
        id = str(self.context.member_id)
        sql = "update member set leave_amount=%s where id=%s;"
        args = [amount, id, ]

        try:
            self.db.query(sql=sql,args=args)
            return amount
        except Exception as e:
            print(e)
            print("修改用户余额失败")

        pass