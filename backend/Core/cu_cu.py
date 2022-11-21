from gtts import gTTS

import playsound
import os
import time
import json

import speech_recognition as sr

'''
Cu cu thông thường (danh pháp hai phần: Cuculus canorus), còn gọi là đại đỗ quyên (大杜鹃), 
bố cốc (布谷), quách công, là một loài chim thuộc Chi Cu cu, họ Cuculidae. Loài chim qua 
tiết cốc vũ (hết mùa Xuân) mới kêu, qua tiết hạ chí (qua giữa Hè) rồi thôi, tiếng như giục
người cấy lúa, nên gọi là chim bố cốc. Loài này là một loài di cư rộng lớn vào mùa hè sang
châu Âu và châu Á, và mùa đông ở châu Phi. Nó là một loài bố mẹ ký sinh, đẻ nhờ vào tổ loài
chim khác, đặc biệt là Prunella modularis, Anthus pratensis và Acrocephalus scirpaceus.
'''


'''
# Lấy ra thời gian hiện tại
# @param input non
# @return thời gian hiện tại, ngày tháng năm hiện tại
'''
def getTime():
    localtime = time.asctime(time.localtime(time.time()))

    _day = localtime[8:10]
    _month = localtime[4:7]
    _year = localtime[20:24]
    _time = localtime[11:19]
    __time = localtime[11:13] + "-" + localtime[14:16] + "-" + localtime[17:19]

    date = _day + " " + _month + " " + _year

    return _time, date, _day, _month, _year, __time

class TextToSpeech:
    
    '''
    # Khởi tạo
    # @param input linkOut
    # @param input linkTextData
    # @param input lang: ngôn ngữ
    # @return non
    '''
    def __init__(self, linkOut, linkHisList, lang = "vi"):
        self.linkOut = linkOut
        self.linkHisList = linkHisList
        self.lang = lang

    '''
    # Chuyển văn bản thành âm thanh
    # @param input text: văn bản cần đọc
    # @return (phát ra âm thanh khi gọi hàm)
    '''
    def TextSpeech(self, content):

        present = getTime()
        _link = present[2] + "-" + present[3] + "-" + present[4] + "-" + present[5] + ".mp3"

        output = gTTS(content, lang = self.lang, slow=False)
        output.save(_link)

        linkOut = self.linkOut + _link
        output.save(linkOut)

        self.saveTTS(content, _link)

        playsound.playsound(_link, True)
        os.remove(_link)

        return output

    def SpeechFromFile(self, fileLink):
        playsound.playsound(fileLink, True)
        return

    '''
    # Lưu lại câu vừa nhập
    # @param input 
    # @param input 
    # @return non
    '''
    def saveTTS(self, content, link):
        with open(self.linkHisList, encoding="utf-8") as fj:
            _input = json.load(fj)

        present = getTime()

        dataaaa = {
            "date": present[1],
            "time": present[0],
            "content": content,
            "filename": link,
        }

        _input.append(dataaaa)


        dataaa = json.dumps(_input, indent=2, ensure_ascii=False)

        myjsonfile = open(self.linkHisList, "w", encoding="utf-8")
        myjsonfile.write(dataaa)
        myjsonfile.close()
        return


class SpeechToText:

    '''
    Khởi tạo
    # @param input linkHisList: Dường dẫn tới vị trí lưu lịch sử câu
    # @param input languge: ngôn ngữ
    # @return non
    '''
    def __init__(self, linkHisList, language = "vi-VI"):
        self.language = language
        self.linkHisList = linkHisList
    
    '''
    Chuyển âm thanh thành văn bản
    # @param input micro: âm thanh
    # @return văn bản được nhận viện
    '''
    def SpeechText(self):
        output = ""
        r = sr.Recognizer()
        print("Nói: ")
        with sr.Microphone() as source:
            audio = r.listen(source)
        try:
            text = r.recognize_google(audio, language = self.language)
            output = text
            self.saveSTT(output)
        except:
            output = "Không nhận diện được âm thanh."
            return output
        return output

    '''
    Lưu lịch sử hội thoại
    # @param content đoạn hội thoại
    # @return non
    '''
    def saveSTT(self, content):
        with open(self.linkHisList, encoding="utf-8") as fj:
            _input = json.load(fj)

        dataaaa = {
            "date": getTime()[1],
            "time": getTime()[0],
            "content": content,
        }

        _input.append(dataaaa)

        # print(_input)

        dataaa = json.dumps(_input, indent=2, ensure_ascii=False)

        myjsonfile = open(self.linkHisList, "w", encoding="utf-8")
        myjsonfile.write(dataaa)
        myjsonfile.close()
        return

class GiaoTiepNhanh:

    def __init__(self, link):
        self.link = link

    def loadFile(self):
        _input = ""
        with open(self.link, encoding="utf-8") as fj:
            _input = json.load(fj)
        return _input
    
    def writeFile(self, _input):
        dataaa = json.dumps(_input, indent=2, ensure_ascii=False)

        myjsonfile = open(self.link, "w", encoding="utf-8")
        myjsonfile.write(dataaa)
        myjsonfile.close()

    def addTopic(self, topic):
        _input =  self.loadFile()
        dataaaa = {
            "topic": topic,
            "content": [],
            "link": [],
        }

        _input.append(dataaaa)

        self.writeFile(_input)

    def searchTopic(self, topic):
        _input = self.loadFile()
        for i in range(len(_input)):
            _topic = _input[i]["topic"]
            if _topic == topic:
                return i

    def delTopic(self, locationTopic):
        _input = self.loadFile()
        
        _input.pop(locationTopic)

        self.writeFile(_input)

        return

    def moveTopic(self, topic):
        return
    
    def addContent(self, topic, content):
        _input = self.loadFile()
        for i in range(len(_input)):
            _topic = _input[i]["topic"]
            if _topic == topic:
                _input[i]["content"].append(content)
                _input[i]["link"].append(content + ".mp3")

        self.writeFile(_input)
        return

    def searchAllContent(self, content):
        lsOut = {}
        _input = self.loadFile()

        for i in range(len(_input)):
            temp = _input[i]["content"]
            for j in range(len(temp)):
                if temp[j] == content:
                    lsOut.update({i:j})

        return lsOut

    def searchContentByTopic(self, topic, content):
        _input = self.loadFile()
        for i in range(len(_input)):
            _topic = _input[i]["topic"]
            if _topic == topic:
                temp = _input[i]["content"]
                for j in range(len(temp)):
                    if temp[j] == content:
                        return {i:j}

    def delLocationContent(self, locationTopic, locationContent):
        _input = self.loadFile()
        
        _input[locationTopic]['content'].pop(locationContent)
        _input[locationTopic]['link'].pop(locationContent)

        self.writeFile(_input)

        return

    def delContent(self, topic, content):
        
        return
    
    def moveContent(self, topic, content):
        return
    
    def setup(self, topic):
        _input =  self.loadFile()

        ls = ["Có chuyện gì vậy", "Tôi ổn", "Cảm ơn", "Không vấn đề gì cả", "Đừng lo lắng", "Tôi phải đi đây"]
        lsL = ["Có chuyện gì vậy.mp3", "Tôi ổn.mp3", "Cảm ơn.mp3", "Không vấn đề gì cả.mp3", "Đừng lo lắng.mp3", "Tôi phải đi đây.mp3"]

        dataaaa = {
            "topic": topic,
            "content": ls,
            "link": lsL,
        }

        _input.append(dataaaa)

        # print(_input)

        self.writeFile(_input)
        return 
