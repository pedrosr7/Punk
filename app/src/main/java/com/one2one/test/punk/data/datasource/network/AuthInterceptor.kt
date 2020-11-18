package com.one2one.test.punk.data.datasource.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request
            .newBuilder()
            .build()
        return chain.proceed(request)
    }
}