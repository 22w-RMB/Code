import requests


class HttpHandler:
    def __init__(self):
        self.session = requests.session()

    def visit(self,url,method,params=None, data = None, json = None, **kwargs):

        # if method.lower() == 'get':
        #     res = self.session.get(url,params=params,**kwargs)
        # elif method.lower() == 'post':
        #     res = self.session.post(url,params=params,data=data,json=json,**kwargs)
        #
        res = self.session.request(method,url,params=params,data=data,json=json,**kwargs)

        try:
            return res.json()
        except ValueError:
            print("Not JSON")
