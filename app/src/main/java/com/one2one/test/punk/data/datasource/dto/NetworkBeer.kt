package com.one2one.test.punk.data.datasource.dto

import com.google.gson.annotations.SerializedName

data class NetworkBeer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("abv")
    val degree: String
)