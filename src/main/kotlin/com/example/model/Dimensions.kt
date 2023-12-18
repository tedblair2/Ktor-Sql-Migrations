package com.example.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dimensions(
    @SerialName("GroundClearance")
    val groundClearance: Int?,
    @SerialName("height")
    val height: Int?,
    @SerialName("length")
    val length: Int?,
    @SerialName("width")
    val width: Int?
)