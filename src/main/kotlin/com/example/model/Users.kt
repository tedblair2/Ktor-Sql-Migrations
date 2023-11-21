package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val name:String,
    val address:String,
    val schoolId:Int,
    val age:Int,
    val height:Int,
    val id:Int=0,
):java.io.Serializable

object Users:Table(){
    val id=integer("id").autoIncrement()
    val name=varchar("name",255)
    val address=varchar("address",255)
    val schoolId=integer("schoolid").references(Schools.id,ReferenceOption.CASCADE)
    val age=integer("age")
    val height=integer("height").default(0)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}
