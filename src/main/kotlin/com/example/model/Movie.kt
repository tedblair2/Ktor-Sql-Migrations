package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Movies:IntIdTable(){
    val name=varchar("name",255)
    val release_date=integer("release_date")
    val director_id=integer("director_id")
}

class Movie(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<Movie>(Movies)
    var name by Movies.name
    var releaseDate by Movies.release_date
    var directorId by Movies.director_id
}
@Serializable
data class MovieDTO(
    val name:String,
    val releaseDate:Int,
    val directorId:Int,
    val id:Int=0,
)

fun Movie.movieToDTO():MovieDTO{
    return MovieDTO(
        id = id.value,
        name = name,
        releaseDate=releaseDate,
        directorId = directorId
    )
}
