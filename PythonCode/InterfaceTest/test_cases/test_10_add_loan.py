import json
import unittest

from common import my_ddt
from common.excel_handler import ExcelHandler
from common.python_handler import ExcelConfig, OtherConfig
from common.request_handler import RequestHandler
from precondition.helper import Context, replaceLabel


@my_ddt.ddt
class TestAddLoan(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.addLoanSheetName)


    def setUp(self) -> None:

        self.session = RequestHandler()
        self.context = Context()

    def tearDown(self) -> None:
        self.session.close()


    @my_ddt.data(*testData)
    def test_add_laon(self, singleData):

        url = OtherConfig.remoteHost + singleData['url']

        headers = json.loads(singleData['headers'])
        headers['Authorization'] = self.context.token

        requestData = replaceLabel(singleData['data'])

        if "$wrong_member_id$" in requestData:
            requestData = requestData.replace("$wrong_member_id$", str(self.context.member_id + 10))

        responseData = self.session.visit(method=singleData['method'],
                                          url=url,
                                          headers=headers,
                                          json = json.loads(requestData))

        print(responseData)

        try:
            self.assertEqual(singleData['expect'], responseData['code'])
            print("测试用例通过")
        except AssertionError as e:
            print("测试用例不通过： %s " % e)
            raise e



