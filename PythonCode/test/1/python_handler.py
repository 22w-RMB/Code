import datetime
import os

# projectPath = os.path.dirname(os.path.dirname(__file__))
import time




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


class ExcelConfig:
    excelName = "A1.xlsx"
    excelAbsPath = joinName("resource","excel",fileName=excelName)
    outputSheetName = "量价申报"
    baseSheetName = "交易单元"
    newSheetName = "结果数据"

class LoggerConfig:
    logName = "log.txt"
    loggerLevel = "DEBUG"
    loggerAbsPath = joinName("output","log",fileName=logName)



print(ExcelConfig().excelAbsPath)
