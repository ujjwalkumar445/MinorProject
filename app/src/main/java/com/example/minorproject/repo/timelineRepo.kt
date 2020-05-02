package com.example.minorproject.repo

import androidx.lifecycle.MutableLiveData
import com.example.minorproject.timeline.TimelineModal
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class timelineRepo { var db = FirebaseFirestore.getInstance()

    var timelineMutableData : MutableLiveData<ArrayList<TimelineModal>> = MutableLiveData()

    fun getTimelineData(): MutableLiveData<ArrayList<TimelineModal>> {

        var timelinelist: ArrayList<TimelineModal> = ArrayList()


        db.collection("Timeline").orderBy("Date", Query.Direction.DESCENDING)
            .addSnapshotListener(EventListener<QuerySnapshot>
            { value, e ->
                if (value != null) {
                    for (document: QueryDocumentSnapshot in value) {
                        var date = document.data.get("Date").toString()
                        var imageUrl = document.data.get("ImageUrl").toString()
                        timelinelist.add(TimelineModal(imageUrl, date))

                    }
                }
                timelineMutableData.value = timelinelist
            })
        return timelineMutableData
    }
}