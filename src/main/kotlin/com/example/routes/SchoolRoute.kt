package com.example.routes

import com.example.db.SchoolService
import com.example.model.School
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get as get1

fun Routing.schoolRoute(schoolService: SchoolService=get1()){
    route("/schools"){
        get {
            val schools=schoolService.getSchools()
            call.respond(HttpStatusCode.OK,schools)
        }
        post {
            val school=call.receive<School>()
            schoolService.addSchool(school)?.let {
                call.respond(HttpStatusCode.Created,it)
            } ?: call.respond(HttpStatusCode.NotImplemented,"Error adding school")
        }
        put {
            val school=call.receive<School>()
            val result=schoolService.updateSchool(school)
            if (result){
                call.respond(HttpStatusCode.OK,"Update successful")
            }else{
                call.respond(HttpStatusCode.NotImplemented,"Update not done")
            }
        }
        delete {
            val school=call.receive<School>()
            val result=schoolService.deleteSchool(school)
            if (result){
                call.respond(HttpStatusCode.OK,"Delete successful")
            }else{
                call.respond(HttpStatusCode.NotImplemented,"Delete not done")
            }
        }
        get("/search") {
            val query=call.request.queryParameters["q"].toString()
            val schools=schoolService.searchSchool(query)
            call.respond(HttpStatusCode.OK,schools)
        }
        get("/{id}") {
            val id=call.parameters["id"]?.toInt()
            id?.let {
                schoolService.getSchool(id)?.let {school->
                    call.respond(HttpStatusCode.OK,school)
                } ?: call.respond(HttpStatusCode.NotFound,"School not found")
            } ?: call.respond(HttpStatusCode.BadGateway,"Provide Input!!")
        }

    }
}