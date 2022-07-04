import os
import platform
import re
import sys

# run: 卸载与安装

os.system('./gradlew clean')
os.system('./gradlew assembleDebug')

apkPath = 'app/build/outputs/apk/debug/'
apkPackageNamePath = 'app/build.gradle'

with open(apkPackageNamePath, 'r', encoding='utf-8') as f:
    for line in f:
        if line.__contains__('applicationId'):
            packageName = re.compile(r'"(.*?)"').findall(line)[0]
            break

# print('uninstall ' + packageName)
# os.system('adb uninstall %s' % packageName)

for dirpath, dirnames, filenames in os.walk(apkPath):
    for filename in filenames:
        if filename.__contains__('.apk'):
            apkPath = apkPath + filename
            break

print('install ' + apkPath)
os.system('adb install -r %s' % apkPath)
os.system('adb shell am start -n com.yzc.bilibili/.arch.view.MainActivity')



