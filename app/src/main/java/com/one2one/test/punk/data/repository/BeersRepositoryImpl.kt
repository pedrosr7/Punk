package com.one2one.test.punk.data.repository

import arrow.core.Either
import com.one2one.test.punk.core.exceptions.MyFailure
import com.one2one.test.punk.core.managers.NetworkManager
import com.one2one.test.punk.data.datasource.RemoteDataSource
import com.one2one.test.punk.domain.models.BeerListing
import com.one2one.test.punk.domain.repository.BeerRepository

class BeersRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val networkManager: NetworkManager
): BeerRepository {
    override suspend fun getBeers(name: String?): Either<MyFailure, BeerListing> {
        return when (networkManager.isNetworkAvailable) {
            true -> remoteDataSource.getBeers(name)
            false -> Either.Left(MyFailure.NetworkConnectionError)
        }
    }
}