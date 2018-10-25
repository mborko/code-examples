import os

os.system("sudo hcitool lescan > scan & sleep 6 && sudo pkill --signal=SIGINT hcitool")

with open("scan","r") as fh:
    print(fh.readlines())

