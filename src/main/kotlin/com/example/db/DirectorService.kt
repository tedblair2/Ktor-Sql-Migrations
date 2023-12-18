package com.example.db

import com.example.model.DirectorDto

interface DirectorService {
    suspend fun addDirector(directorDto: DirectorDto):DirectorDto
    suspend fun updateDirector(directorDto: DirectorDto)
    suspend fun getDirectors():List<DirectorDto>
    suspend fun deleteDirector(directorDto: DirectorDto)
}