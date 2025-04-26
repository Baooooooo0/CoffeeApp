package com.example.coffeeapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.coffeeapp.ui.screens.CartItem

class CartViewModel : ViewModel() {
    var cartItems = mutableStateListOf<CartItem>()
        private set

    private val _totalPrice = mutableStateOf(0.0)
    val totalPrice get() = _totalPrice.value

    fun addToCart(item: CartItem) {
        cartItems.add(item)
        recalculateTotal()
    }

    fun removeFromCart(item: CartItem) {
        cartItems.remove(item)
        recalculateTotal()
    }

    fun increaseQuantity(item: CartItem) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = cartItems[index].quantity + 1)
            recalculateTotal()
        }
    }

    fun decreaseQuantity(item: CartItem) {
        val index = cartItems.indexOf(item)
        if (index != -1 && cartItems[index].quantity > 1) {
            cartItems[index] = cartItems[index].copy(quantity = cartItems[index].quantity - 1)
            recalculateTotal()
        }
    }

    fun updateItemQuantity(index: Int, newQuantity: Int) {
        if (index in cartItems.indices) {
            cartItems[index] = cartItems[index].copy(quantity = newQuantity)
            recalculateTotal()
        }
    }

    fun removeItem(index: Int) {
        if (index in cartItems.indices) {
            cartItems.removeAt(index)
            recalculateTotal()
        }
    }

    private fun recalculateTotal() {
        _totalPrice.value = cartItems.sumOf { it.price * it.quantity }
    }

    fun clearCart() {
        cartItems.clear()
        _totalPrice.value = 0.0
    }

}
