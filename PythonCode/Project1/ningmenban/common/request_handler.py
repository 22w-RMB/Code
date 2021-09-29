
import requests


class RequestHandler:

    def __init__(self):
        self.session = requests.session()
        print(self.__class__.__name__, "sssssaf")


    def visit(self,method, url,headers=None,params=None, json=None,data=None,**kwargs):

        response = self.session.request(method=method,
                                        url=url,
                                        headers=headers,
                                        params=params,
                                        json=json,
                                        data=data,
                                        **kwargs)

        try:
            return response.json()
        except:
            print("该接口的响应结果不是  JSON 格式")


    def close(self):
        self.session.close()



if __name__ == '__main__':
    r = RequestHandler()
    # r.visit()