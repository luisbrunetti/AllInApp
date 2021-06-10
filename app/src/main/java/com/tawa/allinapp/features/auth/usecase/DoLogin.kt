package com.tawa.allinapp.features.auth.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.AuthRepository
import javax.inject.Inject

class DoLogin
@Inject constructor(private val authRepository: AuthRepository) : UseCase<Boolean, DoLogin.Params>() {

    override suspend fun run(params: Params) = authRepository.login(params.username, params.password)

    data class Params(val username: String, val password: String)
}