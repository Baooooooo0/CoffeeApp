package com.example.coffeeapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.coffeeapp.ui.screens.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartViewModel : ViewModel() {
    var cartItems = mutableStateListOf<CartItem>()
        private set

    private val _totalPrice = mutableStateOf(0.0)
    val totalPrice get() = _totalPrice.value

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        loadCart()
    }

    fun addToCart(item: CartItem) {
        val existingItem = cartItems.find { it.name == item.name }
        if (existingItem != null) {
            updateItemQuantity(existingItem, existingItem.quantity + 1)
        } else {
            cartItems.add(item)
            recalculateTotal()
            saveCart()
        }
    }

    fun removeFromCart(item: CartItem) {
        cartItems.remove(item)
        recalculateTotal()
        saveCart()
    }

    fun increaseQuantity(item: CartItem) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = cartItems[index].quantity + 1)
            recalculateTotal()
            saveCart()
        }
    }

    fun decreaseQuantity(item: CartItem) {
        val index = cartItems.indexOf(item)
        if (index != -1 && cartItems[index].quantity > 1) {
            cartItems[index] = cartItems[index].copy(quantity = cartItems[index].quantity - 1)
            recalculateTotal()
            saveCart()
        }
    }

    fun updateItemQuantity(item: CartItem, newQuantity: Int) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = newQuantity)
            recalculateTotal()
            saveCart()
        }
    }

    fun removeItem(index: Int) {
        if (index in cartItems.indices) {
            cartItems.removeAt(index)
            recalculateTotal()
            saveCart()
        }
    }

    fun clearCart() {
        cartItems.clear()
        _totalPrice.value = 0.0
        saveCart()
    }

    private fun recalculateTotal() {
        _totalPrice.value = cartItems.sumOf { it.price * it.quantity }
    }

    private fun saveCart() {
        if (::sharedPreferences.isInitialized) {
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(cartItems)
            editor.putString("cart_items", json)
            editor.apply()
        }
    }

    private fun loadCart() {
        if (::sharedPreferences.isInitialized) {
            val gson = Gson()
            val json = sharedPreferences.getString("cart_items", null)
            val type = object : TypeToken<List<CartItem>>() {}.type
            val items: List<CartItem>? = gson.fromJson(json, type)
            items?.let {
                cartItems.addAll(it)
                recalculateTotal()
            }
        }
    }
}
