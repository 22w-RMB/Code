import json
import unittest

from common import my_ddt
from common.db_handler import DBHandler
from common.excel_handler import ExcelHandler
from common.python_handler import OtherConfig, ExcelConfig
from common.request_handler import RequestHandler
from precondition.helper import Context, replaceLabel


@my_ddt.ddt
class TestUpdateName(unittest.TestCase):

    excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
    testData = excelHandler.readSheetAllData(ExcelConfig.updateNameSheetName)

    @classmethod
    def setUpClass(cls) -> None:
        cls.db = DBHandler()
        pass

    def setUp(self) -> None:
        self.session = RequestHandler()
        self.context = Context()

        pass

    def tearDown(self) -> None:
        self.session.close()
        pass

    @classmethod
    def tearDownClass(cls) -> None:
        cls.db.close()
        pass

    @my_ddt.data(*testData)
    def test_update_name(self,singleData):

        url = OtherConfig.remoteHost + singleData['url']
        print(url)

        headers = json.loads(singleData['headers'])
        headers['Authorization'] = self.context.token

        requestData = replaceLabel(singleData['data'])

        if '$wrong_member_id$' in requestData:
            requestData = requestData.replace("$wrong_member_id$", str(self.context.member_id + 2))

        if "$equal_other_user$" in requestData:
            otherUserName = self.db.query("select * from member where id < %s order by id limit 1;",args=[self.context.member_id,])['reg_name']
            print(otherUserName)
            requestData = requestData.replace("$equal_other_user$", otherUserName)

        print(requestData)
        responseData = self.session.visit(method=singleData['method'],
                                          url=url,
                                          headers=headers,
                                          json=json.loads(requestData))
        print(responseData)
        try:
            # 第一个是 Expected，期望结果，第二个是 实际结果
            self.assertEqual(singleData['expect'], responseData['code'],)
            print("测试用例通过")
        except AssertionError as e:

            print("测试用例不通过： %s" % e)
            raise e

        pass


