package com.example.model


import com.example.plugins.dbQuery
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Function
import org.postgresql.jdbc.PgArray

@Serializable
data class Car(
    @SerialName("color")
    val color: String?,
    @SerialName("dimensions")
    val dimensions: Dimensions?,
    @SerialName("engine")
    val engine: Engine?,
    @SerialName("name")
    val name: String?,
    @SerialName("transmission")
    val transmission: String?,
    @SerialName("4wheeldrive")
    val wheeldrive: Boolean?
)

data class CarItems(
    val id:Int,
    val attrs:Car
)

object Cars:Table(){
    val id=integer("id").autoIncrement()
    val attr=json<Car>("attrs",
        serialize = {Json.encodeToString(it as Car)},
        deserialize = {Json.decodeFromString(it) as Car})
    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}

class JsonValue<T>(
    override val columnType: IColumnType,
):Function<T>(columnType){
    override fun toQueryBuilder(queryBuilder: QueryBuilder) =queryBuilder{

    }
}

class JsonNameExpression(val columnName:String):Expression<String>(){
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder.append("$columnName->>'name'")
    }
}

class ArrayExpression():Expression<PgArray>(){
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder.append("('{'||jsonb_array_length(items)||'}')::text[]")
    }
}

suspend fun getArrayResult():Array<String> = dbQuery {
    val pgArray=Carts
        .slice(ArrayExpression().alias("arraysize"))
        .select { (Carts.userId eq 3) }
        .map { it[ArrayExpression().alias("arraysize")] }
        .single()
    val array=pgArray.array as Array<String>
    pgArray.free()
    array
}
