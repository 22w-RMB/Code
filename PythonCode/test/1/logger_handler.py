
import logging
from python_handler import LoggerConfig


class LoggerHandler(logging.Logger):
    def __init__(self,name="root",level="DEBUG",file=None,
                 format="%(asctime)s %(filename)s-%(lineno)d:%(name)s:%(levelname)s - %(message)s"):
        super().__init__(name)
        print("还好")
        self.setLevel(level)

        fm =logging.Formatter(format)

        if file:
            # if not os.path.exists(LoggerConfig().loggerFilePath):
            #     os.mkdir(LoggerConfig().loggerFilePath)
            fileHandler = logging.FileHandler(file,encoding="utf-8")
            fileHandler.setLevel(level)
            fileHandler.setFormatter(fm)
            self.addHandler(fileHandler)

        consoleHandler = logging.StreamHandler()
        consoleHandler.setFormatter(fm)
        consoleHandler.setLevel(level)


loggerInstance = LoggerHandler(file=LoggerConfig().loggerAbsPath)
