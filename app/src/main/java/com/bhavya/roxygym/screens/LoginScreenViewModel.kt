package com.bhavya.roxygym.screens
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.auth
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

class LoginScreenViewModel: ViewModel() {
    val loadingState= MutableStateFlow(LoadingState.IDLE)
    private val auth:FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    fun signWithEmailAndPassword(email:String,password:String, home:()->Unit){

        try {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        home()
                    }
                    else{
                        Log.d("fb", "signWithEmailAndPassword: ${task.result.toString()}")
                    }
                }

        }
catch (ex:Exception){
    Log.d("FB", "signWithEmailAndPassword: ${ex.message} ")


}
    }

}