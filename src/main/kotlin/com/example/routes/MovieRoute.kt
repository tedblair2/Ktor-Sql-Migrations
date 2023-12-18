package com.example.routes

import com.example.db.MovieService
import com.example.model.MovieDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get as get1

fun Routing.movieRoute(movieService: MovieService=get1()){
    route("movie"){
        get {
            val movies=movieService.getAllMovies()
            call.respond(HttpStatusCode.OK,movies)
        }
        post {
            val movie=call.receive<MovieDTO>()
            val result=movieService.addMovie(movie)
            call.respond(HttpStatusCode.OK,result)
        }
        put {
            val movie=call.receive<MovieDTO>()
            movieService.updateMovie(movie)
            call.respond(HttpStatusCode.OK, mapOf("message" to "Update successful"))
        }
    }
}