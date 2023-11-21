package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class School(
    val schoolName:String,
    val schoolAddress:String,
    val id:Int=0
)

object Schools:Table(){
    val id=integer("id").autoIncrement()
    val schoolAddress=varchar("schooladdress",255)
    val schoolName=varchar("schoolname",255)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}
