package com.one2one.test.punk.presentation.beer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.Kind
import arrow.fx.IO
import arrow.fx.extensions.io.unsafeRun.runNonBlocking
import arrow.unsafe
import com.one2one.test.punk.core.*
import com.one2one.test.punk.core.plataform.SingleLiveEvent
import com.one2one.test.punk.domain.models.Beer
import com.one2one.test.punk.domain.models.StateView
import com.one2one.test.punk.domain.usecases.getBeersUseCase

class BeersViewModel(
    runtimeContext: RuntimeContextKoin
) : ViewModel(), BeersView {

    val showLoading = SingleLiveEvent<Boolean>()
    val showErrorObserver = SingleLiveEvent<Throwable>()

    private val _beers: MutableLiveData<List<Beer>> by lazy {
        MutableLiveData<List<Beer>>()
    }
    val observerBeers: MutableLiveData<List<Beer>> = _beers

    private val runtime = IO.runtime(runtimeContext)

    fun getBeers(name: String?) {
        unsafe { runNonBlocking({
            runtime.getBeers(
                name = name,
                view = this@BeersViewModel
            )
        }, {}) }
    }

    override fun displayBeers(beers: List<Beer>) {
        _beers.postValue(beers)
    }

    override fun displayErrors(t: Throwable) {
        showErrorObserver.postValue(t)
    }

    override fun showLoading() {
        showLoading.postValue(true)
    }

    override fun hideLoading() {
        showLoading.postValue(false)
    }

}

fun <F> Runtime<F>.getBeers(name: String?, view: BeersView): Kind<F, Unit> = fx.concurrent {
    !effect { view.showLoading() }

    val maybe = !getBeersUseCase(name).attempt()

    !effect { view.hideLoading() }
    !effect {
        maybe.fold(
            ifLeft = { view.displayErrors(it) },
            ifRight = { view.displayBeers(beers = it.beers) }
        )
    }
}

interface BeersView : StateView {
    fun displayBeers(beers: List<Beer>)
    fun displayErrors(t: Throwable)
}