package com.example.coffeeapp.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coffeeapp.model.DrinkData
import com.example.coffeeapp.recycle.HeaderMenu
import com.example.coffeeapp.ui.SetStatusBarIconsLight
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun DrinkListScreen(categoryId: String, navController: NavController) {

    SetStatusBarIconsLight(isLightIcons = true)
    val drinks = remember { mutableStateListOf<DrinkData>() }

    LaunchedEffect(categoryId) {
        Log.d("DEBUG", "Loading drinks for categoryId: $categoryId")
        val database = FirebaseDatabase.getInstance(
            "https://coffeeappshoputh-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).reference //get api

        database.child("Items")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (itemSnapshot in snapshot.children) {
                        val key = itemSnapshot.key ?: continue
                        val drink = itemSnapshot.getValue(DrinkData::class.java)
                        drink?.let {
                            it.id = key
                            it.favourite = itemSnapshot.child("favourite").getValue(Boolean::class.java) ?: false
                            if (it.categoryId == categoryId) {
                                drinks.add(it)
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Load error: ${error.message}")
                }
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        HeaderMenu(categoryId, navController = navController)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            items(drinks) { drink ->
                Box(
                    modifier = Modifier
                        .height(230.dp)
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("drink_detail/${drink.id}")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = drink.picUrl.firstOrNull() ?: "",
                                contentDescription = "drink_image",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .size(175.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = drink.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )
                                // Text(text = drink.description, fontSize = 15.sp)
                                Text(
                                    text = "Price: ${String.format("%.2f", drink.price)}$",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )
                            }
                        }
                    }
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        tint = if (drink.favourite) Color.Red else Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(36.dp)
                            .clickable {
                                val updatedDrink = drink.copy(favourite = !drink.favourite)
                                val index = drinks.indexOfFirst { it.id == drink.id }
                                if (index != -1) {
                                    drinks[index] = updatedDrink
                                }
                                val database = FirebaseDatabase.getInstance(
                                    "https://coffeeappshoputh-default-rtdb.asia-southeast1.firebasedatabase.app"
                                ).reference
                                database.child("Items").child(drink.id)
                                    .child("favourite")
                                    .setValue(updatedDrink.favourite)
                                    .addOnSuccessListener {
                                        Log.d("Firebase", "Favourite updated successfully")
                                    }
                                    .addOnFailureListener {
                                        Log.e("Firebase", "Failed to update favourite: ${it.message}")
                                    }
                            }
                    )
                }
            }
        }
    }
}



