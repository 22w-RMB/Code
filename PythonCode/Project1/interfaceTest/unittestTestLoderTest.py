
import unittest
import os

from interfaceTest.test_cases import test_login, test_register
from interfaceTest.test_cases.test_login import TestLogin
from interfaceTest.test_cases.test_register import TestRegister

'''

收集测试用例 ：TestLoader  ， 加载器  ，加载测试用例
放到测试集合（测试套件） TestSuite

1、 初始化 TestLoader ， unittest.TestLoader()
2、 suite = testloader.discover(文件夹路径, "demo_*.py") 发现测试用例
    注：  "demo_*.py" 这个参数的是意思是 在文件夹路径中找到以 demo 开头的文件 ，不填的话默认以 test开头
3、 如果你想运行的测试用例，放到指定文件夹中

几种加载测试用例的方式：
1、用的最多，整个项目一起加载，使用：discover
2、只想测试某一个具体的模块、功能，使用 loadTestsFromModule 或 loadTestsFromTestCase


'''


# 初始化 TestLoader
testloader = unittest.TestLoader()

dirPath = os.path.dirname(os.path.abspath(__file__))
casePath = os.path.join(dirPath,"test_cases")
# 发现测试用例
# suite = testloader.discover(casePath)

# 单独加载多个指定模块
# suite1 = testloader.loadTestsFromModule(test_login)
# suite2 = testloader.loadTestsFromModule(test_register)

# 添加指定测试类
suite1 = testloader.loadTestsFromTestCase(TestLogin)
suite2 = testloader.loadTestsFromTestCase(TestRegister)

suite = unittest.TestSuite()
suite.addTests(suite1)
suite.addTests(suite2)

print(suite)


# 运行器

reportPath = os.path.join(dirPath,"report")
if not os.path.exists(reportPath):
    os.mkdir(reportPath)

filePath = os.path.join(reportPath,"test_result.txt")

with open(filePath,'w',encoding="utf-8") as f:
    # 初始化运行器，以普通文本生成测试报告， TextTestRunner
    runner = unittest.TextTestRunner(f)
    # 运行测试用例
    runner.run(suite)
