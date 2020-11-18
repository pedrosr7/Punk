package com.one2one.test.punk.data.datasource.mapper

import com.one2one.test.punk.data.datasource.dto.NetworkBeer
import com.one2one.test.punk.domain.models.Beer

fun NetworkBeer.toDomain() = Beer(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl ?: "",
    degree = degree
)
