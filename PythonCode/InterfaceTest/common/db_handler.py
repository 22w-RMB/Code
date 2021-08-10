

import pymysql
from common.python_handler import SqlConfig

class DBHandler():

    def __init__(self,host , port, user, password, charset, database, cursorclass, **kwargs):
        self.conn = pymysql.connect(host=host , port=port,
                       user = user, password=password,
                       charset=charset,  # 不能是 utf-8
                       database=database,cursorclass=cursorclass,**kwargs)
        self.cursor = self.conn.cursor()


    def query(self,sql, args=None, one=True):

        self.cursor.execute(sql,args=args)

        if one:
            return self.cursor.fetchone()
        else:
            return self.cursor.fetchall()

    def close(self):

        self.cursor.close()


if __name__ == '__main__':
    db = DBHandler(host= SqlConfig.host , port=SqlConfig.port,
                       user = SqlConfig.user, password=SqlConfig.password,
                       charset=SqlConfig.charset,  # 不能是 utf-8
                       database=SqlConfig.database,cursorclass=SqlConfig.cursorclass)
    res = db.query("select * from  member where mobile_phone=%s",args=[135])
    print(res)