package com.example.db

import com.example.model.User
import com.example.model.Users
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserServiceImpl : UserService {

    private fun resultRowToUser(row: ResultRow):User{
        return User(
            id = row[Users.id],
            name = row[Users.name],
            address = row[Users.address],
            age = row[Users.age],
            schoolId = row[Users.schoolId],
            height = row[Users.height]
        )
    }

    override suspend fun addUser(user: User): User? = dbQuery{
        val insertStmt=Users.insert {
            it[name]= user.name
            it[address]= user.address
            it[schoolId]= user.schoolId
            it[age]= user.age
            it[height]= user.height
        }
        insertStmt.resultedValues?.singleOrNull()?.let { resultRowToUser(it) }
    }

    override suspend fun updateUser(user: User): Boolean = dbQuery{
        Users.update({Users.name eq user.name }){
           it[schoolId]= user.schoolId
           it[age]= user.age
           it[address]= user.address
            it[height]= user.height
        }>0
    }

    override suspend fun deleteUser(user: User): Boolean = dbQuery{
        Users.deleteWhere { name eq user.name }>0
    }

    override suspend fun getUsers(): List<User> = dbQuery{
        Users.selectAll().map { resultRowToUser(it) }
    }

    override suspend fun searchUser(query: String): List<User> = dbQuery{
        Users.select { (Users.name.lowerCase() like "%${query.lowercase()}%") or
                (Users.address.lowerCase() like "%${query.lowercase()}%")}
            .map { resultRowToUser(it) }
    }

    override suspend fun getUser(id: Int): User? = dbQuery{
        Users.select { (Users.id eq id) }.map { resultRowToUser(it) }.singleOrNull()
    }
}