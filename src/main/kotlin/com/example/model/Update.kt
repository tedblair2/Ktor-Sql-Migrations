package com.example.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.jdbc.JdbcConnectionImpl
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun <T> Table.arrayColumn(name:String, columnType: ColumnType):Column<Array<T>>{
    return registerColumn(name,ArrayColumnType(columnType))
}
class ArrayColumnType(private val type: ColumnType) : ColumnType() {

    private fun supportsArrays():Boolean{
        val connection=TransactionManager.current().connection
        if (connection is JdbcConnectionImpl){
            val metadata=connection.connection.metaData
            val dbProductionName=metadata.databaseProductName
            return !dbProductionName.startsWith("SQLite")
        }
        return true
    }

    override fun sqlType(): String = buildString {
        if (!supportsArrays()) {
            append("TEXT")
        } else {
            append(type.sqlType())
            append(" ARRAY")
        }
    }

    override fun valueToDB(value: Any?): Any? {
        if (!supportsArrays())
            return "'NOT SUPPORTED'"

        if (value is Array<*>) {
            val columnType = type.sqlType().split("(")[0]
            val jdbcConnection = (TransactionManager.current().connection as JdbcConnectionImpl).connection
            return jdbcConnection.createArrayOf(columnType, value)
        } else {
            return super.valueToDB(value)
        }
    }

    override fun valueFromDB(value: Any): Any {
        if (!supportsArrays()) {
            val clazz = type::class
            val clazzName = clazz.simpleName
            if (clazzName == "LongColumnType")
                return arrayOf<Long>()
            if (clazzName == "TextColumnType")
                return arrayOf<String>()
            error("Unsupported Column Type")
        }

        if (value is java.sql.Array) {
            return value.array
        }
        if (value is Array<*>) {
            return value
        }
        error("Array does not support for this database")
    }

    override fun notNullValueToDB(value: Any): Any {
        if (!supportsArrays())
            return "'NOT SUPPORTED'"

        if (value is Array<*>) {
            if (value.isEmpty())
                return "'{}'"

            val columnType = type.sqlType().split("(")[0]
            val jdbcConnection = (TransactionManager.current().connection as JdbcConnectionImpl).connection
            return jdbcConnection.createArrayOf(columnType, value) ?: error("Can't create non null array for $value")
        } else {
            return super.notNullValueToDB(value)
        }
    }
}