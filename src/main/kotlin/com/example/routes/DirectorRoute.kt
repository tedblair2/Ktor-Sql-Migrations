package com.example.routes

import com.example.db.DirectorService
import com.example.model.DirectorDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get as get1

fun Routing.directorRoutes(directorService: DirectorService=get1()){
    route("director"){
        get {
            val directors=directorService.getDirectors()
            call.respond(HttpStatusCode.OK,directors)
        }
        post {
            val director=call.receive<DirectorDto>()
            val result=directorService.addDirector(director)
            call.respond(HttpStatusCode.OK,result)
        }
        put {
            val director=call.receive<DirectorDto>()
            directorService.updateDirector(director)
            call.respond(HttpStatusCode.OK, mapOf("message" to "Update successful"))
        }
    }
}