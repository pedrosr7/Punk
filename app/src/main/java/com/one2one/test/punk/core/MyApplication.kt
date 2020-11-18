package com.one2one.test.punk.core

import android.app.Application
import android.content.Context
import com.one2one.test.punk.di.retrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.one2one.test.punk.di.appModule

class MyApplication : Application() {

    override fun onCreate(){
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(appModule, retrofitModule)
        }

    }

    fun Context.application(): MyApplication = applicationContext as MyApplication
}