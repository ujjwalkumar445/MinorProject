package com.example.minorproject.repo

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import com.example.minorproject.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUpRepo {

    private var mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
    private lateinit var url: String
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    fun getSignUpDetail(
        username: String,
        email: String,
        password: String,
        view: View,
        filepath: Uri?
    ) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "createUserWithEmail:success")
                    uploadFile(filepath, username, email, password)
                    loginScreen(view)
                } else {
                    Log.e(TAG, "createUserWithEmail:failure", task.exception)
                }

            }
    }


    private fun uploadFile(
        filepath: Uri?,
        username: String,
        email: String,
        password: String
    ) {

        if (filepath != null) {
            val imageRef = mStorageReference.child("images/" + mAuth.currentUser!!.uid)
            imageRef.putFile(filepath)
                .addOnSuccessListener {
                    Log.e("on success", "uploaded")
                    getUrl(imageRef, username, email, password)
                }
                .addOnFailureListener {
                    Log.e("on failure", "not uploaded")


                }
        }
    }

    private fun getUrl(
        imageRef: StorageReference,
        username: String,
        email: String,
        password: String
    ) {
        imageRef.getDownloadUrl()
            .addOnSuccessListener {
                url = it.toString()
                Log.i(" image url", url)
                userData(username, email, password)

            }
    }

    private fun userData(username: String, email: String, password: String) {
        var user = hashMapOf(
            "email" to email, "name" to username,
            "password" to password, "image" to url
        )
        db.collection("User").document(mAuth.currentUser!!.uid).set(user as Map<String, Any>)
            .addOnCompleteListener { documentReference ->
                Log.i("data added", "DocumentSnapshot added with ID")
            }
            .addOnFailureListener { documentRefrence ->
                Log.i("data not added", "Error adding document")
            }
    }

    private fun loginScreen(view: View) {
        val intent = Intent(view.context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        view.context.startActivity(intent)
    }
}

