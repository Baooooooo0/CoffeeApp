package com.example.coffeeapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coffeeapp.model.DrinkData
import com.example.coffeeapp.ui.screens.DrinkDetailScreen
import com.example.coffeeapp.ui.screens.DrinkListScreen
import com.example.coffeeapp.ui.screens.LoginScreen
import com.example.coffeeapp.ui.screens.MenuScreen
import com.example.coffeeapp.ui.screens.ProfileScreen
import com.example.coffeeapp.ui.screens.SplashScreen
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Menu") {
        composable("Login") { LoginScreen(navController) }
        composable("Profile") { ProfileScreen(navController) }
        composable("Splash") { SplashScreen(navController) }
        composable("Menu") { MenuScreen(navController) }

        composable("category_items/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            DrinkListScreen(categoryId = categoryId, navController = navController)
        }

        composable(
            "drink_detail/{drinkJson}",
            arguments = listOf(navArgument("drinkJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val drinkJson = backStackEntry.arguments?.getString("drinkJson")
            drinkJson?.let {
                val drink = Gson().fromJson(it, DrinkData::class.java)
                DrinkDetailScreen(drink)
            }
        }

    }
}
