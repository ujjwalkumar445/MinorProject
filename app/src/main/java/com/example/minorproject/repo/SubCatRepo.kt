package com.example.minorproject.repo

import androidx.lifecycle.MutableLiveData
import com.example.minorproject.home.ImageCat.CatImageModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class subCatRepo {

    var db = FirebaseFirestore.getInstance()
    var mutableSubdata: MutableLiveData<ArrayList<CatImageModel>> = MutableLiveData()


    fun Getdata(args: String): MutableLiveData<ArrayList<CatImageModel>> {
        var list: ArrayList<CatImageModel> = ArrayList()


        db.collection("CategoryImage").document(args).collection("Image")

            .addSnapshotListener(EventListener<QuerySnapshot>
            { value, e ->
                if (value != null) {
                    for (document: QueryDocumentSnapshot in value) {
                        var id: String = document.id
                        var imageUrl = document.data.get("ImageUrl").toString()

                        list.add(CatImageModel(imageUrl, id, args))
                    }
                }
                mutableSubdata.value = list
            })
        return mutableSubdata

    }
}