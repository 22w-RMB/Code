import datetime
import os

# projectPath = os.path.dirname(os.path.dirname(__file__))
import time

from pymysql.cursors import DictCursor


def joinName(*args,fileName=None):
    projectPath = os.path.dirname(os.path.dirname(__file__))
    for file in args:
        projectPath = os.path.join(projectPath,file)
        if not os.path.exists(projectPath):
            os.mkdir(projectPath)
    if fileName:
        projectPath = os.path.join(projectPath, fileName)
    return projectPath

class LoggerConfig:
    logName = "log.txt"
    loggerLevel = "DEBUG"
    loggerAbsPath = joinName("output","log",fileName=logName)

class ExcelConfig:
    excelName = "cases.xlsx"
    excelAbsPath = joinName("resource","excel",fileName=excelName)
    registerSheetName = "register"
    rechargeSheetName = "recharge"

class ReportConfig:
    reportName = "report-%s.html" % (datetime.datetime.now().strftime("%m-%d %H：%M：%S"))
    # reportName = r"23-11.html"
    reportAbsPath = joinName("output","report",fileName=reportName)

class SqlConfig:
    host = "8.129.91.152"
    port = 3306
    user = "future"
    password = "123456"
    charset = "utf8"  # 不能是 utf-8
    database = "futureloan"
    cursorclass = DictCursor  # 将获取到的游标以字典格式输出

class OtherConfig:
    testCasesPath = joinName("test_cases")
    remoteHost = "http://8.129.91.152:8766"



print(LoggerConfig().loggerAbsPath)
# print(ExcelConfig().excelAbsPath)