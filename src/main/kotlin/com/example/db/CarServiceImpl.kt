package com.example.db

import com.example.model.CarItems
import com.example.model.Cars
import com.example.model.JsonNameExpression
import com.example.plugins.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.selectAll
@Serializable
data class Result(
    val car_item:String
)
class CarServiceImpl : CarService {

    private fun resultRowToCarItem(resultRow: ResultRow):CarItems{
        return CarItems(
            id=resultRow[Cars.id],
            attrs = resultRow[Cars.attr]
        )
    }
    override suspend fun getCars(): List<CarItems> = dbQuery{
        Cars.selectAll().map { resultRowToCarItem(it) }
    }

    override suspend fun getCar(path: String): List<Result> = dbQuery{
        Cars
            .slice(JsonNameExpression("attrs").alias("car_name"))
            .selectAll()
            .map {
                Result(it[JsonNameExpression("attrs").alias("car_name")])
            }
    }
}