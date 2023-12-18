package com.example.db

import com.example.model.CarItems

interface CarService {
    suspend fun getCars():List<CarItems>
    suspend fun getCar(path:String="hello"):List<Result>
}