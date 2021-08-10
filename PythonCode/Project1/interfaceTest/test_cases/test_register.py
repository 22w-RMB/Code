import unittest


class TestRegister(unittest.TestCase):
    def test_register_1_success(self):
        try:
            self.assertEqual(1,1)
        except AssertionError as e:
            print("断言失败",e)

    def test_register_2_error(self):
        self.assertEqual(1, 3 - 3)