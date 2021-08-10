


import unittest

import HTMLTestRunner
from common.python_handler import OtherConfig,ReportConfig

loader = unittest.TestLoader()
suite = loader.discover(OtherConfig.testCasesPath)
filePath = ReportConfig


with open(ReportConfig.reportAbsPath,mode="wb") as f:
    runner = HTMLTestRunner.HTMLTestRunner(f)
    runner.run(suite)