package com.mezri.bigburger.data.network.dto

const val UNKNOWN_VALUE = "UNKNOWN"

interface DTOMapper<I, O> {
    fun map(input: I): O
}