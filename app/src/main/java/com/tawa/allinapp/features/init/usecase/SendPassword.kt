package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.AuthRepository
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.Schedule
import javax.inject.Inject

class SendPassword
@Inject constructor(private val authRepository: AuthRepository) : UseCase<Boolean, SendPassword.Params>() {

    override suspend fun run(params: Params) = authRepository.sendPassword(params.password)

    data class Params(val password:String)

}