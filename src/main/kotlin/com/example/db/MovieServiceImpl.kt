package com.example.db

import com.example.model.Movie
import com.example.model.MovieDTO
import com.example.model.movieToDTO
import com.example.plugins.dbQuery

class MovieServiceImpl : MovieService {

    override suspend fun addMovie(movieDTO: MovieDTO): MovieDTO = dbQuery{
        val movie=Movie.new {
            name=movieDTO.name
            releaseDate=movieDTO.releaseDate
            directorId=movieDTO.directorId
        }
        movie.movieToDTO()
    }

    override suspend fun updateMovie(movieDTO: MovieDTO) = dbQuery{
        val movie=Movie.findById(movieDTO.id)
        movie?.name=movieDTO.name
        movie?.directorId=movieDTO.directorId
        movie?.releaseDate=movieDTO.releaseDate
    }

    override suspend fun getAllMovies(): List<MovieDTO> = dbQuery{
        Movie.all().map { it.movieToDTO() }
    }
}