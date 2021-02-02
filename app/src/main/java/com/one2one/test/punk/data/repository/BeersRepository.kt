package com.one2one.test.punk.data.repository

import arrow.Kind
import com.one2one.test.punk.core.Runtime
import com.one2one.test.punk.core.exceptions.MyFailure
import com.one2one.test.punk.data.datasource.loadBeers
import com.one2one.test.punk.domain.models.BeerListing

fun <F> Runtime<F>.getBeersRepository(name: String?): Kind<F, BeerListing> = fx.concurrent {
    if(context.networkManager.isNotNetworkAvailable) !raiseError<BeerListing>(MyFailure.NetworkConnectionError)

    !loadBeers(name)
}
