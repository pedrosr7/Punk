package com.one2one.test.punk.data.datasource.mapper

import com.one2one.test.punk.core.exceptions.MyFailure


fun Int.toNetworkError() = when (this) {
    401 -> MyFailure.Unauthorized
    else -> MyFailure.ServerError
}

fun Throwable.normalizeError() = when (this) {
    is MyFailure -> this
    else -> MyFailure.ServerError
}