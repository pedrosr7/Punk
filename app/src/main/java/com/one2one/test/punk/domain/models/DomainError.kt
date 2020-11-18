package com.one2one.test.punk.domain.models

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.one2one.test.punk.core.exceptions.DataPreferencesError
import com.one2one.test.punk.core.exceptions.MyFailure

sealed class DomainError {

    companion object {
        fun fromThrowable(e: Throwable): DomainError =
            when (e) {
                is MyFailure.NotFound -> NotFoundError
                is MyFailure.Unauthorized -> AuthenticationError
                is MyFailure.NetworkConnectionError -> NetworkConnectionError
                is DataPreferencesError.NotFoundData -> NotFoundDataError
                else -> UnknownServerError((Some(e)))
            }
    }

    object NetworkConnectionError : DomainError()
    object AuthenticationError : DomainError()
    object NotFoundDataError : DomainError()
    object NotFoundError : DomainError()
    data class UnknownServerError(val e: Option<Throwable> = None) : DomainError()
}