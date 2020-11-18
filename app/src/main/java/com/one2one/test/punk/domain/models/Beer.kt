package com.one2one.test.punk.domain.models

import java.io.Serializable

data class Beer(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val degree: String
): Serializable, Comparable<Beer> {
    override fun compareTo(other: Beer): Int =
        name.compareTo(other.name)
}