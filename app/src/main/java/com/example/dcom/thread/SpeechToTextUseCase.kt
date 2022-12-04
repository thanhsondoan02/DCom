package com.example.dcom.thread

import com.example.dcom.repo.RepositoryFactory

class SpeechToTextUseCase: BaseUseCase<BaseUseCase.VoidRequest, String>() {
    override suspend fun execute(rv: VoidRequest): String {
        val repo = RepositoryFactory.getCommunicationRepo()
        return repo.listen()
    }
}
