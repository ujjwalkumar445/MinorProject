package com.example.minorproject.repo

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.minorproject.HomePage
import com.google.firebase.auth.FirebaseAuth

class LoginRepo {

    var mAuth = FirebaseAuth.getInstance()
    var onComplete = MutableLiveData<Boolean>(false)

    fun getLoginDetail(email: String, password: String): MutableLiveData<Boolean> {

        mAuth.signInWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "SignInWithEmail : Success")
                    onComplete.value = true
                } else {
                    Log.e(TAG, "UserWithEmail:failure", task.exception)
                    onComplete.value = false
                }
            }
        return onComplete
    }


}