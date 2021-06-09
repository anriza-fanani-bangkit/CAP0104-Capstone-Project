package com.example.angkoot.utils

import android.widget.EditText

object EditTextInputUtils {
    const val MIN_USERNAME_LENGTH = 6
    const val MIN_PASSWORD_LENGTH = 8

    fun isUsernameValid(username: String): Boolean =
        username.isNotEmpty() && username.length >= MIN_USERNAME_LENGTH

    fun isPasswordValid(password: String): Boolean =
        password.isNotEmpty() && password.length >= MIN_PASSWORD_LENGTH

    fun isPhoneNumberValid(phoneNumber: String): Boolean =
        phoneNumber.isNotEmpty() && phoneNumber.length in 11..12

    fun setError(editText: EditText, message: String) {
        editText.error = message
    }

    fun clearError(editText: EditText) {
        editText.error = null
    }
}