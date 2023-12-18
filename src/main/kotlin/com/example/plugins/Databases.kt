package com.example.plugins

import com.example.model.ItemIndexes
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureDatabases() {
    val driverClass=environment.config.property("storage.driverClassName").getString()
    val jdbcUrl=environment.config.property("storage.jdbcURL").getString()
    val hikariDataSource= createHikariDataSource(jdbcUrl,driverClass)
    Flyway.configure().dataSource(hikariDataSource).load().migrate()
    Database.connect(hikariDataSource)
}

private fun createHikariDataSource(url:String,driver:String): HikariDataSource {
    val hikariConfig= HikariConfig().apply {
        driverClassName=driver
        jdbcUrl=url
        maximumPoolSize=3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    return HikariDataSource(hikariConfig)
}

suspend fun <T> dbQuery(block:suspend ()->T):T{
    return newSuspendedTransaction(Dispatchers.IO) { block() }
}

val indexTableFieldsIndex=ItemIndexes.realFields.toSet()
    .mapIndexed { index, expression -> expression to index  }
    .toMap()

fun nativeSelect(query:String,fieldIndex:Map<Expression<*>,Int>):List<ResultRow>{
    val resultRow= mutableListOf<ResultRow>()
    TransactionManager.current().exec(query){resultSet->
        while (resultSet.next()){
            resultRow += ResultRow.create(resultSet,fieldIndex)
        }
    }
    return resultRow
}

fun nativeUpdate(query: String):Int{
    val connection=TransactionManager.current().connection
    val preparedStatement=connection.prepareStatement(query, arrayOf())
    val isUpdated=preparedStatement.executeUpdate()
    connection.close()
    return isUpdated
}
