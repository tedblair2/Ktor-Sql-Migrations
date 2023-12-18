package com.example.model

import org.jetbrains.exposed.sql.Table


data class ItemIndex(
    val index:Int
)

object ItemIndexes: Table(){
    val itemIndex=integer("itemindex")
}