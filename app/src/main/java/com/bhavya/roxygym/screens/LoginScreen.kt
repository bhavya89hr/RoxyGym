package com.bhavya.roxygym.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bhavya.roxygym.R
import com.bhavya.roxygym.navigation.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }






    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(paddingValues),
        ) {
            Image(
                painter = painterResource(
                    R.drawable.icon,
                ), contentDescription = "", modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            Spacer(modifier = Modifier.height(60.dp))


            TextField(
                value = email,
                onValueChange = {
                    email = it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "")
                },
                placeholder = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
            TextField(
                value = password,
                onValueChange = {
                    password = it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "")
                },
                placeholder = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text="Create Account",
                    style = TextStyle(
                        fontSize = 15.sp,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            val user = AuthRepository.registerUser(email,password)
                            if (user != null) {
                                navController.navigate("HomeScreen")
                            } else {
                                errorMessage = "Registration Failed"
                            }
                        }
                    }
                )
                Spacer(Modifier.width(150.dp))
                Text(text = "Forgot Password",
                    style = TextStyle(
                        fontSize = 15.sp,
                        textAlign = TextAlign.Right
                    )
                )


            }
            Spacer(modifier = Modifier.height(5.dp))
            LoginButton(onClick={
                coroutineScope.launch {
                    val user = AuthRepository.loginUser(email,password)
                    if(user != null){
                        println("Login successful: $user")
                        navController.navigate("HomeScreen")
                    }
                    else{
                        println("Login failed")
                        errorMessage = "Login Failed"
                    }
                }
            },  "Login")



        }

    }
}


@Composable
fun LoginButton(onClick: () -> Unit,text:String){
    Button( onClick =onClick, modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)) {
        Text(text=text,
            fontSize = 18.sp, fontWeight = FontWeight.Bold)

    }
}




