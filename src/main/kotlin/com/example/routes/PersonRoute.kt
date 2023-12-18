package com.example.routes

import com.example.db.PersonService
import com.example.model.Person
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get as get1

fun Routing.personRoute(personService: PersonService=get1()){
    route("/person"){
        get {
            val persons=personService.getPersons()
            call.respond(persons)
        }
        post {
            val person=call.receive<Person>()
            personService.addPerson(person)?.let {
                call.respond(HttpStatusCode.Created,it)
            } ?: call.respond(HttpStatusCode.NotImplemented,"User not added!!")
        }
        put {
            val person=call.receive<Person>()
            val result=personService.updatePerson(person)
            if (result){
                call.respond(HttpStatusCode.OK,"Person Updated")
            }else{
                call.respond(HttpStatusCode.NotImplemented,"Person not updated!")
            }
        }
        delete {
            val person=call.receive<Person>()
            val result=personService.deletePerson(person)
            if (result){
                call.respond(HttpStatusCode.OK,"Person Deleted")
            }else{
                call.respond(HttpStatusCode.NotImplemented,"Person not deleted!")
            }
        }
        get ("/{id}"){
            val id=call.parameters["id"]?.toInt()
            id?.let {
                val age=personService.personAge(id)
                call.respond(HttpStatusCode.OK, mapOf("age" to age))
            } ?: call.respond(HttpStatusCode.BadRequest,"Please provide an Id!!")
        }
    }
}