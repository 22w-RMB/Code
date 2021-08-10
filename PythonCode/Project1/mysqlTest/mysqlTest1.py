

import pymysql
from pymysql.cursors import DictCursor

conn = pymysql.connect(host="8.129.91.152" , port=3306,
                       user = "future", password="123456",
                       charset="utf8",  # 不能是 utf-8
                       database="futureloan",
                       cursorclass=DictCursor # 将获取到的游标以字典格式输出
                         )

print(conn)

cursor = conn.cursor()

cursor.execute("select * from member limit 0,10;")
one = cursor.fetchone()
all = cursor.fetchall()
print(one)
print(all)

cursor.execute("select * from member where mobile_phone=%s and id=%s;",args=[13323234514,2])
one = cursor.fetchone()
all = cursor.fetchall()
print(one)
print(all)

cursor.close()