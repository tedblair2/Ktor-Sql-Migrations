package com.example

import com.example.di.configureDI
import com.example.plugins.configureDatabases
import com.example.plugins.configureMonitoring
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDI()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureRouting()
}
