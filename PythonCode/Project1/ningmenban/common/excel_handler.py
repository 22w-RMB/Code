
import openpyxl
from openpyxl import Workbook
from openpyxl.worksheet.worksheet import Worksheet


class ExcelHandler:

    def __init__(self, filename):

        self.filename = filename

    def openSheet(self, sheetName) -> Worksheet:

        wb = openpyxl.load_workbook(filename=self.filename)
        return wb[sheetName]


    def getTitle(self, sheetName) -> list:

        sheet = self.openSheet(sheetName)
        list1 = []
        for cell in sheet[1]:
            list1.append(cell.value)

        return list1

    def readAllData(self, sheetName) -> list:

        sheet = self.openSheet(sheetName)
        titleList = self.getTitle(sheetName)
        rows = list(sheet.rows)[1:]
        allData = []
        for row in rows:

            cellList = []
            for cell in row:
                cellList.append(cell.value)

            dict1 = dict(zip(titleList, cellList))
            allData.append(dict1)

        return allData




if __name__ == '__main__':

    e = ExcelHandler(r"D:\code\PythonCode\Project1\ningmenban\resource\excel\cases.xlsx")
    data = e.readAllData("register")
    print(data)