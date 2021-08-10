

import unittest
import HTMLTestRunner
import os
from datetime import datetime
import time

dirPath = os.path.dirname(os.path.abspath(__file__))
testcasePath = os.path.join(dirPath,"test_cases")
filePath = os.path.join(dirPath,"report")
# 获取当前时间戳
nowTime = time.time()  # 是一个 float 类型
print(nowTime)  # 1626449284.9523122
print(int(nowTime))

# 获取当前时间并格式化输出
time_str1 = datetime.now().strftime('%Y-%m-%d,%H:%M:%S')
print(time_str1)  # 2021-07-16,23:28:04

time_str = datetime.now().strftime('%Y-%m-%d')

fileName = "report%s.html"%time_str
file = os.path.join(filePath,fileName)

if not os.path.exists(filePath):
    os.mkdir(filePath)

loader = unittest.TestLoader()
suite = loader.discover(testcasePath)

# 网页的读写格式必须使用 二进制 ，即 b
with open(file,"wb") as f:
    runner = HTMLTestRunner.HTMLTestRunner(f , title="最帅的测试报告",
                                           description= " 最帅的描述",
                                           tester="最帅的测试者")
    runner.run(suite)