package com.example.db

import com.example.model.Person
import com.example.model.Persons
import com.example.plugins.dbQuery
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PersonServiceImpl : PersonService {

    private fun resultRowToPerson(resultRow: ResultRow):Person{
        return Person(
            name = resultRow[Persons.name],
            id = resultRow[Persons.id],
            dateOfBirth = resultRow[Persons.dateOfBirth]
        )
    }
    override suspend fun getPersons(): List<Person> = dbQuery{
        Persons.selectAll().map { resultRowToPerson(it) }
    }

    override suspend fun addPerson(person: Person): Person? = dbQuery{
        val insertStatement=Persons.insert {
            it[name]=person.name
            it[dateOfBirth]=person.dateOfBirth
        }
        insertStatement.resultedValues?.singleOrNull()?.let { resultRowToPerson(it) }
    }

    override suspend fun updatePerson(person: Person): Boolean = dbQuery{
        Persons.update ({Persons.name eq person.name}){
            it[dateOfBirth]=person.dateOfBirth
        }>0
    }

    override suspend fun deletePerson(person: Person): Boolean = dbQuery{
        Persons.deleteWhere { name eq person.name }>0
    }

    override suspend fun personAge(id: Int): Int = dbQuery{
        val person=Persons.select { (Persons.id eq id) }
            .map { resultRowToPerson(it) }
            .singleOrNull()
        person?.let {
            val now=Clock.System.now()
            val today=now.toLocalDateTime(TimeZone.currentSystemDefault()).date
            val age=it.dateOfBirth.until(today,DateTimeUnit.YEAR)
            age
        }?: 0
    }
}