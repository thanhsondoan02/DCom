package com.example.dcom.thread

import android.content.Context
import com.example.dcom.repo.RepositoryFactory

class SpeechToTextUseCase: BaseUseCase<SpeechToTextUseCase.SpeechToTextRV, String>() {
    override suspend fun execute(rv: SpeechToTextRV): String {
        val repo = RepositoryFactory.getCommunicationRepo()
        return repo.listen(rv.context)
    }

    class SpeechToTextRV(val context: Context) : RequestValue

}
