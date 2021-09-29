
import  logging


class LogHandler(logging.Logger):

    def __init__(self, filename, level, fmtStr, name = "ROOT"):

        super().__init__(name)
        self.setLevel(level)
        fmt = logging.Formatter(fmt=fmtStr)

        if filename:
            fileHandler = logging.FileHandler(filename, encoding="utf-8")
            fileHandler.setLevel(level)
            fileHandler.setFormatter(fmt)
            self.addHandler(fileHandler)

        streamHandler = logging.StreamHandler()
        streamHandler.setFormatter(fmt)
        streamHandler.setLevel(level)
        self.addHandler(streamHandler)
        print("=====================================")


if __name__ == '__main__':
    path = r"D:\code\PythonCode\Project1\ningmenban\output\log\log.txt"
    fmtStr = "%(asctime)s-%(name)s-%(levelname)s  %(filename)s-%(lineno)d-%(message)s"

    log = LogHandler(filename=path, fmtStr=fmtStr, level="DEBUG")

    log.info("哈哈哈")

    pass

