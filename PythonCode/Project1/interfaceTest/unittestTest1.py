
import unittest
import requests

'''
    兼容 get，post，delete，put等等方法的请求：
        session.request(method,url,params=params,data=data,json=json,**kwargs)
'''
class HttpHandler:
    def __init__(self):
        self.session = requests.session()

    def visit(self,url,method,params=None, data = None, json = None, **kwargs):

        # if method.lower() == 'get':
        #     res = self.session.get(url,params=params,**kwargs)
        # elif method.lower() == 'post':
        #     res = self.s  ession.post(url,params=params,data=data,json=json,**kwargs)
        #
        res = self.session.request(method,url,params=params,data=data,json=json,**kwargs)

        try:
            return res.json()
        except ValueError:
            print("Not JSON")

'''
    1、unittest 中 测试方法的执行顺序 默认是根据 ascii 编码排序的
    2、如果我们想手工调整测试用例的执行顺序，不同的字母前面加 数字
        如：  test_login_1_success， test_login_2_error
        
    pycharm 运行的注意事项：
    1、在空行处右击，执行整个模块
    2、在类名上，执行单个测试类
    3、在方法名上，执行单个测试用例
    
    通过命令行方式执行 unittest
    1、python -m unittest test_module test_module2
    2、python -m unittest test_module.TestClass
    3、python -m unittest test_module.TestClass.test_method
    4、python -m unittest tests/test_module.TestClass.test_method
    
    断言方式：
    assertTrue，判断条件 
    assertGreater， a > b
    assertIn， a in b
'''
class TestLogin(unittest.TestCase):
    def test_login_1_success(self):
        try:
            self.assertEqual(1,1)
        except AssertionError as e:
            print("断言失败",e)

    def test_login_2_error(self):
        self.assertEqual(1, 3 - 3)

class TestRegister(unittest.TestCase):
    def test_register_1_success(self):
        try:
            self.assertEqual(1,1)
        except AssertionError as e:
            print("断言失败",e)

    def test_register_2_error(self):
        self.assertEqual(1, 3 - 3)

if __name__ == "__main__":
    print("h")
    unittest.main()