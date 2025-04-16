package com.example.coffeeapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coffeeapp.ui.screens.CategoryPanel
import com.example.coffeeapp.ui.screens.DrinkListScreen
import com.example.coffeeapp.ui.screens.LoginScreen
import com.example.coffeeapp.ui.screens.MenuScreen
import com.example.coffeeapp.ui.screens.ProfileScreen
import com.example.coffeeapp.ui.screens.SplashScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Splash") {
        composable("Login") { LoginScreen(navController) }
        composable("Profile") { ProfileScreen(navController) }
        composable("Splash") { SplashScreen(navController) }
        composable("Menu") { MenuScreen(navController) }
        composable("DrinkList") { DrinkListScreen() }

        // ✅ Bổ sung route cho màn Category
        composable("Category") {
            CategoryPanel(navHostController = navController)
        }

        // ✅ Route để hiển thị chi tiết đồ uống
        composable(
            "DrinkDetail/{drinkName}/{imageResId}",
            arguments = listOf(
                navArgument("drinkName") { type = NavType.StringType },
                navArgument("imageResId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val drinkName = backStackEntry.arguments?.getString("drinkName")
            val imageResId = backStackEntry.arguments?.getInt("imageResId")
        }
    }
}
