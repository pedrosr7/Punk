package com.one2one.test.punk.core.exceptions

sealed class DataPreferencesError : Throwable() {
    object NotFoundData : DataPreferencesError()
}