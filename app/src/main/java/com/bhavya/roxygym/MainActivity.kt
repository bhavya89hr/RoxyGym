package com.bhavya.roxygym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bhavya.roxygym.navigation.RGNavigation
import com.bhavya.roxygym.ui.theme.RoxyGymTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseApp.initializeApp(this)
            RoxyGymTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        RGNavigation()

                    }
                }
            }
        }
    }
}

