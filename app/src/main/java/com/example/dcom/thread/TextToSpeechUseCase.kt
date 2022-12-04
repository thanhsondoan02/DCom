package com.example.dcom.thread

import android.content.Context
import com.example.dcom.repo.RepositoryFactory

class TextToSpeechUseCase: BaseUseCase<TextToSpeechUseCase.TextToSpeechRV, Unit>() {
    override suspend fun execute(rv: TextToSpeechRV) {
        val repo = RepositoryFactory.getCommunicationRepo()
        repo.speak(rv.text, rv.context)
    }

    class TextToSpeechRV(val text: String, val context: Context) : RequestValue
}
