package com.example.angkoot.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _usernameText = MutableLiveData<String>()
    val usernameText: LiveData<String> get() = _usernameText
    private val _passwordText = MutableLiveData<String>()
    val passwordText: LiveData<String> get() = _passwordText

    var areAllInputsValid = MutableLiveData(listOf(false, false))

    fun setUsernameText(newString: String) {
        _usernameText.value = newString
    }

    fun setPasswordText(newString: String) {
        _passwordText.value = newString
    }

    fun validateUsername(valid: Boolean) {
        areAllInputsValid.value = areAllInputsValid.value?.mapIndexed { index, validState ->
            if (index == 0) valid
            else validState
        }
    }

    fun validatePassword(valid: Boolean) {
        areAllInputsValid.value = areAllInputsValid.value?.mapIndexed { index, validState ->
            if (index == 1) valid
            else validState
        }
    }
}