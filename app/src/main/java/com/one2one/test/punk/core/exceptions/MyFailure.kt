package com.one2one.test.punk.core.exceptions

sealed class MyFailure: Throwable() {
    object NotFound: MyFailure()
    object ServerError: MyFailure()
    object Unauthorized: MyFailure()
    object NetworkConnectionError: MyFailure()
}