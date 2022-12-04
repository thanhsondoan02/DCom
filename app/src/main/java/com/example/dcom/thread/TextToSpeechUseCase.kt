package com.example.dcom.thread

import com.example.dcom.repo.RepositoryFactory

class TextToSpeechUseCase: BaseUseCase<TextToSpeechUseCase.SendGiftRV, Unit>() {
    override suspend fun execute(rv: SendGiftRV) {
        val repo = RepositoryFactory.getCommunicationRepo()
        repo.speak(rv.text)
    }

    class SendGiftRV(val text: String) : RequestValue {
    }
}
