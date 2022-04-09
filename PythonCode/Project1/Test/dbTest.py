import pymysql
from pymysql.cursors import DictCursor


class DBHandler:

    def __init__(self,host,port,user,password,database,charset="utf8",cursorclass=DictCursor):

        self.conn = pymysql.connect(host=host,port=port,user=user,password=password,database=database,charset=charset,cursorclass=cursorclass)

        self.cursor = self.conn.cursor()

    def execSql(self,sql=None, args=None, isOne=True):

        self.cursor.execute(query=sql, args=args)
        self.conn.commit()

        if isOne:
            return self.cursor.fetchone()

        return self.cursor.fetchall()

    def close(self):
        self.cursor.close()
        self.conn.close()



