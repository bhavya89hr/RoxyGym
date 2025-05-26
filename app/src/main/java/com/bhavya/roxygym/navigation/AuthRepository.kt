package com.bhavya.roxygym.navigation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

object AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? {

        return auth.currentUser
    }

    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception){
            println("Login Error: ${e.message}")
            null
        }
    }

    suspend fun registerUser(email: String,password: String): FirebaseUser?
    {
        return try{
            val result = auth.createUserWithEmailAndPassword(email,password).await()
            result.user
        }catch(e:Exception){
            null
        }
    }
    fun logoutUser(email: String, password: String) {
        auth.signOut()
    }

   }
