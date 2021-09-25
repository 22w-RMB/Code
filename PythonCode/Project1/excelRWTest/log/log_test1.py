

'''
    官网描述：
    https://docs.python.org/zh-cn/3/library/logging.html

    logging ： 标准库

    日志级别：大写
    1、 NOSET ， 0 ， 等于没写
    2、 DEBUG ， 10 ， 调试，一些额外信息，备注，往往和主体功能无关
    3、 INFO , 20 , 主体功能信息， 类似 日报，做了些啥？
    4、 WARNING ， 30 ， 警告，下次可能要出错了
    5、 ERROR ， 40 ， 出错
    6、 CRITICAL ， 50 ， 极其严重

    完整的用例： (日志显示的级别取决于 “日志收集器”和“日志处理器”中的最高的级别)
    1、日志收集器 logger   ： 类似于 日记本
    2、日记收集器级别 level
    3、日志处理器准备 handler  ： 不同记号的笔
    4、日志处理器级别设置
    5、设置日志格式 format
    6、添加日志处理器 ：logger.addHandler(handler)

    格式：（最常用的）
    1、%(levelname)s ：是打印的日志的级别
    2、%(filename)s ：日志模块的名字
    3、%(lineno)d ：打印日志的行号
    4、%(message)s ：是打印的日志信息
    5、%(name)s ： 是收集器的名字
    6、%(asctime)s ： 什么时候运行的，时间

'''


import logging

# 参数：表示日记的名字
logger = logging.getLogger("生活")

# 设置日志级别，要记录什么级别及其以上的级别的日志
# 默认级别是 warining
logger.setLevel("CRITICAL")
#
#  日志处理器默认是使用控制台输出 ，也可以设置级别
#  如下是放到一个 file 文件中
handler = logging.FileHandler('log.txt')
handler.setLevel("WARNING")
#  handler 格式
#  name： 是收集器的名字
#  levelname：是打印的日志的级别
#  message：是打印的日志信息
file_format = logging.Formatter(style="{",fmt="{asctime}:{name}:{levelname}:{filename}:{lineno}:{message}",
                                datefmt="%Y/%m/%d %H:%M:%S")
handler.setFormatter(file_format)
logger.addHandler(handler)

#  如下日志处理器是设置控制台输出
console_output = logging.StreamHandler()
console_output.setLevel("ERROR")
console_fmt = logging.Formatter(fmt="%(asctime)s-%(name)s-%(levelname)s-%(message)s",datefmt="%Y/%m/%d %H:%M:%S")
console_output.setFormatter(console_fmt)
logger.addHandler(console_output)

logger.info("info")
logger.debug("debug")
logger.warning("warning")
logger.error("error")
logger.critical("critical")