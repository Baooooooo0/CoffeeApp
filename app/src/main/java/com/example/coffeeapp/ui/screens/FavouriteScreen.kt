package com.example.coffeeapp.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coffeeapp.model.DrinkData
import com.example.coffeeapp.recycle.HeaderScreen
import com.example.coffeeapp.ui.SetStatusBarIconsLight
import com.google.firebase.database.*

@Composable
fun FavouriteScreen(navController: NavController) {

    SetStatusBarIconsLight(isLightIcons = true)
    val favouriteDrinks = remember { mutableStateListOf<DrinkData>() }

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance(
            "https://coffeeappshoputh-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).reference

        database.child("Items")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    favouriteDrinks.clear()
                    for (itemSnapshot in snapshot.children) {
                        val key = itemSnapshot.key ?: continue
                        val picUrls = mutableListOf<String>()
                        itemSnapshot.child("picUrl").children.forEach {
                            it.getValue(String::class.java)?.let { url -> picUrls.add(url) }
                        }

                        val favourite = itemSnapshot.child("favourite").getValue(Boolean::class.java) ?: false

                        if (favourite) {
                            val drink = DrinkData(
                                id = key,
                                title = itemSnapshot.child("title").getValue(String::class.java) ?: "",
                                description = itemSnapshot.child("description").getValue(String::class.java) ?: "",
                                extra = itemSnapshot.child("extra").getValue(String::class.java) ?: "",
                                price = itemSnapshot.child("price").getValue(Double::class.java) ?: 0.0,
                                categoryId = itemSnapshot.child("categoryId").getValue(String::class.java) ?: "",
                                favourite = favourite,
                                picUrl = picUrls
                            )
                            favouriteDrinks.add(drink)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error loading favourites: ${error.message}")
                }
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HeaderScreen(navController = navController,detail = "Favourite Drinks")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            items(favouriteDrinks) { drink ->
                Box(
                    modifier = Modifier
                        .height(230.dp)
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Button(
                        onClick = {navController.navigate("drink_detail/${drink.id}")},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                    fontSize = 25.sp,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                )
                                Text(
                                    text = "Price: ${String.format("%.2f", drink.price)}$",
                                    fontSize = 20.sp,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
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
                                val index = favouriteDrinks.indexOfFirst { it.id == drink.id }
                                if (index != -1) {
                                    favouriteDrinks[index] = updatedDrink
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
