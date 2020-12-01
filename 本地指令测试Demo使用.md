# 本地指令测试Demo使用

## 项目说明

用来演示和测试Rokid Glass眼镜本地语音指令的Demo App，同时为测试人员提供成组测试语音指令的相关功能。

## 测试使用说明
### 1. 安装Demo App
```
adb install VoiceInstructDemo.apk
// 项目中的 release/VoiceInstructDemo.apk
```
### 2. 准备测试指令配置

* 建立需要测试的多个txt文件，格式UTF-8。将想要测试的指令组写入单个文件，一行一个，没有空格，空行。如果在某些多音字要设置拼音的场景，
* 可以在指令名后面加上","，然后添加具体拼音，每个文字的拼音用空格间隔。
* 英文测试需要先将系统语音切换到英文，同时语音助手会将指令识别切换到英文引擎，将想要测试的指令组写入单个文件，一行一个，指令词的单词之间用空格间隔。

```shell
文件1：测试1.txt
内容：
上一页
下一页

文件2：测试2.txt
内容：
上一个
下一个

文件3：拼音测试3.txt
内容：
重力,zhong li
核心应用,he xin ying yong

文件4：英文测试4.txt
内容：
last page
next page
select item one

```

### 3. 将测试文件推入眼镜SD卡

将准备好的多个txt文件推送到硬件设备的SD卡glassOrderTest目录下，如果glassOrderTest目录不存在，需要先创建。

```shell
adb shell
cd sdcard
mkdir glassOrderTest
exit
adb push 测试1.txt /sdcard/glassOrderTest/
adb push 测试2.txt /sdcard/glassOrderTest/
...
```

### 4. 进行测试

1. 启动app，在首页中会出现glassOrderTest目录中测试文件相对应测试button，如”测试1.txt“会生成”测试1.txt“按钮，点击按钮，会进入指令测试页面；
2. 指令测试页面，用户语音测试文件内容中的指令名称，相应指令的触发会在当前页面UI中显示出来。
3. 如在首页点击”测试1.txt“按钮，进入”测试1.txt“指令测试页面，说出”上一页“、”下一页“触发本地语音指令，会在页面UI上以text相应记录。




