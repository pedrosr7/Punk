package com.one2one.test.punk.domain.usecases

import arrow.Kind
import com.one2one.test.punk.core.Runtime
import com.one2one.test.punk.data.repository.getBeersRepository
import com.one2one.test.punk.domain.models.BeerListing

fun <F> Runtime<F>.getBeersUseCase(name: String?): Kind<F, BeerListing> =
    getBeersRepository(name)