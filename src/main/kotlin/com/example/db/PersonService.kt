package com.example.db

import com.example.model.Person

interface PersonService {
    suspend fun getPersons():List<Person>
    suspend fun addPerson(person: Person):Person?
    suspend fun updatePerson(person: Person):Boolean
    suspend fun deletePerson(person: Person):Boolean

    suspend fun personAge(id:Int):Int
}