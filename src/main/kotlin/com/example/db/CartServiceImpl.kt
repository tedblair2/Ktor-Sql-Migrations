package com.example.db

import com.example.model.*
import com.example.plugins.dbQuery
import com.example.plugins.nativeUpdate
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class CartServiceImpl : CartService {

    private fun resultRowToCartItem(resultRow: ResultRow):CartItem{
        return CartItem(
            id = resultRow[Carts.id],
            items = resultRow[Carts.items].toList(),
            userId = resultRow[Carts.userId]
        )
    }
    override suspend fun addCartItems(item: CartItem): CartItem? = dbQuery{
        val insertStatement=Carts.insert {
            it[items]=item.items.toTypedArray()
            it[userId]=item.userId
        }
        insertStatement.resultedValues?.singleOrNull()?.let {  resultRowToCartItem(it)}
    }

    override suspend fun getCartItems(): List<CartItem> = dbQuery{
        Carts.selectAll().map { resultRowToCartItem(it) }
    }

    override suspend fun getUserOrder(id: Int): UserOrder? = dbQuery{
        (Users innerJoin Carts)
            .slice(Users.name,Users.address,Carts.items)
            .select { (Users.id eq id) }
            .map {
                val items=it[Carts.items].toList()
                UserOrder(
                    userName = it[Users.name],
                    address = it[Users.address],
                    items = items,
                    total = calculateTotal(items)
                )
            }.singleOrNull()
    }

    override suspend fun getUserOrders(): List<UserOrder> = dbQuery{
        (Users innerJoin Carts)
            .slice(Users.name,Users.address,Carts.items)
            .selectAll()
            .map {
                val items=it[Carts.items].toList()
                UserOrder(
                    userName = it[Users.name],
                    address = it[Users.address],
                    items = items,
                    total = calculateTotal(items)
                )
            }
    }

    override suspend fun updateOrder(quantity: Int, userId: Int,item:String): Boolean = dbQuery{
        val query="WITH cart_item AS (\n" +
                "    SELECT ('{'||index-1||',quantity}')::text[] AS path\n" +
                "    FROM carts, jsonb_array_elements(items) WITH ORDINALITY arr(item, index)\n" +
                "    WHERE item->>'itemName' = '$item' AND user_id = $userId\n" +
                ")\n" +
                "UPDATE carts\n" +
                "SET items = jsonb_set(items, cart_item.path, '$quantity', false)\n" +
                "FROM cart_item\n" +
                "WHERE user_id = $userId;"
        nativeUpdate(query)>0
    }

    private fun calculateTotal(items:List<Cart>):Double{
        val total = items.sumOf { it.price * it.quantity }
        return "%.2f".format(total).toDouble()
    }

}