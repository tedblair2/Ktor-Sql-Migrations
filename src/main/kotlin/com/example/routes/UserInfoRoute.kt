package com.example.routes

import com.example.db.SchoolService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get as get1

fun Routing.userinfoRoute(schoolService: SchoolService=get1()){
    route("/userinfo"){
        get {
            val userSchoolInfo=schoolService.getUserAndSchoolInfo()
            call.respond(HttpStatusCode.OK,userSchoolInfo)
        }
        get("/{id}"){
            call.parameters["id"]?.toInt()?.let {id->
                schoolService.getUserSchoolInfo(id)?.let {userInfo->
                    call.respond(HttpStatusCode.OK,userInfo)
                } ?: call.respond(HttpStatusCode.NotFound,"Info not found!!")
            } ?: call.respond(HttpStatusCode.BadGateway,"Id cannot be empty!!")
        }
        get("left") {
            val userSchoolInfo=schoolService.getUserInfoLeftJoin()
            call.respond(HttpStatusCode.OK,userSchoolInfo)
        }
        get("right") {
            val userSchoolInfo=schoolService.getUserInfoRightJoin()
            call.respond(HttpStatusCode.OK,userSchoolInfo)
        }
    }
}