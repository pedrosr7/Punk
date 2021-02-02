package com.one2one.test.punk.domain.usecases

import arrow.core.Either
import com.one2one.test.punk.core.exceptions.MyFailure
import com.one2one.test.punk.core.plataform.BaseParamsUseCase
import com.one2one.test.punk.domain.models.BeerListing
import com.one2one.test.punk.domain.repository.BeerRepository

class GetBeersUseCases(
    private val beerRepository: BeerRepository
): BaseParamsUseCase<BeerListing, String?>() {
    override suspend fun run(params: String?): Either<MyFailure, BeerListing> = beerRepository.getBeers(params)
}