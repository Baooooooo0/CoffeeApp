package com.example.coffeeapp.model

data class DrinkData(
    val categoryId: String = "",
    val title: String = "",
    val description: String = "",
    val extra: String = "",
    val price: Double = 0.0,
    val picUrl: List<String>? = null
)

