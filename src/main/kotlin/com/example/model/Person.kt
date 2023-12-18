@file:OptIn(ExperimentalSerializationApi::class)
@file:UseSerializers(LocalDateSerializer::class)

package com.example.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

@Serializable
data class Person(
    val name:String,
    val dateOfBirth:LocalDate,
    val id:Int=0
)

object Persons:Table(){
    val id=integer("id").autoIncrement()
    val name=varchar("name",255)
    val dateOfBirth=date("date_of_birth")
}

class LocalDateSerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }
}
