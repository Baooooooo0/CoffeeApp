package com.example.coffeeapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.coffeeapp.model.CategoryItemData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CategoryPanel(navHostController: NavHostController) {
    val iconSize = 40.dp
    val cornerRadius = 24.dp

    val categories = listOf(
        CategoryItemData("Ice Drink", com.example.coffeeapp.R.drawable.ice_drink),
        CategoryItemData("Hot Drink", com.example.coffeeapp.R.drawable.hot_drink),
        CategoryItemData("Hot Coffee", com.example.coffeeapp.R.drawable.hot_coffee),
        CategoryItemData("Ice Coffee", com.example.coffeeapp.R.drawable.ice_coffee),
        CategoryItemData("Brewing Coffee", com.example.coffeeapp.R.drawable.brewing_coffee),
        CategoryItemData("Shake", com.example.coffeeapp.R.drawable.shake),
        CategoryItemData("Restaurant", com.example.coffeeapp.R.drawable.restaurant),
        CategoryItemData("Breakfast", com.example.coffeeapp.R.drawable.breakfast),
        CategoryItemData("Cake", com.example.coffeeapp.R.drawable.cake)
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(cornerRadius))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Select Category",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            for (rowIndex in 0..2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (colIndex in 0..2) {
                        val index = rowIndex * 3 + colIndex
                        if (index < categories.size) {
                            val item = categories[index]
                            CategoryItem(
                                iconResId = item.iconResId,
                                label = item.label,
                                iconSize = iconSize,
                                onClick = {
                                    navHostController.navigate("DrinkDetail/${item.label}/${item.iconResId}")
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CategoryItem(
    iconResId: Int,
    label: String,
    iconSize: Dp,
    onClick: () -> Unit
) {
    val painter = painterResource(id = iconResId)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painter,
                contentDescription = label,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
