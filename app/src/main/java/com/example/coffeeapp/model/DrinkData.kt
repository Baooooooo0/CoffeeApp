package com.example.coffeeapp.model

data class DrinkData(
    val categoryId: String = "",
    val description: String = "",
    val extra: String = "",
    //val picUrl: List<String> = emptyList(),
    val price: Double = 0.0,
    val title: String = ""
)
