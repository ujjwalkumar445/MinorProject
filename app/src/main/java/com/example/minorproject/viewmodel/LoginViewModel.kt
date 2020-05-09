package com.example.minorproject.viewmodel

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minorproject.repo.LoginRepo
import com.example.minorproject.utils.ValidationsUtils

class LoginViewModel : ViewModel() {

    private var errMessage = MutableLiveData<String>()
    var loginRepo = LoginRepo()

    fun getErrMessage(): LiveData<String> {
        return errMessage
    }

    fun onLoginClicked(email: String, password: String, view: View) {
        if (TextUtils.isEmpty(email)) {
            errMessage.value = "Please Enter the Email first"

        } else if (!ValidationsUtils.checkEmail(email)) {
            errMessage.value = "Invalid Email. Please try again"

        } else if (TextUtils.isEmpty(password)) {
            errMessage.value = "please Enter the Password first"

        } else if (!ValidationsUtils.checkPassword(password)
            && password.length <= 10
        ) {
            errMessage.value = "Must have at least 10 Characters"

        } else {
            loginRepo.getLoginDetail(email, password, view)


        }
    }
}