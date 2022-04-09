
from python_handler import ExcelConfig
from excel_handler import ExcelHandler
from logger_handler import loggerInstance

loggerInstance.info("hh")

excelHandler = ExcelHandler(ExcelConfig.excelAbsPath)
output = excelHandler.readSheetAllData(ExcelConfig.outputSheetName)
#rint(output[0]['申报价格'])
outputLen = len(output)

base = excelHandler.readSheetAllData(ExcelConfig.baseSheetName)
# print(base[1]['全网新能源消纳比例'] )






# number 为 96点数据，从 1 开始

#前一段
def  getFirstQ(price, number, pType) -> float:

    loggerInstance.info("======================================================当前模拟出清段数：" + str(number) )
    loggerInstance.info("时刻为:"   + str(base[number-1]['时刻'] ) )
    loggerInstance.info("申报功率为： " + str(base[number-1]['申报功率'])  )
    loggerInstance.info("全网新能源消纳比例： " + str(base[number-1]['全网新能源消纳比例'] ) )
    temp = 0
    if pType == "日前":
        loggerInstance.info("日前节点价格为： " + str(price))
        # 申报功率*全网新能源消纳比例
        temp = base[number - 1]['申报功率'] * base[number - 1]['全网新能源消纳比例']
    else:
        loggerInstance.info("实时节点价格为： " + str(price))
        # 预测实时出力
        temp = base[number - 1]['预测实时出力'] * 1


    # 出清价格 < P1
    if price < output[0]['申报价格']:

        loggerInstance.info(" 出清价格 < P1 ")
        loggerInstance.info(" 出力为：0 ")

        return 0

    # # 申报功率*全网新能源消纳比例
    # temp = base[number-1]['预测实时出力'] * base[number-1]['全网新能源消纳比例']

    # 出清价格 > P10
    if price > output[outputLen-1]['申报价格']:

        loggerInstance.info(" 出清价格  > P10 ")
        loggerInstance.info("出力为：" + str( output[outputLen-1]['终止出力']) )

        return min(output[outputLen-1]['终止出力'],temp)

    # 如果量价申报只有一段
    if outputLen == 1:

        loggerInstance.info(" 量价申报只有一段 ")
        loggerInstance.info("出力为：" + str(output[outputLen - 1]['终止出力']) )

        return min(output[outputLen - 1]['终止出力'], temp)

    # 出清价格 = P n = P n+t
    t = -1
    for i in range(0, outputLen):
        if price == output[i]['申报价格']:
            t = i

    if t != -1 :
        loggerInstance.info(" t： " + str(t) )
        loggerInstance.info(" 出清价格 = P n = P n+t ")
        loggerInstance.info("出力为：" + str(output[t]['终止出力']) )

        return min(output[t]['终止出力'], temp)


    # P n < 出清价格 < P n+1
    for i in range(0, outputLen-1):
        if price > output[i]['申报价格'] and price < output[i+1]['申报价格']:
            t = i
            break

    loggerInstance.info(" t： " + str(t))
    loggerInstance.info(" P n < 出清价格 < P n+1 ")
    loggerInstance.info("出力为：" + str(output[t]['终止出力']) )

    return min(output[t]['终止出力'], temp)

# 后一段
def dealQ(q, number,quantityList):
    if number == 1:
        return q

    if q > ( quantityList[number - 2] + speed * 15 ):
        q = quantityList[number - 2] + speed * 15
        print("路过")
        return q

    if q < ( quantityList[number - 2] - speed * 15 ):
        q = quantityList[number - 2] - speed * 15
        print("路过")
        return q

    return q

# 结合
def getQ(price, number, pType,quantityList):
    #前一段
    firstQ = getFirstQ(price, number, pType)
    #后一段
    resultQ= dealQ(firstQ, number,quantityList)
    return resultQ



# 计算日前出清电量
def getRecentlyQuantity(totalNumber) -> list:
    quantityList = []
    count = 0
    for number in range(1, totalNumber+1):
        #
        price = base[number - 1]["预测日前节点价格"]
        quantity = getQ(price, number, "日前",quantityList)
        quantityList.append(quantity)
        excelHandler.writeData(ExcelConfig.newSheetName, number+1, 2, str(quantity))
        if quantity == base[number - 1]["双边出清"]:
            count += 1

    if count == totalNumber:
        loggerInstance.info("双边出清电量预测结果正确")



    return  quantityList


# 计算实际出清电量
def getActualQuantity(totalNumber) -> list:
    quantityList = []
    count = 0

    for number in range(1, totalNumber+1):
        #
        price = base[number - 1]["预测实时节点价格"]
        quantity = getQ(price, number, "实时", quantityList)
        quantityList.append(quantity)
        excelHandler.writeData(ExcelConfig.newSheetName, number + 1, 3, str(quantity))
        if quantity == base[number - 1]["实时上网scada电力"]:
            count += 1
        else:
            errorL.append({"时刻":base[number - 1]["时刻"],"电量":quantity,"实际":base[number - 1]["实时上网scada电力"]})


    if count == totalNumber:
        loggerInstance.info("实时上网scada电力预测正确")
        print("实时上网scada电力预测正确")
    else:
        print(count)

    return  quantityList





print("------------------------")

errorL = []

# 爬坡速率
speed = 48*0.03

totalNumber = 96

# 获得
l1 = getRecentlyQuantity(totalNumber)
l2 = getActualQuantity(totalNumber)

# print("日前")
print(l1)

print("------------------------")
print("------------------------")

print("实时")
print(l2)
