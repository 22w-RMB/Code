import datetime
import os

# projectPath = os.path.dirname(os.path.dirname(__file__))
import time

from pymysql.cursors import DictCursor



def joinName(*args,fileName=None):
    # print(__file__)
    projectPath = os.path.dirname(os.path.dirname(__file__))
    # print(projectPath)
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
    loginSheetName = "login"
    rechargeSheetName = "recharge"
    investSheetName = "invest"
    withdrawSheetName = "withdraw"
    updateNameSheetName = "update_name"
    auditSheetName = "audit"
    infoSheetName = "info"
    loansSheetName = "loans"
    addLoanSheetName = "add_loan"

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
    preconditionData = {
        "mobile_phone": "13541186875",
        "pwd": "12345678"
    }
    preconditionHeader={
        "Content-Type":"application/json",
        "X-Lemonban-Media-Type":"lemonban.v2"
    }
    adminData = {
        "mobile_phone": "13888465211",
        "pwd": "12345678"
    }
    registerUrl = remoteHost + "/futureloan/member/register"
    loginUrl = remoteHost + "/futureloan/member/login"
    rechargeUrl = remoteHost + "/futureloan/member/recharge"
    addLoanUrl = remoteHost + "/futureloan/loan/add"
    verifyUrl = remoteHost + "/futureloan/loan/audit"

    addLoanAmount = 6300

    registerData = { "mobile_phone": "#new_phone#","pwd": "12345678"}

    rechargeData = { "member_id": "#member_id#", "amount": addLoanAmount }

    addLoanData = {"member_id": "#member_id#",
         "title": "报名 Java 全栈自动dsacascs化课程9.1",
         "amount": addLoanAmount,
         "loan_rate": 12.0,
         "loan_term": 12,
         "loan_date_type": 1,
         "bidding_days": 5}


    # 为什么此处请求数据使用 字符串 ？
    # 因为 请求数据中有个参数是 true，json格式只能写成true，如果写成字典格式，python 无法识别 true
    # 所以先用字符串保存，使用时再通过 json.loads(verifyData) 转换成 json 格式
    verifyData =  '{ "loan_id": #loan_id#, "approved_or_not": true }'


# print(LoggerConfig().loggerAbsPath)
# # print(ExcelConfig().excelAbsPath)
# print(OtherConfig.addLoanData)