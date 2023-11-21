package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSchoolInfo(
    val name:String,
    val address:String,
    val age:Int,
    val schoolName:String,
    val schoolAddress:String
)
