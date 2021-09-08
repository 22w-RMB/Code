
class funA:

    def __init__(self,fun):
        self.fun = fun

    def __get__(self, instance, owner):
       return self.fun(instance)


class funB:

    @funA
    def fieldName(self):
        return 5

print(funA.fieldName)