import time

from gevent import monkey
import gevent

def test(id):
    i = 0
    while True:
        time.sleep(0.5)
        print("我是：",id,"，当前 i 为： ",i)
        i += 1

monkey.patch_all()
gevent.joinall(
    [
        gevent.spawn(test,1),
        gevent.spawn(test,2),
        gevent.spawn(test,3)
    ]
)