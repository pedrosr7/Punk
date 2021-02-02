package com.one2one.test.punk.domain.repository

import arrow.core.Either
import com.one2one.test.punk.core.exceptions.MyFailure
import com.one2one.test.punk.data.datasource.dto.NetworkBeer
import com.one2one.test.punk.domain.models.BeerListing
import retrofit2.Response

/**
 *
 * Handles beer request
 *
 */
interface BeerRepository {

    /**
     * Function that get beers by name
     *
     * @param name Beer name
     *
     */
    suspend fun getBeers(name: String?): Either<MyFailure, BeerListing>

}