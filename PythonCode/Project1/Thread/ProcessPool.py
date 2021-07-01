from multiprocessing import Pool
import os,time,random

def run_task(name):
    print('Task %s (pid = %s ) is running...' % (name,os.getpid()))
    time.sleep(random.random()*3)
    print('Task %s end.'%name)

if __name__ == "__main__":
    print('Current process %s .' % os.getpid())

    # 创建了容量为 3 的线程池 ， 也就是每次最多运行 3 个
    p = Pool(processes=3)
    for i in range(5):
        # 依次向 进程池中添加 5 个任务
        # apply_async是异步非阻塞式，不用等待当前进程执行完毕，
        # 随时跟进操作系统调度来进行进程切换，即多个进程并行执行，提高程序的执行效率
        # p.apply_async(run_task,args=(i,))

        # apply : 阻塞主进程, 并且一个一个按顺序地执行子进程,
        # 等到全部子进程都执行完毕后 ,继续执行 apply()后面主进程的代码
        p.apply(run_task,args=(i,))
    print('Waiting for all subprocesses done..')
    # 调用 close 之后就不能继续添加新的 Process 了
    p.close()
    # 调用 join 之前必须调用 close ，
    p.join()
    print('All processes done')