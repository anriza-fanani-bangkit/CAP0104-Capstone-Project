package com.example.angkoot.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityRegisterBinding
import com.example.angkoot.domain.model.User
import com.example.angkoot.utils.EditTextInputUtils
import com.example.angkoot.utils.ext.isAllTrue
import com.example.angkoot.utils.ext.text
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var database: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFirebaseDatabase()
        setupUI()
        observeData()
    }

    private fun setupFirebaseDatabase() {
        database = FirebaseDatabase.getInstance().reference
        usersRef = FirebaseDatabase.getInstance().getReference("users")
    }

    private fun setupUI() {
        supportActionBar?.hide()

        with(binding) {
            edtPhoneRegister.addTextChangedListener(phoneNumberTextWatcher)
            edtUsernameRegister.addTextChangedListener(usernameTextWatcher)
            edtPasswordRegister.addTextChangedListener(passwordTextWatcher)
            edtConfirmPasswordRegister.addTextChangedListener(confirmPasswordTextWatcher)

            btnSignUp.setOnClickListener { register() }
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

    private fun register() {
        with(binding) {
            val newUser = User(
                edtPhoneRegister.text(),
                edtUsernameRegister.text(),
                edtPasswordRegister.text()
            )

            Log.d("Hehe", "user: $newUser")

            val userId = usersRef.push().key?.filterIndexed { index, char -> index != 0 } ?: ""

            Log.d("Hehe", "userId: $userId")

            database.child("users").child(userId).setValue(newUser)
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