import speech_recognition as sr

def SpeechToText():
    output = ""
    r = sr.Recognizer()

    with sr.Microphone() as source:
        # r.adjust_for_ambient_noise(source)
        audio = r.listen(source)

    try:
        text = r.recognize_google(audio,language="vi-VI")
        output = text
    except:
        output = "Không nhận diện được âm thanh."

    return output


print(SpeechToText())