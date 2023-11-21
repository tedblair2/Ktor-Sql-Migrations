package com.example.di

import com.example.db.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import java.io.File

val appModule= module {
    single<UserService> {parameters->
        UserServiceCacheImpl(UserServiceImpl(), File(parameters.get() as String))
    }
    single<SchoolService> {
        SchoolServiceImpl()
    }
}

fun Application.configureDI(){
    install(Koin){
        modules(appModule)
    }
}