package com.one2one.test.punk.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import com.one2one.test.punk.core.exceptions.MyFailure
import com.one2one.test.punk.core.managers.NetworkManager
import com.one2one.test.punk.data.datasource.network.ApiService
import com.nhaarman.mockitokotlin2.whenever
import com.one2one.test.punk.data.datasource.RemoteDataSource
import com.one2one.test.punk.data.datasource.dto.NetworkBeer
import com.one2one.test.punk.data.repository.BeersRepositoryImpl
import com.one2one.test.punk.domain.models.Beer
import com.one2one.test.punk.domain.models.BeerListing
import com.one2one.test.punk.domain.repository.BeerRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class BeersRepositoryUnitTest {

    @Mock
    private lateinit var networkHandler: NetworkManager

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var beerRepository: BeerRepository

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        beerRepository = BeersRepositoryImpl(remoteDataSource, networkHandler)
    }

    @Test
    fun `get beers with internet should return beers`() {

        //GIVEN
        val response = Either.Right(mockBeerMap().beers)

        //WHEN
        whenever(networkHandler.isNetworkAvailable).thenReturn(true)

        whenever(
                runBlocking { remoteDataSource.getBeers("beer")}
        ).thenAnswer {
            response
        }

        //THEN
        runBlocking {
            beerRepository.getBeers(null)
            assertEquals(response, beerRepository.getBeers("beer"))
        }


    }

    @Test
    fun `get beers with wrong data should return server error`() {

        //GIVEN
        val response = Either.Left(MyFailure.InvalidObject)

        //WHEN
        whenever(networkHandler.isNetworkAvailable).thenReturn(true)


        whenever(
                runBlocking { remoteDataSource.getBeers("beer")}
        ).thenAnswer {
            response
        }

        //THEN
        runBlocking {
            beerRepository.getBeers(null)
            assertEquals(response, beerRepository.getBeers("beer"))
        }

    }

    @Test
    fun `get beers without internet should return error connection`() {

        //GIVEN
        val response = Either.Left(MyFailure.NetworkConnectionError)

        //WHEN
        whenever(networkHandler.isNetworkAvailable).thenReturn(false)


        //THEN
        runBlocking {
            beerRepository.getBeers(null)
            assertEquals(response, beerRepository.getBeers("beer"))
        }

    }


    private fun mockResponseBeer(): List<NetworkBeer> = listOf(
        NetworkBeer(1, "", "", "", ""),
        NetworkBeer(2, "", "", "", ""),
        NetworkBeer(3, "", "", "", ""),
        NetworkBeer(4, "", "", "", "")
    )

    private fun mockBeerMap(): BeerListing = BeerListing(listOf(
            Beer(1, "", "", "", ""),
            Beer(2, "", "", "", ""),
            Beer(3, "", "", "", ""),
            Beer(4, "", "", "", "")
    ))
    private fun mockBeerMap2(): BeerListing = BeerListing(listOf(
            Beer(1, "", "", "", ""),
            Beer(2, "", "", "", ""),
            Beer(3, "", "", "", "")
    ))

}