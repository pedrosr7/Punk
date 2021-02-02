package com.one2one.test.punk.data.datasource.implementation

import arrow.core.Either
import com.one2one.test.punk.core.exceptions.MyFailure
import com.one2one.test.punk.data.datasource.RemoteDataSource
import com.one2one.test.punk.data.datasource.mapper.toDomain
import com.one2one.test.punk.data.datasource.network.ApiService
import com.one2one.test.punk.domain.models.BeerListing

class RemoteDataSourceImpl constructor(
    private val apiService: ApiService
): RemoteDataSource {
    override suspend fun getBeers(name: String?): Either<MyFailure, BeerListing> {
        val response = apiService.fetchBeers(name)
        return if(response.isSuccessful) {
            try {
                Either.Right(BeerListing(response.body()!!.map { it.toDomain() }))
            } catch (e: Exception) {
                Either.Left(MyFailure.InvalidObject)
            }

        } else {
            Either.Left(MyFailure.ServerError)
        }
    }

}