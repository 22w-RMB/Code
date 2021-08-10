# from aa import a


class Demo():
    token = ""
    i = 2


d = Demo()
d.i = 5
Demo.i = 4
print(Demo.i)