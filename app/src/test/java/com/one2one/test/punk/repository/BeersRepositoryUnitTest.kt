package com.one2one.test.punk.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.io.applicativeError.attempt
import arrow.fx.extensions.io.concurrent.concurrent
import arrow.fx.extensions.io.unsafeRun.runBlocking
import arrow.unsafe
import com.one2one.test.punk.core.Runtime
import com.one2one.test.punk.core.RuntimeContextKoin
import com.one2one.test.punk.core.exceptions.MyFailure
import com.one2one.test.punk.core.managers.NetworkManager
import com.one2one.test.punk.data.datasource.mapper.toDomain
import com.one2one.test.punk.data.datasource.network.ApiService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.one2one.test.punk.data.datasource.dto.NetworkBeer
import com.one2one.test.punk.data.repository.getBeersRepository
import com.one2one.test.punk.domain.models.BeerListing
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import me.jorgecastillo.hiroaki.eq
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class BeersRepositoryUnitTest {

    private lateinit var runtime: Runtime<ForIO>// let's fix F to IO in tests also.

    @Mock
    private lateinit var networkHandler: NetworkManager

    @Mock
    private lateinit var apiService: ApiService

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {

        val testDispatcher = TestCoroutineDispatcher()
        val runtimeContext = RuntimeContextKoin(
            testDispatcher,
            testDispatcher,
            apiService,
            networkHandler
        )

        runtime = object : Runtime<ForIO>(IO.concurrent(), runtimeContext) {}
    }

    @Test
    fun `get beers with internet should return beers`() {

        //GIVEN
        val response = Response.success(mockResponseBeer())

        val mockCall = mock<Call<List<NetworkBeer>>> {
            on { execute() } doReturn response
        }

        //WHEN
        whenever(runtime.context.networkManager.isNotNetworkAvailable).thenReturn(false)

        whenever(
            runtime.context.apiService.fetchBeers()
        ).thenAnswer {
            mockCall
        }

        //THEN
        unsafe {

            val data = runBlocking {
                runtime.getBeersRepository(null)
            }

            data.beers eq  BeerListing(response.body()!!.map { it.toDomain() }).beers
        }

    }

    @Test
    fun `get beers with wrong data should return server error`() {

        //GIVEN
        val response = Response.success(BeerListing(listOf()))

        val mockCall = mock<Call<BeerListing>> {
            on { execute() } doReturn response
        }

        //WHEN
        whenever(runtime.context.networkManager.isNotNetworkAvailable).thenReturn(false)

        whenever(
            runtime.context.apiService.fetchBeers()
        ).thenAnswer {
            mockCall
        }

        //THEN
        unsafe {

            val res: Either<Throwable, BeerListing> = runBlocking {
                val op = runtime.getBeersRepository(null).attempt()
                op.map {
                    it.fold(
                        ifLeft = { error -> error.left() },
                        ifRight = { success -> success.right() }
                    )
                }
            }

            res eq MyFailure.ServerError.left()
        }

    }

    @Test
    fun `get beers without internet should return error connection`() {

        //WHEN
        whenever(runtime.context.networkManager.isNotNetworkAvailable).thenReturn(true)

        //THEN
        unsafe {

            val res: Either<Throwable, BeerListing> = runBlocking {
                val op = runtime.getBeersRepository(null).attempt()
                op.map {
                    it.fold(
                        ifLeft = { error -> error.left() },
                        ifRight = { success -> success.right() }
                    )
                }
            }

            res eq MyFailure.NetworkConnectionError.left()
        }

    }

    private fun mockResponseBeer(): List<NetworkBeer> = listOf(
        NetworkBeer(1, "", "", "", ""),
        NetworkBeer(2, "", "", "", ""),
        NetworkBeer(3, "", "", "", ""),
        NetworkBeer(4, "", "", "", "")
    )

}