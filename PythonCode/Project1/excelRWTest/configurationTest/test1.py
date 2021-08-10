


'''

    Python 的配置文件的形式
    1、python 模块，py 文件作为配置的文件
    2、yaml 文件， .yaml， .yml
    3、.ini 文件， .conf文件， .xml文件

'''

'''存储配置项'''
'''
    如果有多个环境、例如生产环境等等
'''

import sys

class LoggerConfig:
    logger_name = 'loggerName'
    logger_file = "log.txt"
    level = "DEBUG"

# 多种环境可以通过类继承的方式
class ProductLoggerConfig(LoggerConfig):
    level = "WARNING"

# 判断操作系统
if sys.platform == "linux":
    config = ProductLoggerConfig()
else:
    config = LoggerConfig()
