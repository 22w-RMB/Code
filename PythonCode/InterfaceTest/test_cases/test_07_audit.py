import json
import  unittest

from common import my_ddt
from common.db_handler import DBHandler
from common.excel_handler import ExcelHandler
from common.python_handler import ExcelConfig, OtherConfig
from common.request_handler import RequestHandler
from precondition.helper import Context


@my_ddt.ddt
class TestAudit(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.auditSheetName)


    @classmethod
    def setUpClass(cls) -> None:
        cls.db = DBHandler()

    def setUp(self) -> None:
        self.session = RequestHandler()
        self.context = Context()


    def tearDown(self) -> None:

        self.session.close()

    @classmethod
    def tearDownClass(cls) -> None:
        cls.db.close()

    @my_ddt.data(*testData)
    def test_audit(self,singleData):
        print(singleData)
        url = OtherConfig.remoteHost + singleData['url']


        token = self.context.admin_token
        if "$not_admin$" in singleData['headers']:
            token = self.context.token

        headers = json.loads(singleData['headers'])
        headers['Authorization'] = token

        requestData = self.replaceRequestData(singleData['data'])
        print(requestData)

        responseData = self.session.visit(method=singleData['method'],
                                          url=url,
                                          headers=headers,
                                          json=json.loads(requestData))
        print(responseData)


        try:
            self.assertEqual(singleData['expect'], responseData['code'])
            print("测试用例通过")
        except AssertionError as e:
            print("测试用例失败： %s" % e)
            raise e


    def replaceRequestData(self, requestData):

        if "#status_1#" in requestData:
            return requestData.replace("#status_1#", self.queryLoanId(1))

        if "$status_2$" in requestData:
            return requestData.replace("$status_2$", self.queryLoanId(2))

        if "$status_3$" in requestData:
            return requestData.replace("$status_3$", self.queryLoanId(3))

        if "$status_4$" in requestData:
            return requestData.replace("$status_4$", self.queryLoanId(4))

        if "$not_exist_loan_id$" in requestData:
            return requestData.replace("$not_exist_loan_id$", self.queryLoanId(isQueryNotExistId=True))

        return requestData

    def queryLoanId(self, status=1, isQueryNotExistId = False) :

        if isQueryNotExistId:
            try:
                print("查询不存在的 Loan Id")
                id = self.db.query("select * from loan order by id desc limit 1 ;")['id']
                return str(id + 10)
            except:
                print("查询失败")

        args = [status, ]
        print("查询 status = %d 的 Loan Id" % status)
        try:
            id = self.db.query("select * from loan where status = %s limit 1 ;",args=args)['id']
            return str(id)
        except:
            print("查询失败")

