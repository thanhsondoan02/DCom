from gtts import gTTS
import playsound
import os

def TextToSpeech(text):
    name = text + ".mp3"
    output = gTTS(text, lang="vi", slow=False)
    output.save(name)
    linkOut = ".\\algorithm\Core\soundData\\" + name
    output.save(linkOut)
    playsound.playsound(name, True)
    os.remove(name)
    return output

TextToSpeech("Một con vịt yêu hai con mèo Một con vịt yêu hai con mèo Một con vịt yêu hai con mèo Một con vịt yêu hai con mèo Một con vịt yêu hai con mèo Một con vịt yêu hai con mèo")