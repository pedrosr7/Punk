package com.one2one.test.punk.core.plataform

import arrow.core.Either
import com.one2one.test.punk.core.exceptions.MyFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class BaseParamsUseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<MyFailure, Type>

    open operator fun invoke(
        scope: CoroutineScope,
        params: Params,
        onResult: (Either<MyFailure, Type>) -> Unit = {}
    ) {
        val backgroundJob = scope.async { run(params) }
        scope.launch { onResult(backgroundJob.await()) }
    }

}