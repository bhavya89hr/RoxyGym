package com.bhavya.roxygym.navigation

import EditScreen
import RenewScreen
import StatusScreen

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bhavya.roxygym.screens.AddUserScreen
import com.bhavya.roxygym.screens.FirestoreUsers
import com.bhavya.roxygym.screens.HomeScreen
import com.bhavya.roxygym.screens.LoginScreen
import com.bhavya.roxygym.screens.PriceScreen

@Composable
fun RGNavigation(){
    val navController = rememberNavController(   )
    NavHost(navController = navController,startDestination=RGScreens.LoginScreen.name) {

        composable(route = RGScreens.LoginScreen.name) {
            LoginScreen(
                navController = navController
            )
        }
            composable(route = RGScreens.HomeScreen.name) {
                HomeScreen(
                    navController = navController
                )
            }
        composable(route = RGScreens.AddUserScreen.name) {
            AddUserScreen(
                navController = navController
            )
        }
        composable(route = RGScreens.StatusScreen.name) {
            StatusScreen(navController = navController)

        }
        composable(route=RGScreens.EditScreen.name){
            EditScreen(navController = navController)
        }
        composable(route=RGScreens.PriceScreen.name){
            PriceScreen(navController=navController)
        }
        composable(route=RGScreens.RenewScreen.name){
            RenewScreen(
                navController = navController, user = FirestoreUsers()

            )

        }


    }}


