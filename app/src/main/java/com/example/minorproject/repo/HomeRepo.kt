package com.example.minorproject.repo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.minorproject.home.CatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class HomeRepo {

    var db = FirebaseFirestore.getInstance()
    lateinit var arrayList: ArrayList<CatModel>
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
    private var url: String? = null
    private var userId: String = mAuth.currentUser!!.uid
    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun GetFirebasedata(): MutableLiveData<ArrayList<CatModel>> {

        var mutabledata: MutableLiveData<ArrayList<CatModel>> = MutableLiveData()

        db.collection("Category")

            .addSnapshotListener(EventListener<QuerySnapshot>
            { value, e ->
                if (value != null) {
                    arrayList = ArrayList()
                    for (document: QueryDocumentSnapshot in value) {
                        var cat_id: String = document.id

                        var catName = document.data.get("ImageTitle").toString()
                        var imageUrl = document.data.get("ImageUrl").toString()
                        arrayList.add(CatModel(catName, imageUrl, cat_id))
                    }
                }
                mutabledata.value = arrayList

            })
        return mutabledata

    }

    fun uploadImage(filepath: Uri?, imageTitle: String) {
        if (filepath != null) {
            val pathRef = mStorageReference.child("CImages/" + UUID.randomUUID().toString())
            pathRef.putFile(filepath)
                .addOnSuccessListener {
                    Log.i("on success", "uploaded")
                    dwnldUrl(pathRef, imageTitle)

                }
                .addOnFailureListener {
                    Log.i("on failure", "not uploaded")

                }

        }
    }

    private fun dwnldUrl(
        pathRef: StorageReference,
        imageTitle: String
    ) {
        pathRef.downloadUrl
            .addOnSuccessListener {
                url = it.toString()
                UploadData(imageTitle)
            }
    }

    private fun UploadData(imageTitle: String) {
        val image = hashMapOf("ImageUrl" to url, "ImageTitle" to imageTitle, "User Id" to userId)
        database.collection("Category")
            .document().set(image as Map<*, *>)
            .addOnCompleteListener {
                Log.i("data added", "DocumentSnapshot added with ID")
            }
            .addOnFailureListener {
                Log.i("data not added", "Error adding document")
            }
    }


}