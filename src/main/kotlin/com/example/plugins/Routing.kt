package com.example.plugins

import com.example.db.SchoolService
import com.example.db.UserService
import com.example.model.School
import com.example.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.get as get1
import org.koin.ktor.ext.inject
import org.postgresql.util.PSQLException

fun Application.configureRouting(schoolService: SchoolService=get1()) {
    val storageFilepath=environment.config.property("storage.ehcacheFilePath").getString()
    val userService by inject<UserService> { parametersOf(storageFilepath) }
    routing {
        route("/users"){
            get {
                val users=userService.getUsers()
                call.respond(HttpStatusCode.OK,users)
            }
            post {
                val user=call.receive<User>()
                try {
                    val result=userService.addUser(user)
                    result?.let {
                        call.respond(HttpStatusCode.Created,it)
                    } ?: call.respond(HttpStatusCode.NotImplemented,"Error adding user")
                }catch (e:ExposedSQLException){
                    call.respond(HttpStatusCode.BadRequest,"Insert on table users violates foreign key constraint fk_users_school")
                }catch (e:PSQLException){
                    call.respond(HttpStatusCode.BadRequest,"Insert on table users violates foreign key constraint fk_users_school")
                }
            }
            put{
                try {
                    val user=call.receive<User>()
                    val result=userService.updateUser(user)
                    if (result){
                        call.respond(HttpStatusCode.OK,"Update successful")
                    }else{
                        call.respond(HttpStatusCode.NotImplemented,"Update not done")
                    }
                }catch (e:ExposedSQLException){
                    call.respond(HttpStatusCode.BadRequest,"Update on table users violates foreign key constraint fk_users_school")
                }catch (e:PSQLException){
                    call.respond(HttpStatusCode.BadRequest,"Update on table users violates foreign key constraint fk_users_school")
                }
            }
            delete{
                val user=call.receive<User>()
                val result=userService.deleteUser(user)
                if (result){
                    call.respond(HttpStatusCode.OK,"Delete successful")
                }else{
                    call.respond(HttpStatusCode.NotImplemented,"Delete not done")
                }
            }
            get("/search"){
                val query=call.request.queryParameters["q"].toString()
                val users=userService.searchUser(query)
                call.respond(HttpStatusCode.OK,users)
            }
            get("/{id}") {
                val id=call.parameters["id"]?.toInt()
                id?.let {
                    userService.getUser(it)?.let {user->
                        call.respond(HttpStatusCode.OK,user)
                    } ?: call.respond(HttpStatusCode.NotFound,"User not found")
                } ?: call.respond(HttpStatusCode.BadGateway,"Provide Input!!")
            }
        }
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
        }
    }
}
