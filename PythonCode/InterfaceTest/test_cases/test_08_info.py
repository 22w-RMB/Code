import json
import unittest

from common import my_ddt
from common.db_handler import DBHandler
from common.excel_handler import ExcelHandler
from common.python_handler import ExcelConfig, OtherConfig
from common.request_handler import RequestHandler
from precondition.helper import Context

@my_ddt.ddt
class TestInfo(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.infoSheetName)

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
    def test_info(self, singleData):

        url = OtherConfig.remoteHost + self.replaceUrl(singleData['url'])
        print(url)

        token = self.context.admin_token
        if "$not_admin$" in singleData['headers']:
            token = self.context.token

        headers = json.loads(singleData['headers'])
        headers['Authorization'] = token

        response = self.session.visit(method=singleData['method'],
                                      url=url,
                                      headers=headers,
                                      json=singleData['data'])
        print(response)

        try:
            self.assertEqual(singleData['expect'],response['code'])
            print("测试用例通过")
        except AssertionError as e:
            print("测试用例不通过： %s" % e)
            raise e

    def replaceUrl(self, url):

        if "#member_id#" in url:
            id = self.context.admin_id
            return url.replace("#member_id#", str(id))

        if "$not_exist_member_id$" in url:
            id = self.db.query("select * from member order by id desc limit 1;")['id'] + 10
            return url.replace("$not_exist_member_id$", str(id))

        return url
