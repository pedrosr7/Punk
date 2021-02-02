package com.one2one.test.punk.di

import com.one2one.test.punk.core.MyApplication
import com.one2one.test.punk.core.managers.NetworkManager
import com.one2one.test.punk.data.datasource.RemoteDataSource
import com.one2one.test.punk.data.datasource.implementation.RemoteDataSourceImpl
import com.one2one.test.punk.data.repository.BeersRepositoryImpl
import com.one2one.test.punk.domain.repository.BeerRepository
import com.one2one.test.punk.domain.usecases.GetBeersUseCases
import com.one2one.test.punk.presentation.beer.viewmodel.BeersViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val appModule = module {

    single { MyApplication() }

    single {
        NetworkManager(
            androidContext()
        )
    }

    single<RemoteDataSource> { RemoteDataSourceImpl(get()) }
    single<BeerRepository> { BeersRepositoryImpl(get(), get()) }
    single { GetBeersUseCases(get()) }
    viewModel { BeersViewModel(get()) }

}

