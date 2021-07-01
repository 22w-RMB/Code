def customer():
    print('--4、开始执行生成器代码')
    response = None
    while True:
        print('--5、yield，中断，保存上下文')
        n = yield response
        print('--8、获取上下文，继续往下执行')
        if not n:
            return
        print('[Customer]：consuming {}'.format(n))
        response='OK' + str(n)

def produce(c):
    print('--3、启动生成器，开始执行生成器customer')
    c.send(None)
    print("--6、继续往下执行")
    n = 0
    while n<5:
        n+=1
        print('[Producer]：producing {}'.format(n))
        print("--7、第 {} 次唤醒生成器，从 yield 位置继续往下执行！--".format(n+1))
        r = c.send(n)
        print("--9、第 8 步往下")
        print("[Producer]：customer return {}..".format(r))
    c.close()

if __name__=="__main__":
    c = customer()
    produce(c)