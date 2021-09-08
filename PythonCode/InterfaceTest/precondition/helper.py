import json

from common.db_handler import DBHandler
from common.random_phone import generateMobile
from common.request_handler import RequestHandler
from common.python_handler import OtherConfig, SqlConfig
import jsonpath
import re

class Context:

    @property
    def token(self):
        res = login(OtherConfig.preconditionData)
        # jsonpath ==> 专门用来解析 json 的路径工具
        # print(res)
        token = jsonpath.jsonpath(res, "$..token")[0]
        tokenType = jsonpath.jsonpath(res, "$..token_type")[0]

        return " ".join([tokenType, token])

    @property
    def member_id(self):
        res = login(OtherConfig.preconditionData)
        return jsonpath.jsonpath(res, "$..id")[0]

    @property
    def loan_id(self):

        db = DBHandler(host= SqlConfig.host , port=SqlConfig.port,
                       user = SqlConfig.user, password=SqlConfig.password,
                       charset=SqlConfig.charset,  # 不能是 utf-8
                       database=SqlConfig.database,cursorclass=SqlConfig.cursorclass)
        loan = db.query("select * from loan where status=2  order by id desc limit 1;")
        # 一定要关闭
        print(loan)
        db.close()
        return loan['id']

    @property
    def admin_token(self):
        res = login(OtherConfig.adminData)
        # print(res)
        token = jsonpath.jsonpath(res, "$..token")[0]
        tokenType = jsonpath.jsonpath(res, "$..token_type")[0]

        return " ".join([tokenType, token])

    @property
    def admin_id(self):
        res = login(OtherConfig.adminData)
        return jsonpath.jsonpath(res, "$..id")[0]

    @property
    def user_balance(self):
        res = login(OtherConfig.preconditionData)
        return jsonpath.jsonpath(res, "$..leave_amount")[0]


def register(isNeedRegister=True):
    db = DBHandler(host=SqlConfig.host, port=SqlConfig.port,
                   user=SqlConfig.user, password=SqlConfig.password,
                   charset=SqlConfig.charset,  # 不能是 utf-8
                   database=SqlConfig.database, cursorclass=SqlConfig.cursorclass)
    phone = ""
    while True:
        new_mobile = generateMobile()
        print("新生成的手机号码为：%s" % new_mobile)
        isExist = db.query("select * from  member where mobile_phone=%s limit 1;",args=[new_mobile,])
        if not isExist:
            print("该号码未注册")
            phone = new_mobile
            if isNeedRegister == False:
                db.close()
                return { "mobile_phone": phone,"pwd": "12345678"}

            break;
    db.close()

    OtherConfig.registerData['mobile_phone'] = phone
    session =RequestHandler()
    res = session.visit(url=OtherConfig.registerUrl,method="post",
                        headers=OtherConfig.preconditionHeader,json=OtherConfig.registerData)
    session.close()
    print(res)
    if res['code'] == 0:
        return OtherConfig.registerData
    else:
        raise Exception("注册失败")



def login(data):

    session = RequestHandler()
    res = session.visit(url=OtherConfig.loginUrl,method="post",
                        headers=OtherConfig.preconditionHeader,json=data)
    session.close()
    return res


def recharge(amount=0):
    print("============ Preapre To Recharge ============")
    headers = OtherConfig.preconditionHeader.copy()
    headers['Authorization'] = Context().token
    OtherConfig.rechargeData['member_id'] = Context().member_id
    session = RequestHandler()
    if amount != 0:
        OtherConfig.rechargeData['amount'] = amount
    print(OtherConfig.rechargeData)
    res = session.visit(method="post", url=OtherConfig.rechargeUrl, headers=headers, json=OtherConfig.rechargeData)
    session.close()
    amount = jsonpath.jsonpath(res,"$..leave_amount")[0]
    print("============ Recharge Success! User's leave_amount is %.2f ============" % amount)
    # 充值完再把 充值的金额置为 默认金额，即6300
    OtherConfig.rechargeData['amount'] = OtherConfig.addLoanAmount
    return amount


def addLoan():
    print("============ Preapre To Add Laon ============")
    # 将请求头深复制
    headers = OtherConfig.preconditionHeader.copy()
    # 深复制出来的请求头添加 Authorization 属性，即 token
    headers['Authorization'] = Context().token
    # 修改默认配置的 “新增项目” 接口的请求数据中的 member_id，即用户的 id
    # 配置中该接口的 请求数据是 字典格式，所以直接替换
    OtherConfig.addLoanData['member_id'] = Context().member_id
    # 获取已经封装好的请求对象
    session = RequestHandler()
    # 执行请求操作
    res = session.visit(method="post",url = OtherConfig.addLoanUrl,headers=headers,json= OtherConfig.addLoanData)
    # 关闭会话
    session.close()
    # 获取当前接口响应体的 id，即新增项目的 id
    loan_id = jsonpath.jsonpath(res,"$..id")[0]
    print("============ Add Loan Success! loan_id is %d ============" % loan_id)
    return loan_id


def verifyLoan(loan_id):
    print("============ Preapre To Verify loan_id：%d ============"%loan_id)
    # 将请求头深复制
    headers = OtherConfig.preconditionHeader.copy()
    # 深复制出来的请求头添加 Authorization 属性，即 token
    headers['Authorization'] = Context().admin_token
    # 该接口中的请求数据是字符串格式，所以用 replace
    data = OtherConfig.verifyData.replace("#loan_id#",str(loan_id))
    # 获取已经封装好的请求对象
    session = RequestHandler()
    # 执行请求操作
    res = session.visit(method="patch", url=OtherConfig.verifyUrl, headers=headers, json= json.loads(data))
    # 关闭会话
    session.close()
    # 判断接口请求是否成功， 不成功则抛出异常
    if res['code'] == 0:
        print("============ Verify Success! ============")
    else:
        raise Exception("审核不通过")

# 将 新增项目 和 审核 函数 放在一个 函数中，方便投资用例调用
def addAndVerifyLoan() -> dict:
    id = addLoan()
    verifyLoan(id)
    # 用字典格式保存 新增项目的 id 和 借款金额
    infoDict = {"id":id,"amount":OtherConfig.addLoanAmount}
    return infoDict

def replaceLabel(target):
    print("============ Preapre To Replace Request Data ============")
    # 正则表达式
    patternStr = "#(.*?)#"
    # re.findall 返回的是一个列表，保存匹配到的结果，即 #member_id#
    while re.findall(pattern=patternStr,string=target):
        # re.search().group(1)：返回的事 member_id，即正则表达式中括号中的内容
        strr = re.search(pattern=patternStr,string=target).group(1)
        # print(strr)
        # 替换 匹配到的结果，也包括两个 # ，也会被替换掉，即替换的是  #member_id# 这一整串，
        # 而不是正则表达式中括号内的，即 member_id
        # repl ： a 替换成 b， 在这里的 b 就是 repl，a 就是 string
        # 例如我想把 #member_id# 换成 100，100 就是 repl
        # string：要被替换的字符串，即 匹配正则表达式的 字符串，如包含 #member_id# 的字符串
        # count：表示替换的个数，1表示替换匹配到正则表达式的第一个的结果
        target = re.sub(pattern=patternStr,repl=str(getattr(Context(),strr)),string=target,count=1)

    print("============ Replace Request Data Success!============")
    print("============ Request Data is %s ============" % target)
    return target



if __name__ == '__main__':

    # save_token()
    # print(Context().loan_id)
    # strs = '{"member_id":"#member_id#","loan_id":"#loan_id#","amount":400.00}'
    # print(replaceLabel(strs))
    # a = Context().loan_id
    # print(Context().token)
    # a = Context().loan_id
    # print(Context().token)
    # a = Context().loan_id
    # print(Context().token)
    # id = addLoan()
    # print(id)
    # verifyLoan(id)
    # recharge()
    # print(register())

    recharge()
    recharge(50)
    recharge()
    pass