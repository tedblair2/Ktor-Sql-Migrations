package com.example.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Engine(
    @SerialName("cc")
    val cc: Int?,
    @SerialName("cylinders")
    val cylinders: Int?,
    @SerialName("fuel")
    val fuel: String?,
    @SerialName("power")
    val power: Int?
)