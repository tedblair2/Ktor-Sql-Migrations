package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Directors:IntIdTable(){
    val name=varchar("name",255)
}

class Director (id : EntityID<Int>) : IntEntity(id) {
    companion object:IntEntityClass<Director>(Directors)
    var name by Directors.name
}
@Serializable
data class DirectorDto(
    val name:String,
    val id:Int=0,
)
fun Director.directorToDTO():DirectorDto{
    return DirectorDto(id = id.value, name = name)
}