package com.example.plugins

import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        userRoute()
        schoolRoute()
        userinfoRoute()
        directorRoutes()
        movieRoute()
        cartRoute()
        personRoute()
        carRoute()
    }
}
