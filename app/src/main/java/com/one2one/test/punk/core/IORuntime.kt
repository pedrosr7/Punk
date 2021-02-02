package com.one2one.test.punk.core

import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.io.concurrent.concurrent
import arrow.fx.typeclasses.Concurrent
import com.one2one.test.punk.core.managers.NetworkManager
import kotlinx.coroutines.CoroutineDispatcher
import com.one2one.test.punk.data.datasource.network.ApiService
import kotlinx.coroutines.Dispatchers


/**
 * This context contains the program dependencies. It can potentially work over any data type F that
 * supports concurrency, or in other words, any data type F that there's an instance of concurrent
 * Fx for.
 */
@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class Runtime<F>(
    concurrent: Concurrent<F>,
    val context: RuntimeContextKoin
) : Concurrent<F> by concurrent

fun IO.Companion.runtime(ctx: RuntimeContextKoin) = object : Runtime<ForIO>(IO.concurrent(), ctx) {}

data class RuntimeContextKoin(
    val bgDispatcher: CoroutineDispatcher = Dispatchers.IO,
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    val apiService: ApiService,
    val networkManager: NetworkManager
)