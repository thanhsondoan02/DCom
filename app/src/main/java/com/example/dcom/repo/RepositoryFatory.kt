package com.example.dcom.repo

object RepositoryFactory {

    private val authRepoImpl = CommunicationRepoImpl()

    fun getCommunicationRepo(): ICommunicationRepo {
        return authRepoImpl
    }
}
