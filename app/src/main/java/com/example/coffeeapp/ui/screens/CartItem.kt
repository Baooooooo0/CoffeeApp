package com.example.coffeeapp.ui.screens

data class CartItem(
    val name: String,
    val price: Double,
    var quantity: Int,
    val imageRes: Int
)