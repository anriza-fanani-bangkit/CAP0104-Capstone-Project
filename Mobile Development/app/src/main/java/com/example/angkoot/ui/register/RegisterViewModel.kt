package com.example.angkoot.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val _phoneNumberText = MutableLiveData<String>()
    private val _usernameText = MutableLiveData<String>()
    private val _passwordText = MutableLiveData<String>()
    private val _confirmPasswordText = MutableLiveData<String>()

    val phoneNumberText: LiveData<String> get() = _phoneNumberText
    val usernameText: LiveData<String> get() = _usernameText
    val passwordText: LiveData<String> get() = _passwordText
    val confirmPasswordText: LiveData<String> get() = _confirmPasswordText

    var areAllInputsValid = MutableLiveData(listOf(false, false, false, false))

    fun setPhoneNumberText(newString: String) {
        _phoneNumberText.value = newString
    }

    fun setUsernameText(newString: String) {
        _usernameText.value = newString
    }

    fun setPasswordText(newString: String) {
        _passwordText.value = newString
    }

    fun setConfirmPasswordText(newString: String) {
        _confirmPasswordText.value = newString
    }

    fun validatePhoneNumber(valid: Boolean) {
        areAllInputsValid.value = areAllInputsValid.value?.mapIndexed { index, validState ->
            if (index == 0) valid
            else validState
        }
    }

    fun validateUsername(valid: Boolean) {
        areAllInputsValid.value = areAllInputsValid.value?.mapIndexed { index, validState ->
            if (index == 1) valid
            else validState
        }
    }

    fun validatePassword(valid: Boolean) {
        areAllInputsValid.value = areAllInputsValid.value?.mapIndexed { index, validState ->
            if (index == 2) valid
            else validState
        }
    }

    fun validateConfirmPassword(valid: Boolean) {
        areAllInputsValid.value = areAllInputsValid.value?.mapIndexed { index, validState ->
            if (index == 3) valid
            else validState
        }
    }
}