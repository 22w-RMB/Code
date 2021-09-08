

import random

def generateMobile() -> str:

    # phone = "1" + random.choice(["3","5","7","8","9"])
    phone = "1" + random.choice(["3","5","7","8"])

    for i in range(9):
        phone += str(random.randint(1,9))

    return phone