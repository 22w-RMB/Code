

import unittest

from common import my_ddt
from common.excel_handler import ExcelHandler
from common.python_handler import ExcelConfig
from common.request_handler import RequestHandler
from precondition.helper import Context


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



        pass

