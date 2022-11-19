from gtts import gTTS

import playsound
import os
import time
import json

import speech_recognition as sr
import cu_cu as cc

cucu = cc.TextToSpeech(".\Core\Data\Audio\TempCom\\", ".\Core\Data\Text\HistoryTTSList.json")

cucu.TextSpeech("Có chuyện gì vậy")
cucu.TextSpeech("Tôi ổn")
cucu.TextSpeech("Cảm ơn")
cucu.TextSpeech("Không vấn đề gì cả")
cucu.TextSpeech("Đừng lo lắng")
cucu.TextSpeech("Tôi phải đi đây")


# cucu.addTC("Giao tiếp cơ bản")

