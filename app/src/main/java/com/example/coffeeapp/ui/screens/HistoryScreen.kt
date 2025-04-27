package com.example.coffeeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeeapp.model.CartItem
import com.example.coffeeapp.recycle.HeaderScreen
import com.example.coffeeapp.viewmodel.CartViewModel

@Composable
fun HistoryScreen(navController: NavController, cartViewModel: CartViewModel) {
    val history = cartViewModel.purchaseHistory
    val timestamps = cartViewModel.purchaseTimestamps

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        HeaderScreen(navController = navController, detail = "Your History")

        Spacer(modifier = Modifier.height(16.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No purchase history yet.",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            Button(
                onClick = { cartViewModel.clearPurchaseHistory() },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 8.dp)
            ) {
                Text(text = "Clear History")
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(history.reversed()) { index, order ->
                    val reversedIndex = history.size - 1 - index
                    val timestamp = timestamps.getOrNull(reversedIndex) ?: "Unknown time"

                    Text(
                        text = "Order #${history.size - index} - $timestamp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    order.forEach { item ->
                        HistoryItemRow(item)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun HistoryItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Quantity: ${item.quantity}",
                fontSize = 14.sp
            )
        }

        Text(
            text = "${"%,.2f".format(item.price * item.quantity)} $",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
