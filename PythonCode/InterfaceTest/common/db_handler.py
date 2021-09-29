

import pymysql
from pymysql.cursors import DictCursor

from common.python_handler import SqlConfig

class DBHandler():

    def __init__(self,
                 host = "8.129.91.152",
                 port = 3306,
                 user = "future",
                 password = "123456",
                 charset = "utf8",  # 不能是 utf-8
                 database = "futureloan",
                 cursorclass = DictCursor,  # 将获取到的游标以字典格式输出,
                 **kwargs):
        self.conn = pymysql.connect(host=host , port=port,
                       user = user, password=password,
                       charset=charset,  # 不能是 utf-8
                       database=database,cursorclass=cursorclass,**kwargs)
        self.cursor = self.conn.cursor()


    def query(self,sql, args=None, one=True):

        self.cursor.execute(sql,args=args)

        self.conn.commit()

        if one:
            return self.cursor.fetchone()
        else:
            return self.cursor.fetchall()

    def close(self):
        self.cursor.close()
        self.conn.close()


if __name__ == '__main__':
    db = DBHandler(host= SqlConfig.host , port=SqlConfig.port,
                       user = SqlConfig.user, password=SqlConfig.password,
                       charset=SqlConfig.charset,  # 不能是 utf-8
                       database=SqlConfig.database,cursorclass=SqlConfig.cursorclass)
    res = db.query("select id, reg_name from  member limit 2;",one=True)
    print(res)