import os
from multiprocessing import Process

# 子进程要执行的代码
# 例子1：
def run_proc(name):
    print('Child process %s (%s) Running...'%(name,os.getpid()))

if __name__ == '__main__':
    print('Parent process %s.'%os.getpid())

    for i in range(10):
        p = Process(target=run_proc,args=(str(i)))
        print('Process will start')
        p.start()
        print('Process start')
    print("For End.")
    # join的作用就是线程同步，即主线程执行到join方法时，进入阻塞状态，
    # 然后开始执行子线程，一直等到其他的子线程执行结束，
    # 主线程再执行join方法后面的，直至主线程终止
    # join() 方法要在 start() 后面
    # 如上，主线程会先执行玩所有的 print('Process start') 和 print("For End.")，
    # 再去执行子线程
    p.join()
    print("Process End.")



# 例子2：
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
    for t in thread_list:
        t.join()

    print('主线程结束了！', threading.currentThread().name)
    print('一共用时：', time.time()-start_time)