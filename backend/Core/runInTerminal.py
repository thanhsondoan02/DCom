import cu_cu as cu
import json

linkSTT = ".\\backend\Core\Data\Text\HistoryList.json"
linkTTS = ".\\backend\Core\Data\Text\HistoryTTSList.json"

linkHistory = ".\\backend\Core\Data\Audio\History\\"


def one():
    while True:
        print("Mời bạn nói: ")
        cucu = cu.SpeechToText(linkSTT)
        output = cucu.SpeechText()
        print("Bạn nói: ", output)
        print("Bạn có muốn tiếp tục (Y/N)")
        status = input()
        if(status == 'N'): return
    return 
 
def two():
    while True:
        print("Mời nhập: ")
        _input = input()

        cucu = cu.TextToSpeech(linkHistory, linkTTS)
        
        print("Lắng nghe: ")
        cucu.TextSpeech(_input)

        print("Bạn có muốn tiếp tục (Y/N)")
        status = input()
        if(status == 'N'): return
    return 

def three():
    print("Bạn có muốn tiếp tục (Y/N)")
    status = input()
    if(status == 'N'): return
    return

def four():
    print("Bạn có muốn tiếp tục (Y/N)")
    status = input()
    if(status == 'N'): return
    return

def five():
    while True:
        print("History of STT")
        with open(linkSTT, encoding="utf-8") as fj:
            _input = json.load(fj)
        for i in range(len(_input)):
            date = _input[i]["date"]
            time = _input[i]["time"]
            content = _input[i]["content"]
            print("date: ", date, " time: ", time, " content: ", content)
        
        print("History of TTS")
        with open(linkTTS, encoding="utf-8") as fjj:
            __input = json.load(fjj)
        for i in range(len(__input)):
            date = __input[i]["date"]
            time = __input[i]["time"]
            content = __input[i]["content"]
            print("date: ", date, " time: ", time, " content: ", content)

        print("Bạn có muốn tiếp tục (Y/N):")
        status = input()
        if(status == 'N'): return
    return
 
while True:
    print("Danh sách các chứ năng:")
    print("1. Speech To Text")
    print("2. Text To Speech")
    print("3. Giao tiếp nhanh")
    print("4. Tuỳ chỉnh mẫu câu giao tiếp nhanh")
    print("5. Xem lại cuộc hội thoại")
    print("0. Thoát\n")
    print("Hay chọn một chức năng muốn dùng: ")

    status = input()
    if(status == '0'): break
    if status == '1': one()
    if status == '2': two()
    if status == '3': three()
    if status == '4': four()
    if status == '5': five()