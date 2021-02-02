package com.one2one.test.punk.core.plataform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.Failure

abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()

    fun handleFailure(failure: Failure) {
        this.failure.postValue(failure)
    }

}