import logging

class LogHandler(logging.Logger):

    def __init__(self,filename=None,level="DEBUG",logName="root",
                 logFmt="%(asctime)s-%(name)s-%(filename)s-%(levelname)s-%(lineno)d-%(message)s"):

        super().__init__(name=logName)
        self.setLevel(level)

        fmt = logging.Formatter(fmt=logFmt)

        if filename:
            fileHandler = logging.FileHandler(filename=filename, encoding="utf-8")
            fileHandler.setFormatter(fmt)
            fileHandler.setLevel(level)
            self.addHandler(fileHandler)

        consoleHandler = logging.StreamHandler()
        consoleHandler.setLevel(level)
        consoleHandler.setFormatter(fmt)
        self.addHandler(consoleHandler)


if __name__ == '__main__':

    log = LogHandler()
    log.critical("Criticalaaa")