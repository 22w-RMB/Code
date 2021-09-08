
import logging

logger = logging.getLogger("生活")

logger.setLevel("CRITICAL")


consoleHandler = logging.StreamHandler()
consoleHandler.setLevel("CRITICAL")

fmt = logging.Formatter(fmt="%(asctime)s-%(filename)s: %(lineno)d-%(name)s-%(message)s")
consoleHandler.setFormatter(fmt)

fileHandler = logging.FileHandler(filename="log.txt")
filFmt = logging.Formatter(fmt="%(asctime)s-%(filename)s: %(lineno)d-%(name)s-%(message)s")
fileHandler.setFormatter(filFmt)

logger.addHandler(consoleHandler)
logger.addHandler(fileHandler)
logger.critical("还好")

%(asctime)s - %(filename)s - %(levelname)s - %(lineno)d - %(name)s - %(message)s