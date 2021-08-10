import unittest


class TestLogin(unittest.TestCase):

    def setUp(self) -> None:
        print("正在准备执行测试用例")
    def tearDown(self) -> None:
        print("已经执行完测试用例")

    def test_login_1_success(self):
        try:
            self.assertEqual(1,1)
        except AssertionError as e:
            print("断言失败",e)

    def test_login_2_error(self):
        self.assertEqual(1, 3 - 3)