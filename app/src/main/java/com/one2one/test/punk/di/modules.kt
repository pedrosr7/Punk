package com.one2one.test.punk.di

import com.one2one.test.punk.core.MyApplication
import com.one2one.test.punk.core.RuntimeContextKoin
import com.one2one.test.punk.core.managers.NetworkManager
import com.one2one.test.punk.presentation.beer.viewmodel.BeersViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { MyApplication() }

    single {
        RuntimeContextKoin(Dispatchers.IO, Dispatchers.Main, get(), get())
    }

    single {
        NetworkManager(
            androidContext()
        )
    }

    viewModel {
        BeersViewModel(
            get()
        )
    }

}

