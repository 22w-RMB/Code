

'''

    读取 yaml

'''

import yaml

# 读取 yaml 配置，加载配置项
with open("yamlTest.yaml", encoding="utf-8") as f:
    data = yaml.load(f, Loader=yaml.FullLoader)
    print(data)


data = {'name': '中文'}
# 写入 yaml 配置，加载配置项，写入中文需要添加参数 allow_unicode=True
with open("yamlTest.yaml","w", encoding="utf-8") as f:
    yaml.dump(data, stream=f, allow_unicode=True)

