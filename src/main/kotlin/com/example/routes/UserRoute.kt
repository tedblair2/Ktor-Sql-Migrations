package com.example.routes

import com.example.db.UserService
import com.example.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject
import org.postgresql.util.PSQLException

fun Routing.userRoute(){
    val storageFilepath=environment?.config?.property("storage.ehcacheFilePath")?.getString()
    val userService by inject<UserService> { parametersOf(storageFilepath) }
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
            }catch (e: ExposedSQLException){
                call.respond(HttpStatusCode.BadRequest,"Insert on table users violates foreign key constraint fk_users_school")
            }catch (e: PSQLException){
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
            }catch (e: ExposedSQLException){
                call.respond(HttpStatusCode.BadRequest,"Update on table users violates foreign key constraint fk_users_school")
            }catch (e: PSQLException){
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
}