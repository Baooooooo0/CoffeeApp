package com.example.coffeeapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.coffeeapp.model.DrinkData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun DrinkListScreen(navController: NavHostController) {
    val drinks = remember { mutableStateListOf<DrinkData>() }

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance(
            "https://coffeeappshoputh-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).reference //get api

        database.child("Items")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    drinks.clear()
                    for (itemSnapshot in snapshot.children) {
                        val drink = itemSnapshot.getValue(DrinkData::class.java)
                        Log.d("Firebase", "Loaded drink: ${drink?.title}")
                        drink?.let { drinks.add(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Load error: ${error.message}")
                }
            })
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(drinks) { drink ->
            Text(text = drink.title, fontWeight = FontWeight.Bold)
        }
    }
}