package com.example.minorproject.repo

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.minorproject.profile.UserModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class UserRepo {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var newimageurld: String
    private var mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
    var data1: MutableLiveData<UserModal> = MutableLiveData()
    var mUserName: String = ""
    var mUserEmail: String = ""
    var mUserImage: String = ""


    fun getregisterdetail(): MutableLiveData<UserModal> {
        val documentReference: DocumentReference =
            db.collection("User").document(mAuth.currentUser!!.uid)
        documentReference.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (documentSnapshot != null) {
                mUserEmail = documentSnapshot.data?.get("email").toString()
                mUserName = documentSnapshot.data?.get("name").toString()
                mUserImage = documentSnapshot.data?.get("image").toString()
                var data = UserModal(mUserName, mUserEmail, mUserImage)
                data1.value = data
            }
        }
        return data1

    }


    fun uploadfile(
        email: String,
        username: String,
        view: View,
        filePath: Uri?
    ) {

        if (filePath != null) {
            val imageRef = mStorageReference.child("images/" + mAuth.currentUser!!.uid)
            imageRef.putFile(filePath)
                .addOnSuccessListener {
                    Log.e("Success", "Save to Storage")
                    downloadUrl(imageRef, view, email, username)
                }
                .addOnFailureListener {
                    Log.e("Failure", "Not Save to Storage")


                }
        }
    }

    private fun downloadUrl(
        imageRef: StorageReference,
        view: View,
        email: String,
        username: String
    ) {
        imageRef.getDownloadUrl()
            .addOnSuccessListener {
                newimageurld = it.toString()
                Log.e(" image url", newimageurld)
                addData(view, email, username)

            }
    }

    private fun addData(view: View, email: String, username: String) {
        val user = hashMapOf(
            "email" to email, "name" to username, "image" to newimageurld
        )
        db.collection("User").document(mAuth.currentUser!!.uid).set(user as Map<String, Any>)
            .addOnCompleteListener { documentReference ->
                Log.e("Data Added", "Added to collection with the id")
                getregisterdetail()
            }
            .addOnFailureListener { documentRefrence ->
                Log.e("Failure", "Data not added to collection")
            }
    }
}