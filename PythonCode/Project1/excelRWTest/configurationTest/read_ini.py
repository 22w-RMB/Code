
from configparser import ConfigParser

# 初始化
config = ConfigParser()
# 读取文件
config.read('iniTest.ini',encoding="utf-8")

a = config.get('teachers','name')
# 也可以通过类似字典的方式读取
# a = config['teachers']['name']
print(a)   # ['zzw','zzw1']
print(type(a))  # <class 'str'>

# 写入
config['teachers']['gender'] = "哈哈哈"
with open('iniTest.ini',encoding="utf-8",mode="w") as f:
    a = config.write(f)

# 如果 不存在这样的属性
if not config.has_section("selection"):
    config.add_section("selection")
# 第一个参数是 selection，第二个是 option， 第三个是 value
config.set("selection","option","value")

with open('iniTest.ini',encoding="utf-8",mode="w") as f:
    a = config.write(f)
