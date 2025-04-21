package com.example.coffeeapp.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.coffeeapp.ui.SetStatusBarIconsLight
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

@Composable
fun DrinkListScreen(categoryId: String, navController: NavController) {

    SetStatusBarIconsLight(isLightIcons = true)
    val drinks = remember { mutableStateListOf<DrinkData>() }

    LaunchedEffect(categoryId) {
        Log.d("DEBUG", "Loading drinks for categoryId: $categoryId")
        val database = FirebaseDatabase.getInstance(
            "https://coffeeappshoputh-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).reference

        database.child("Items")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    drinks.clear()
                    for (itemSnapshot in snapshot.children) {
                        val drink = itemSnapshot.getValue(DrinkData::class.java)
                        drink?.let {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp)
                .background(color = Color.Black)
        ) {
            items(drinks) { drink ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.DarkGray)
                        .clickable {
                            val drinkJson = Uri.encode(Gson().toJson(drink))
                            navController.navigate("drink_detail/$drinkJson")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = drink.picUrl.firstOrNull() ?: "",
                        contentDescription = "drink_image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .size(100.dp),
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
                            fontSize = 15.sp,
                            color = Color.White,
                            modifier = Modifier.clickable {
                                val drinkJson = Uri.encode(Gson().toJson(drink))
                                navController.navigate("drink_detail/$drinkJson")
                            }
                        )
                        Text(text = drink.description, fontSize = 12.sp, color = Color.LightGray)
                        Text(
                            text = "Price: ${String.format("%.2f", drink.price)}$",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
