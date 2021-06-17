package com.tawa.allinapp.features.splash

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.AuthRepository
import javax.inject.Inject

class GetSession
@Inject constructor(private val authRepository: AuthRepository) : UseCase<Boolean, UseCase.None>() {

    override suspend fun run(params: None) = authRepository.userLoggedIn()

}