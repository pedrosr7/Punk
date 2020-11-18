package com.one2one.test.punk.core.plataform

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer<T> { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    override fun postValue(value: T?) {
        pending.set(true)
        super.postValue(value)
    }


    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null

    }

    /**
     * AS [call] but this method can be called from a background thread
     */
    fun callAsync() {
        postValue(null)
    }
}