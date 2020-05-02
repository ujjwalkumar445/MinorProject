package com.example.minorproject.repo

import androidx.lifecycle.MutableLiveData
import com.example.minorproject.home.CatImageModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class subCatRepo {

    var db = FirebaseFirestore.getInstance()
    var mutableSubdata: MutableLiveData<ArrayList<CatImageModel>> = MutableLiveData()


    fun Getdata(cat_id: String): MutableLiveData<ArrayList<CatImageModel>> {
        var list: ArrayList<CatImageModel> = ArrayList()


        db.collection("CategoryImage").document(cat_id).collection("Image")

            .addSnapshotListener(EventListener<QuerySnapshot>
            { value, e ->
                if (value != null) {
                    for (document: QueryDocumentSnapshot in value) {
                        var id: String = document.id
                        var imageUrl = document.data.get("ImageUrl").toString()

                        list.add(
                            CatImageModel(
                                imageUrl,
                                id,
                                cat_id
                            )
                        )
                    }
                }
                mutableSubdata.value = list
            })
        return mutableSubdata

    }
}