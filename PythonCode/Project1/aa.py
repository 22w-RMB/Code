'''

序列化操作

'''

try:
    import cPickle as pickle
except ImportError:
    import pickle

d = dict(url='index.html',title="首页",content="首页内容")

# 序列化： dumps()，将任意对象序列化成一个str
xulieD = pickle.dumps(d)
print(xulieD)
# 反序列化： loads()
fanxuliehuaD = pickle.loads(xulieD)
print(fanxuliehuaD)


with open(r"D:\code\PythonCode\Project1\a.txt",'wb') as a:
    # 将序列化后的对象直接写入文件
    xuliehuaA = pickle.dump(d,a)
    print(xuliehuaA)

with open(r"D:\code\PythonCode\Project1\a.txt","rb") as a:
    # 将文件反序列化为对象
    fanxuliehuaA = pickle.load(a)
    print(fanxuliehuaA)