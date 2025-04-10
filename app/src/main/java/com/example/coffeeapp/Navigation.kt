package com.example.coffeeapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coffeeapp.ui.screens.LoginScreen
import com.example.coffeeapp.ui.screens.MenuScreen
import com.example.coffeeapp.ui.screens.ProfileScreen
import com.example.coffeeapp.ui.screens.SplashScreen


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login") {
        composable("Login") { LoginScreen(navController) }
        composable("Profile") { ProfileScreen(navController)}
        composable("Splash") {SplashScreen(navController)}
        composable("Menu") { MenuScreen(navController)}
    }
}