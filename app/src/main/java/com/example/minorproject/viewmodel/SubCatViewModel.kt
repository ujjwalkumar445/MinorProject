package com.example.minorproject.viewmodel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minorproject.home.CatImageModel
import com.example.minorproject.repo.subCatRepo

class SubCatViewModel : ViewModel() {

    var mSubRecyclerData: MutableLiveData<ArrayList<CatImageModel>> = MutableLiveData()
    var subCatRepo = subCatRepo()


    fun Getdata(args: String): LiveData<ArrayList<CatImageModel>> {
        mSubRecyclerData = subCatRepo.Getdata(args)
        return mSubRecyclerData
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onAddImageClicked(filepath: Uri?, mSubCatId: String?) {
        subCatRepo.uploadCatImage(filepath, mSubCatId)

    }
}