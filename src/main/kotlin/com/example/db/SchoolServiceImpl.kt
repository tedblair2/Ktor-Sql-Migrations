package com.example.db

import com.example.model.School
import com.example.model.Schools
import com.example.model.UserSchoolInfo
import com.example.model.Users
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class SchoolServiceImpl : SchoolService {

    private fun resultRowToSchool(row: ResultRow):School{
        return School(
            id = row[Schools.id],
            schoolName = row[Schools.schoolName],
            schoolAddress = row[Schools.schoolAddress]
        )
    }

    override suspend fun getSchools(): List<School> = dbQuery {
        Schools.selectAll().map { resultRowToSchool(it) }
    }

    override suspend fun addSchool(school: School): School? = dbQuery {
        val insertStatement=Schools.insert {
            it[schoolName]=school.schoolName
            it[schoolAddress]=school.schoolAddress
        }
        insertStatement.resultedValues?.singleOrNull()?.let { resultRowToSchool(it) }
    }

    override suspend fun updateSchool(school: School): Boolean = dbQuery{
        Schools.update({Schools.schoolName eq school.schoolName}){
            it[schoolAddress]=school.schoolAddress
        }>0
    }

    override suspend fun deleteSchool(school: School): Boolean = dbQuery{
        Schools.deleteWhere { schoolName eq school.schoolName }>0
    }

    override suspend fun searchSchool(query: String): List<School> = dbQuery {
        Schools.select { (Schools.schoolName.lowerCase() like "%${query.lowercase()}%") or
                (Schools.schoolAddress.lowerCase() like "%${query.lowercase()}%")}
            .map { resultRowToSchool(it) }
    }

    override suspend fun getSchool(id: Int): School? = dbQuery{
        Schools.select { (Schools.id eq id) }.map { resultRowToSchool(it) }.singleOrNull()
    }

    override suspend fun getUserAndSchoolInfo(): List<UserSchoolInfo> = dbQuery {
        (Users innerJoin Schools)
            .slice(Users.name,Users.address,Users.age,Schools.schoolName,Schools.schoolAddress)
            .selectAll()
            .map {
                UserSchoolInfo(
                    name = it[Users.name],
                    address = it[Users.address],
                    age = it[Users.age],
                    schoolName = it[Schools.schoolName],
                    schoolAddress = it[Schools.schoolAddress]
                )
            }
    }

    override suspend fun getUserSchoolInfo(id: Int):UserSchoolInfo? = dbQuery {
        (Users innerJoin Schools)
            .slice(Users.name,Users.address,Users.age,Schools.schoolName,Schools.schoolAddress)
            .select { (Users.id eq id) }
            .map {
                UserSchoolInfo(
                    name = it[Users.name],
                    address = it[Users.address],
                    age = it[Users.age],
                    schoolName = it[Schools.schoolName],
                    schoolAddress = it[Schools.schoolAddress]
                )
            }.singleOrNull()
    }
}