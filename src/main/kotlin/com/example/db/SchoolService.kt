package com.example.db

import com.example.model.School
import com.example.model.UserSchoolInfo

interface SchoolService {
    suspend fun getSchools():List<School>
    suspend fun addSchool(school: School):School?
    suspend fun updateSchool(school: School):Boolean
    suspend fun deleteSchool(school: School):Boolean
    suspend fun searchSchool(query:String):List<School>
    suspend fun getSchool(id:Int):School?
    suspend fun getUserAndSchoolInfo():List<UserSchoolInfo>
    suspend fun getUserSchoolInfo(id: Int):UserSchoolInfo?
}