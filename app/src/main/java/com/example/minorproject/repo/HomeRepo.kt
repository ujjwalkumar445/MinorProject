package com.example.minorproject.repo

import androidx.lifecycle.MutableLiveData
import com.example.minorproject.home.CatModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class HomeRepo {

    var db = FirebaseFirestore.getInstance()
    var mutabledata: MutableLiveData<ArrayList<CatModel>> = MutableLiveData()


    fun GetFirebasedata(): MutableLiveData<ArrayList<CatModel>> {


        var arrayList: ArrayList<CatModel> = ArrayList()


        db.collection("Category")

            .addSnapshotListener(EventListener<QuerySnapshot>
            { value, e ->
                if (value != null) {
                    for (document: QueryDocumentSnapshot in value) {
                        var id: String = document.id

                        var imageTitle = document.data.get("ImageTitle").toString()
                        var imageUrl = document.data.get("ImageUrl").toString()
                        arrayList.add(CatModel(imageTitle, imageUrl, id))
                    }
                }
                mutabledata.value = arrayList
            })
        return mutabledata

    }
}