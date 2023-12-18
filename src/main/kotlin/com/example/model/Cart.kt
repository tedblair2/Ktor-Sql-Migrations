package com.example.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject

@Serializable
data class Cart(
    val itemName:String,
    val price:Double,
    val quantity:Int
)
@Serializable
data class CartItem(
    val userId:Int,
    val items:List<Cart>,
    val id:Int=0
)

object Carts:Table(){
    val id=integer("id").autoIncrement()
    val userId=integer("user_id").references(Users.id,ReferenceOption.CASCADE)
    val items=json<Array<Cart>>("items",
        serialize = {Json.encodeToString(it as Array<Cart>)},
        deserialize = {Json.decodeFromString(it) as Array<Cart>})

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}


fun <T : Any> Table.json(name: String,serialize: (Any) -> String,deserialize: (String) -> Any): Column<T>{
    return registerColumn(name,JsonColumnType(serialize,deserialize))
}

class JsonColumnType(
    private val serialize: (Any) -> String,
    private val deserialize: (String) -> Any) : ColumnType() {
    override fun sqlType() = "JSONB"

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        super.setParameter(stmt, index, value.let {
            PGobject().apply {
                type=sqlType()
                this.value=value as String?
            }
        })
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is PGobject) {
            return value
        }
        return deserialize(checkNotNull(value.value))
    }

    override fun notNullValueToDB(value: Any): String {
        return serialize(value)
    }

}
