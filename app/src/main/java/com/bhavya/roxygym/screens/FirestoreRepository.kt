package com.bhavya.roxygym.screens

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

     private val db = FirebaseFirestore.getInstance()
    private val usersCollection =db.collection("users")


    fun getUsers(onResult: (List<FirestoreUsers>) -> Unit) {
        usersCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("Firestore", "Error fetching users", e)
                return@addSnapshotListener
            }
            val users = snapshot?.documents?.mapNotNull { it.toObject(FirestoreUsers::class.java) } ?: emptyList()
            onResult(users)
        }
    }
    fun DeleteUser(userId: String, onResult: (Boolean) -> Unit) {
        db.collection("users").document(userId)
            .delete()
            .addOnSuccessListener {
                onResult(true)
                Log.d("Firestore", "User deleted successfully")
            }
            .addOnFailureListener { e ->
                onResult(false)
                Log.e("Firestore", "Error deleting user", e)
            }
    }


}