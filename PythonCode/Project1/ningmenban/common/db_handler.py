import pymysql
from pymysql.cursors import DictCursor


class DBHandler:

    def __init__(self, host= "8.129.91.152",
                 port = 3306,
                 user = "future",
                 password = "123456",
                 charset = "utf8",
                 database = "futureloan",
                 cursorclass = DictCursor,
                 **kwargs):

        self.conn = pymysql.connect(host=host , port=port,
                       user = user, password=password,
                       charset=charset,  # 不能是 utf-8
                       database=database,cursorclass=cursorclass,**kwargs)
        self.cursor = self.conn.cursor()




    def execSql(self, sql, args = None, isOne = True):

        self.cursor.execute(query= sql, args=args)
        self.conn.commit()

        if isOne:
            return self.cursor.fetchone()

        return self.cursor.fetchall()

    def close(self):
        self.cursor.close()
        self.conn.close()

if __name__ == "__main__":
    db = DBHandler()
    res = db.execSql(sql="select id, reg_name from  member limit 2;", isOne=True)
    print(res)
    pass