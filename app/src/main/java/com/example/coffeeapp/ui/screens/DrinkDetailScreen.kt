package com.example.coffeeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.coffeeapp.model.DrinkData

@Composable
fun DrinkDetailScreen(drink: DrinkData) {
    var selectedSize by remember { mutableStateOf("S") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(drink.picUrl.firstOrNull())
                .crossfade(true)
                .build(),
            contentDescription = drink.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp) // Điều chỉnh chiều cao ảnh (có thể thay đổi tùy ý)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = drink.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${String.format("%.1f", drink.price)}$",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFA500)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = drink.extra,
            color = Color.LightGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = drink.description,
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Size",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            listOf("S", "M", "L").forEach { size ->
                val isSelected = selectedSize == size
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFFFFA500) else Color.DarkGray)
                        .border(1.dp, Color.Gray, CircleShape)
                        .clickable { selectedSize = size }
                ) {
                    Text(text = size, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { /* Handle Buy */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
        ) {
            Text(text = "Buy Now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

