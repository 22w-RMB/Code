# '''
#
# def funA(fn):
#     print("C语言中文网")
#     fn() # 执行传入的fn参数
#     print("http://c.biancheng.net")
#     return "装饰器函数的返回值"
#
# def funB():
#     print("学习 Python")
#
# funB = funA(funB)
#
# 与下面等价
# '''
#
#
# #funA 作为装饰器函数
# def funA(fn):
#     print("C语言中文网")
#     fn() # 执行传入的fn参数
#     print("http://c.biancheng.net")
#     return "装饰器函数的返回值"
#
# @funA
# def funB():
#     print("学习 Python")
#
# print(funB)
#
#
# '''
# 带参构造器
# '''
# def funA(fn):
#     # 定义一个嵌套函数
#     def say(*args,**kwargs):
#         fn(*args,**kwargs)
#     return say
#
# @funA
# def funB(arc):
#     print("C语言中文网：",arc)
#
# @funA
# def other_funB(name,arc):
#     print(name,arc)
# funB("http://c.biancheng.net")
# other_funB("Python教程：","http://c.biancheng.net/python")

from flask import Flask

app = Flask(__name__)
@app.route("/login")
def login():
    # print("登录")
    return "登录"