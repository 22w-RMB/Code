
import requests
import json

class RequestHandler:
    def __init__(self):
        self.session = requests.session()

    def visit(self,method,url, params=None, data=None, headers=None,json = None,**kwargs):

        res = self.session.request(method=method,url=url, params=params, data=data, headers=headers,json = json,**kwargs)

        try:
            return res.json()
        except:
            print("Not Json")

    def close(self):
        self.session.close()