package com.example.minorproject.viewmodel

import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minorproject.repo.SignUpRepo
import com.example.minorproject.utils.ValidationsUtils

class signUpViewModel : ViewModel() {

    private var errMessage = MutableLiveData<String>()
    var mSignUpRepo = SignUpRepo()
    var onSignUpComplete = MutableLiveData<Boolean>()

    fun getSignUpErrMessage(): LiveData<String> {
        return errMessage
    }

    fun onSignUpClicked(
        username: String,
        email: String,
        password: String,
        filePath: Uri?
    ): MutableLiveData<Boolean> {

        if (TextUtils.isEmpty(username)) {
            errMessage.value = "UserName should not be empty"
        } else if (TextUtils.isEmpty(email)) {
            errMessage.value = "Email Address Should not be empty"
        } else if (!ValidationsUtils.checkEmail(email)) {
            errMessage.value = "Invalid Email"
        } else if (TextUtils.isEmpty(password)) {
            errMessage.value = "Password field Empty"
        } else if (!ValidationsUtils.checkPassword(password)) {
            errMessage.value = "Must Have 1 Special Character, 1 Uppercase, 1 Lowercase, 1 Number"
        } else if (password.length <= 10) {
            errMessage.value = "Must have at least 10 digit password"

        } else {

            onSignUpComplete = mSignUpRepo.getSignUpDetail(username, email, password, filePath)


        }
        return onSignUpComplete

    }

}