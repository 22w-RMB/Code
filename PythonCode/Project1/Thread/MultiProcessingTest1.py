

'''
多线程例子3：
    当我们使用 setDaemon(True)方法，也就是设置子线程为守护进程后，
    主线程一旦执行结束，则全部线程被终止执行，
    可能出现的情况就是，子线程还没完全执行结束，就被迫停止
    setDaemon() 方法一定要在 线程.start()方法前

'''

import threading
import time

def run():
    time.sleep(2)
    print('当前的线程名字是：', threading.current_thread().name)
    time.sleep(2)

if __name__ == '__main__':
    start_time = time.time()
    print('这是主线程：', threading.currentThread().name)
    thread_list = []
    for i in range(5):
        t = threading.Thread(target=run)
        thread_list.append(t)

    for t in thread_list:
        t.setDaemon(True)
        t.start()

    print('主线程结束了！', threading.currentThread().name)
    print('一共用时：', time.time()-start_time)