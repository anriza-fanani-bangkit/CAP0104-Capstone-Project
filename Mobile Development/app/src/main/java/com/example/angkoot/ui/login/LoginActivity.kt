package com.example.angkoot.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityLoginBinding
import com.example.angkoot.ui.home.HomeActivity
import com.example.angkoot.utils.EditTextInputUtils
import com.example.angkoot.utils.ToastUtils
import com.example.angkoot.utils.ext.isAllTrue

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeData()
    }

    private fun setupUI() {
        supportActionBar?.hide()

        with(binding) {
            edtUsernameLogin.addTextChangedListener(usernameTextWatcher)
            edtPasswordLogin.addTextChangedListener(passwordTextWatcher)

            btnLogin.setOnClickListener {
                ToastUtils.show(applicationContext, getString(R.string.login_success_message))

                with(Intent(applicationContext, HomeActivity::class.java)) {
                    startActivity(this)
                }
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            with(EditTextInputUtils) {
                with(binding) {

                    usernameText.observe(this@LoginActivity) { username ->
                        if (!isUsernameValid(username)) {
                            setError(
                                edtUsernameLogin,
                                getString(R.string.edt_username_error_message)
                            )
                            validateUsername(false)
                        } else {
                            clearError(edtUsernameLogin)
                            validateUsername(true)
                        }
                    }

                    passwordText.observe(this@LoginActivity) { password ->
                        if (!isPasswordValid(password)) {
                            setError(
                                edtPasswordLogin,
                                getString(R.string.edt_password_error_message)
                            )
                            validatePassword(false)
                        } else {
                            clearError(edtPasswordLogin)
                            validatePassword(true)
                        }
                    }

                    areAllInputsValid.observe(this@LoginActivity) { validState ->
                        btnLogin.isEnabled = validState.isAllTrue()
                    }
                }
            }
        }
    }

    private var usernameTextWatcher: TextWatcher? = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(tmpString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (tmpString != null)
                viewModel.setUsernameText(tmpString.toString())
        }
    }

    private var passwordTextWatcher: TextWatcher? = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(tmpString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (tmpString != null)
                viewModel.setPasswordText(tmpString.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding) {
            edtUsernameLogin.removeTextChangedListener(usernameTextWatcher)
            usernameTextWatcher = null
            edtPasswordLogin.removeTextChangedListener(passwordTextWatcher)
            passwordTextWatcher = null
        }
    }
}