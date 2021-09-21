import json
import random
import unittest

from common import my_ddt
from common.db_handler import DBHandler
from common.excel_handler import ExcelHandler
from common.python_handler import ExcelConfig, OtherConfig
from common.request_handler import RequestHandler


@my_ddt.ddt
class TestLoans(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.loansSheetName)


    @classmethod
    def setUpClass(cls) -> None:
        cls.db = DBHandler()

    def setUp(self) -> None:

        self.session = RequestHandler()
        self.loanCount = self.db.query("select count(*) as loanCount  from loan ;")['loanCount']

    def tearDown(self) -> None:

        self.session.close()

    @classmethod
    def tearDownClass(cls) -> None:
        cls.db.close()

    @my_ddt.data(*testData)
    def test_loans(self, singleData):

        url = OtherConfig.remoteHost + self.replaceUrl(singleData['url'])
        print(url)

        resposeData = self.session.visit(method=singleData['method'],
                                         headers=json.loads(singleData['headers']),
                                         url = url)
        # print(resposeData)
        try:
            self.assertEqual(singleData['expect'], resposeData['code'])
            print("测试用例通过")
        except AssertionError as e:
            print("测试用例不通过： %s" % e)
            raise e

    def replaceUrl(self, requestUrl):

        pageSize = random.randint(1, self.loanCount//10)
        pageIndex = self.loanCount // pageSize
        randomIndex = random.randint(1, pageIndex)

        if "$ramdom_size$" in requestUrl:
            requestUrl = requestUrl.replace("$ramdom_size$" , str(pageSize))

        if "$ramdom_index$" in requestUrl:
            requestUrl = requestUrl.replace("$ramdom_index$" , str(randomIndex))

        if "$more_index$" in requestUrl:
            requestUrl = requestUrl.replace("$more_index$", str(pageIndex + 3))
            pass

        if "$more_page$" in requestUrl:
            requestUrl = requestUrl.replace("$more_page$", str(self.loanCount + 2))
            pass

        if "$equal_loan_count$" in  requestUrl:
            requestUrl = requestUrl.replace("$equal_loan_count$", str(self.loanCount))

        return requestUrl


