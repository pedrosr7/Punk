package com.one2one.test.punk.di

import com.one2one.test.punk.BuildConfig.API_PUNK
import com.one2one.test.punk.data.datasource.network.ApiService
import com.one2one.test.punk.data.datasource.network.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {

    single {
        retrofit(API_PUNK)  // 4
    }

    single {
        get<Retrofit>().create(ApiService::class.java)   // 5
    }

}

private fun retrofit(baseUrl: String) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(httpClient)
    .build()

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

private val httpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .addNetworkInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()
}