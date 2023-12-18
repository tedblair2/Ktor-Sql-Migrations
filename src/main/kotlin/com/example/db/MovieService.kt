package com.example.db

import com.example.model.MovieDTO

interface MovieService {
    suspend fun addMovie(movieDTO: MovieDTO):MovieDTO
    suspend fun updateMovie(movieDTO: MovieDTO)
    suspend fun getAllMovies():List<MovieDTO>
}