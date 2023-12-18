package com.example.routes

import com.example.db.CartService
import com.example.model.CartItem
import com.example.model.getArrayResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get as get1

fun Routing.cartRoute(cartService: CartService=get1()){
    route("cart"){
        get {
            val items=cartService.getCartItems()
            call.respond(HttpStatusCode.OK,items)
        }
        post {
            val item=call.receive<CartItem>()
            val result=cartService.addCartItems(item)
            call.respond(HttpStatusCode.Created,result!!)
        }
    }
    route("usercart"){
        get {
            val userOrders=cartService.getUserOrders()
            call.respond(HttpStatusCode.OK,userOrders)
        }
        get("/{id}") {
            call.parameters["id"]?.toInt()?.let {
                cartService.getUserOrder(it)?.let {userOrder->
                    call.respond(HttpStatusCode.Found,userOrder)
                } ?: call.respond(HttpStatusCode.NotFound, mapOf("error" to "Order not found!!"))
            } ?: call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Provide id!!"))
        }
    }
    route("test"){
        get {
            val array= getArrayResult()
            call.respond(array)
        }
    }
}