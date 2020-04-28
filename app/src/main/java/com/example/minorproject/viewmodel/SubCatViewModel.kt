package com.example.minorproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minorproject.home.ImageCat.CatImageModel
import com.example.minorproject.repo.subCatRepo

class SubCatViewModel : ViewModel() {

    var mSubRecyclerData: MutableLiveData<ArrayList<CatImageModel>> = MutableLiveData()
    var subCatRepo = subCatRepo()


    fun Getdata(args: String): LiveData<ArrayList<CatImageModel>> {
        mSubRecyclerData = subCatRepo.Getdata(args)
        return mSubRecyclerData
    }
}