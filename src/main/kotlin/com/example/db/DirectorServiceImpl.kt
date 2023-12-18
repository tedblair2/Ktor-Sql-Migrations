package com.example.db

import com.example.model.Director
import com.example.model.DirectorDto
import com.example.model.Directors
import com.example.model.directorToDTO
import com.example.plugins.dbQuery

class DirectorServiceImpl : DirectorService {
    override suspend fun addDirector(directorDto: DirectorDto): DirectorDto = dbQuery{
        val director=Director.new {
            name=directorDto.name
        }
        director.directorToDTO()
    }

    override suspend fun updateDirector(directorDto: DirectorDto) = dbQuery{
        val director=Director.findById(directorDto.id)
        director?.name=directorDto.name
    }

    override suspend fun getDirectors(): List<DirectorDto> = dbQuery{
        Director.all().map {
            it.directorToDTO()
        }
    }

    override suspend fun deleteDirector(directorDto: DirectorDto) = dbQuery{
        Director.find { (Directors.name eq directorDto.name) }.singleOrNull()!!.delete()
    }
}