package com.bhavya.roxygym.screens

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


fun FirestoreData(userId:String,name:String, email:String,phone:String,address:String,
                  membershipDuration: Int,gender:String,onSuccess:()->Unit,
                 onFailure:(Exception)->Unit,    profileImageUrl: String,
                  imageUrl: String,amountPaid:String,amountRemaining:String
){
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

   val user = mutableMapOf(

       "userId" to userId,
       "name" to name,
       "email" to email,
       "phone" to phone,
       "address" to address,
       "gender" to gender,
       "registrationDate" to FieldValue.serverTimestamp(),  // Store registration timestamp
       "membershipDuration" to membershipDuration, // Store membership duration in months
       "profileImageUrl" to imageUrl,// ✅ ADD THIS LINE,
       "amountPaid" to amountPaid,
       "amountRemaining" to amountRemaining

   )



    db.collection("users").document(userId)
        .set(user)
        .addOnSuccessListener {
            onSuccess()
            Log.d("Firestore", "Success ")
        }
        .addOnFailureListener {
            e->onFailure(e)
            Log.d("Firestore", "Failure ")
        }






}



