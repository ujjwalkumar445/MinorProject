package com.example.minorproject.utils

import android.util.Patterns
import java.util.regex.Pattern

class ValidationsUtils {
    companion object{
        fun checkEmail(email: String): Boolean {
        val Pattern: Pattern = Patterns.EMAIL_ADDRESS
        return Pattern.matcher(email).matches()
    }

    fun checkPassword(password: String?): Boolean {
        val Pattern: Pattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$")
        return Pattern.matcher(password).matches()
    }

    }
}