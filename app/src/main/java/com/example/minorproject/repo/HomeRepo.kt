package com.example.minorproject.repo

import androidx.lifecycle.MutableLiveData
import com.example.minorproject.home.CatModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class HomeRepo {

    var db = FirebaseFirestore.getInstance()
   lateinit var arrayList : ArrayList<CatModel>


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
}