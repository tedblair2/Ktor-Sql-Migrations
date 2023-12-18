package com.example.db

import com.example.model.CartItem
import com.example.model.UserOrder

interface CartService {
    suspend fun addCartItems(item: CartItem):CartItem?
    suspend fun getCartItems():List<CartItem>
    suspend fun getUserOrder(id:Int):UserOrder?
    suspend fun getUserOrders():List<UserOrder>
    suspend fun updateOrder(quantity:Int,userId:Int,item:String):Boolean
}