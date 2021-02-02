package com.one2one.test.punk.data.datasource.network

import com.one2one.test.punk.data.datasource.dto.NetworkBeer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("beers")
    fun fetchBeers(
        @Query("beer_name") name: String? = null
    ): Call<List<NetworkBeer>>

}