

import logging
from excelRWTest.configurationTest.test1 import config

class LoggingHandler(logging.Logger):

    def __init__(self,name="root",level="DEBUG",file=None,
                 format="%(asctime)s %(filename)s-%(lineno)d:%(name)s:%(levelname)s - %(message)s"):
        super().__init__(name)

        self.setLevel(level)

        fm = logging.Formatter(format)

        if file:
            fileHandler = logging.FileHandler(file,encoding="utf-8")
            fileHandler.setLevel(level)
            fileHandler.setFormatter(fm)
            self.addHandler(fileHandler)
        consoleHandler = logging.StreamHandler()
        consoleHandler.setLevel(level)
        consoleHandler.setFormatter(fm)
        self.addHandler(consoleHandler)
        
    def info(self,msg) -> None:
        print("soninfo")
        super().info(msg)

# 此处定义变量后，其他地方直接可以导入该模块的 logger 使用，而不需要再次去创建实例
logger = LoggingHandler(config.logger_name,file=config.logger_file)

if __name__ == '__main__':
    LoggingHandler().info("info")
