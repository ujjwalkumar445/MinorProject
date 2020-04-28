package com.example.minorproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minorproject.home.CatModel
import com.example.minorproject.repo.HomeRepo

class HomeViewModel : ViewModel() {

    var mRecyclerData: MutableLiveData<ArrayList<CatModel>> = MutableLiveData()
    var homeRepo = HomeRepo()


    fun Getdata(): LiveData<ArrayList<CatModel>> {
        mRecyclerData = homeRepo.GetFirebasedata()
        return mRecyclerData
    }
}