package com.bhavya.roxygym.screens

import com.google.android.gms.common.api.Status

data class LoadingState(val status: Status, val message:String?=null){
    companion object{
        val IDLE = LoadingState(Status.IDLE)
            val SUCCESS = LoadingState(Status.SUCCESS)

                val LOADING=LoadingState(Status.LOADING)
                    val FAILED=LoadingState(Status.FAILED)


    }
    enum class Status{
        SUCCESS,
        IDLE,
        LOADING,
        FAILED
    }
}
