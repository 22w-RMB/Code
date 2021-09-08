
import pymysql
from pymysql.cursors import DictCursor

connect = pymysql.connect(host= "8.129.91.152",
                          port= 3306,
                          user= "future",
                          password= "123456",
                          database= "futureloan",
                          charset='utf8',
                          cursorclass=DictCursor)

cursor = connect.cursor()
cursor.execute("select * from loan where status = 2 order by id desc limit 1;")
print(cursor.fetchall())
# print(cursor.fetchone())


# import requests
# session = requests.session()
# session.request()