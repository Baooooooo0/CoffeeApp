package com.example.coffeeapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.coffeeapp.ui.screens.CartItem

class CartViewModel : ViewModel() {
    val cartItems = mutableStateListOf<CartItem>()

    fun addToCart(item: CartItem) {
        val existing = cartItems.find { it.name == item.name }
        if (existing != null) {
            existing.quantity += item.quantity
        } else {
            cartItems.add(item)
        }
    }

    fun removeFromCart(item: CartItem) {
        cartItems.remove(item)
    }

    fun clearCart() {
        cartItems.clear()
    }
}
