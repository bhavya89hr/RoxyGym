package com.bhavya.roxygym.navigation

enum class RGScreens {
    HomeScreen,
    LoginScreen,
    StatusScreen,
    EditScreen,
    PriceScreen,
    RenewScreen,
    AddUserScreen;
    companion object{
        fun fromRoute(route: String):RGScreens
                = when(route?.substringBefore("/")) {
                    LoginScreen.name->LoginScreen
            HomeScreen.name->HomeScreen
            AddUserScreen.name->AddUserScreen

            else-> throw  IllegalArgumentException("Route")
        }
        }
}