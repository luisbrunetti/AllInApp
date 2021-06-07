package com.tawa.allinapp.core.functional

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object UnauthorizedError: Failure()
    data class DefaultError(val message: String?): Failure()
    data class MessageEmptyError(val message: String?): Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()
}
