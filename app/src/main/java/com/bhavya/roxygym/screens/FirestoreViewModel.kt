package com.bhavya.roxygym.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.PropertyName

data class FirestoreUsers(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val gender: String = "",
    val registrationDate: Timestamp? = null,
    val membershipDuration: Int? = null,
    var downloadUrl: String = "",
    val amountPaid:String="",
    val amountRemaining:String="",




    var profileImageUrl: String = ""
)


{

}

class FirestoreViewModel:ViewModel(){
    private val repository = FirestoreRepository()

    private val db= FirebaseFirestore.getInstance()

    private val _users= MutableStateFlow<List<FirestoreUsers>>(emptyList())
    val users:StateFlow<List<FirestoreUsers>> = _users.asStateFlow()

    init {
        startAutoRefresh()
    }
     private fun startAutoRefresh(){
         viewModelScope.launch {
             while (true){
                 fetchUsers()
                 delay(5000)
             }
         }
     }
    private fun fetchUsers(){
        db.collection("users").addSnapshotListener{snapshot,
            e->
        if(e!=null){
            Log.e("Firestore", "error fetching", e)
            return@addSnapshotListener

        }
            val userList = snapshot?.documents?.mapNotNull { doc ->
                try {
                    val duration = doc.get("membershipDuration")
                    val durationInt = when (duration) {
                        is Number -> duration.toInt()
                        is String -> duration.toIntOrNull()
                        else -> null
                    }

                    FirestoreUsers(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        email = doc.getString("email") ?: "",
                        phone = doc.getString("phone") ?: "",
                        address = doc.getString("address") ?: "",
                        gender = doc.getString("gender") ?: "",
                        registrationDate = doc.getTimestamp("registrationDate"),
                        membershipDuration = durationInt,
                        profileImageUrl = doc.getString("imageUrl") ?: "",
                        amountRemaining = doc.getString("amountRemaining")?:"",
                        amountPaid = doc.getString("amountPaid")?:""
                    )
                } catch (e: Exception) {
                    Log.e("Firestore", "Error mapping Firestore document: ${e.message}")
                    null
                }
            } ?: emptyList()

            _users.value = userList}


    }
    fun DeleteUser(userId: String) {
        repository.DeleteUser(userId) { success ->
            if (success) {
                _users.value = _users.value.filter { it.id != userId }
            }


        }


    }}