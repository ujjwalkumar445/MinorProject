package com.example.minorproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minorproject.timeline.TimelineModal
import com.example.minorproject.repo.timelineRepo

class TimelineViewModel: ViewModel() {

    var mTimelineRecyclerData: MutableLiveData<ArrayList<TimelineModal>> = MutableLiveData()
    var timelineRepo = timelineRepo()

    fun GetTimelinedata(): LiveData<ArrayList<TimelineModal>> {
        mTimelineRecyclerData = timelineRepo.getTimelineData()
        return mTimelineRecyclerData
    }
}