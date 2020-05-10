package com.example.minorproject.repo

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.minorproject.R
import com.example.minorproject.home.CatImageModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class subCatRepo {

    var db = FirebaseFirestore.getInstance()

    private var mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
    private var url: String? = null
    lateinit var arrayList: ArrayList<CatImageModel>

    @RequiresApi(Build.VERSION_CODES.O)
    private val timeimage = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val localformat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    @RequiresApi(Build.VERSION_CODES.O)
    val localDate = timeimage.format(localformat)


    fun Getdata(cat_id: String): MutableLiveData<ArrayList<CatImageModel>> {
        var mutableSubdata: MutableLiveData<ArrayList<CatImageModel>> = MutableLiveData()
//        var list: ArrayList<CatImageModel> = ArrayList()


        db.collection("CategoryImage").document(cat_id).collection("Image")

            .addSnapshotListener(EventListener<QuerySnapshot>
            { value, e ->
                if (value != null) {
                    arrayList = ArrayList()
                    for (document: QueryDocumentSnapshot in value) {
                        var id: String = document.id
                        var imageUrl = document.data.get("ImageUrl").toString()

                        arrayList.add(
                            CatImageModel(
                                imageUrl,
                                id,
                                cat_id
                            )
                        )
                    }
                }
                mutableSubdata.value = arrayList
            })
        return mutableSubdata

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadCatImage(filepath: Uri?, mSubCatId: String?) {
        if (filepath != null) {
            val pathRef = mStorageReference.child("CImages/" + UUID.randomUUID().toString())
            pathRef.putFile(filepath!!)
                .addOnSuccessListener {
                    Log.i("on success", "uploaded")
                    downloadUrl(pathRef, mSubCatId)

                }
                .addOnFailureListener {
                    Log.i("on failure", "not uploaded")

                }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun downloadUrl(
        pathRef: StorageReference,
        mSubCatId: String?
    ) {
        pathRef.downloadUrl
            .addOnSuccessListener {
                url = it.toString()
                UploadImage(mSubCatId)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun UploadImage(mSubCatId: String?) {

        val image = hashMapOf("ImageUrl" to url)
        db.collection("CategoryImage")
            .document(mSubCatId!!).collection("Image").add(image as Map<*, *>)
            .addOnSuccessListener { DocumentReference ->
                val id = DocumentReference.id
                timelinedata(id)
                Log.i("data added", "DocumentSnapshot added with ID")
            }
            .addOnFailureListener {
                Log.i("data not added", "Error adding document")
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timelinedata(id: String) {

        val image = hashMapOf("ImageUrl" to url, "Date" to localDate)
        db.collection("Timeline").document(id)
            .set(image as Map<*, *>)
            .addOnCompleteListener {
                Log.i("data added", "DocumentSnapshot added with ID")
            }
            .addOnFailureListener {
                Log.i("data not added", "Error adding document")
            }

    }
}