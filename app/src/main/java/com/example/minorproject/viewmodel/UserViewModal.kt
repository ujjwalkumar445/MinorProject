package com.example.minorproject.viewmodel

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minorproject.profile.UserModal
import com.example.minorproject.repo.UserRepo

class UserViewModal : ViewModel() {

    var registerDetail: MutableLiveData<UserModal> = MutableLiveData()

    var mUserRepo = UserRepo()

    fun onUserInfo(): MutableLiveData<UserModal> {
        registerDetail = mUserRepo.getregisterdetail()
        return registerDetail

    }

    fun onImageChanged(
        email: String,
        username: String,
        view: View,
        filePath: Uri?
    ) {
        mUserRepo.uploadfile(email, username, view, filePath)
    }


}