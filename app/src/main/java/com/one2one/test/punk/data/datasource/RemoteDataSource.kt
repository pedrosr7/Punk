package com.one2one.test.punk.data.datasource

import arrow.Kind

import com.one2one.test.punk.core.Runtime
import com.one2one.test.punk.data.datasource.mapper.normalizeError
import com.one2one.test.punk.data.datasource.mapper.toDomain
import com.one2one.test.punk.data.datasource.mapper.toNetworkError
import com.one2one.test.punk.domain.models.BeerListing

fun <F> Runtime<F>.loadBeers(name: String?): Kind<F, BeerListing> = fx.concurrent {

    val response = !effect(context.bgDispatcher) {
        fetchBeers(name)
    }
    continueOn(context.mainDispatcher)

    if (response.isSuccessful) {
        BeerListing(response.body()!!.map { it.toDomain() })
    } else {
        !raiseError<BeerListing>(response.code().toNetworkError())
    }
}.handleErrorWith { error ->
    raiseError(error.normalizeError())
}

private fun <F> Runtime<F>.fetchBeers(name: String?) =
    context.apiService.fetchBeers(name).execute()
