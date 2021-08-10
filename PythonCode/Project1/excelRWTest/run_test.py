

import os
import HTMLTestRunner
import unittest


path = os.path.dirname(os.path.abspath(__file__))
casesPath = os.path.join(path,'test_cases')

reportPath = os.path.join(path,"report")
if not os.path.exists(reportPath):
    os.mkdir(reportPath)

reportFile = os.path.join(reportPath,'report.html')


loader = unittest.TestLoader()
suite = loader.discover(casesPath)

with open(reportFile,"wb") as f:
    runner = HTMLTestRunner.HTMLTestRunner(f)
    runner.run(suite)





