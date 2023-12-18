package com.example.routes

import com.example.db.CarService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get as get1

fun Routing.carRoute(carService: CarService=get1()){
    route("/car"){
        get {
            val carName=carService.getCar()
            call.respond(carName)
        }
    }
}