package com.example.angkoot.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityRegisterBinding
import com.example.angkoot.utils.EditTextInputUtils
import com.example.angkoot.utils.ToastUtils
import com.example.angkoot.utils.ext.isAllTrue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeData()
    }

    private fun setupUI() {
        supportActionBar?.hide()

        with(binding) {
            edtPhoneRegister.addTextChangedListener(phoneNumberTextWatcher)
            edtUsernameRegister.addTextChangedListener(usernameTextWatcher)
            edtPasswordRegister.addTextChangedListener(passwordTextWatcher)
            edtConfirmPasswordRegister.addTextChangedListener(confirmPasswordTextWatcher)

            btnSignUp.setOnClickListener {
                ToastUtils.show(applicationContext, getString(R.string.register_success_message))
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            with(binding) {
                with(EditTextInputUtils) {

                    phoneNumberText.observe(this@RegisterActivity) { phoneNumber ->
                        if (!isPhoneNumberValid(phoneNumber)) {
                            setError(
                                edtPhoneRegister,
                                getString(R.string.edt_phone_number_error_message)
                            )
                            validatePhoneNumber(false)
                        } else {
                            clearError(edtPhoneRegister)
                            validatePhoneNumber(true)
                        }
                    }

                    usernameText.observe(this@RegisterActivity) { username ->
                        if (!isUsernameValid(username)) {
                            setError(
                                edtUsernameRegister,
                                getString(R.string.edt_username_error_message)
                            )
                            validateUsername(false)
                        } else {
                            clearError(edtUsernameRegister)
                            validateUsername(true)
                        }
                    }

                    passwordText.observe(this@RegisterActivity) { password ->
                        if (!isPasswordValid(password)) {
                            setError(
                                edtPasswordRegister,
                                getString(R.string.edt_password_error_message)
                            )
                            validatePassword(false)
                        } else {
                            clearError(edtPasswordRegister)
                            validatePassword(true)
                        }
                    }

                    confirmPasswordText.observe(this@RegisterActivity) { confirmPassword ->
                        if (confirmPassword.length >= MIN_PASSWORD_LENGTH &&
                            confirmPassword.equals(edtPasswordRegister.text.toString())
                        ) {
                            validateConfirmPassword(true)
                            clearError(edtConfirmPasswordRegister)
                        } else {
                            setError(
                                edtConfirmPasswordRegister,
                                getString(R.string.edt_confirm_password_error_message)
                            )
                            validateConfirmPassword(false)
                        }
                    }
                }

                areAllInputsValid.observe(this@RegisterActivity) { validState ->
                    btnSignUp.isEnabled = validState.isAllTrue()
                }
            }
        }
    }

    private var phoneNumberTextWatcher: TextWatcher? = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(tmpText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (tmpText != null)
                viewModel.setPhoneNumberText(tmpText.toString())
        }
    }

    private var usernameTextWatcher: TextWatcher? = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(tmpText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (tmpText != null)
                viewModel.setUsernameText(tmpText.toString())
        }
    }

    private var passwordTextWatcher: TextWatcher? = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(tmpText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (tmpText != null)
                viewModel.setPasswordText(tmpText.toString())
        }
    }

    private var confirmPasswordTextWatcher: TextWatcher? = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(tmpText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (tmpText != null)
                viewModel.setConfirmPasswordText(tmpText.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        with(binding) {
            edtPhoneRegister.removeTextChangedListener(phoneNumberTextWatcher)
            phoneNumberTextWatcher = null
            edtUsernameRegister.removeTextChangedListener(usernameTextWatcher)
            usernameTextWatcher = null
            edtPasswordRegister.removeTextChangedListener(passwordTextWatcher)
            passwordTextWatcher = null
            edtConfirmPasswordRegister.removeTextChangedListener(confirmPasswordTextWatcher)
            confirmPasswordTextWatcher = null
        }
    }
}