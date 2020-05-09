package com.example.minorproject.repo

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.minorproject.HomePage
import com.google.firebase.auth.FirebaseAuth

class LoginRepo {

    var mAuth = FirebaseAuth.getInstance()

    fun getLoginDetail(email: String, password: String, view: View) {

        mAuth.signInWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "SignInWithEmail : Success")
                    newScreen(view)
                } else {
                    Log.e(TAG, "UserWithEmail:failure", task.exception)
                }
            }

    }

    private fun newScreen(view: View) {
        val intent = Intent(view.context, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        view.context.startActivity(intent)
    }
}