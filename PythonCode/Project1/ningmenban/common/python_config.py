
import os
from datetime import datetime

projectPath = os.path.dirname(os.path.dirname(__file__))
print(projectPath)
logPath = os.path.join(os.path.join(projectPath, "output"), "log")
print(logPath)
time = datetime.now().strftime("%Y-%m-%d,%H:%M:%S")
print(time)
logFileName = "log-" + time + ".txt"
print(logFileName)