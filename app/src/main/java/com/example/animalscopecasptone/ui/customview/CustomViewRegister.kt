package com.example.animalscopecasptone.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import com.example.animalscopecasptone.R
import com.google.android.material.button.MaterialButton


class CustomViewRegister @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtUsername: EditText
    private lateinit var btnRegister: MaterialButton

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val username = edtEmail.text.toString().trim()
            val email = edtPassword.text.toString().trim()
            val password = edtUsername.text.toString().trim()

            if (s.hashCode() == edtPassword.text.hashCode() && !TextUtils.isEmpty(email) && !email.isValidEmail()) {
                edtPassword.error = "Please enter a valid email address"
            } else {
                edtPassword.error = null
            }

            if (s.hashCode() == edtUsername.text.hashCode() && password.isNotEmpty() && password.length < 8) {
                edtUsername.error = "Password must be at least 8 characters"
            } else {
                edtUsername.error = null
            }

            btnRegister.isEnabled = validateForm(username, email, password)
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.activity_custom_view_register, this, true)
        edtEmail = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtEmail)
        edtUsername = findViewById(R.id.edtPassword)
        btnRegister = findViewById(R.id.btn_sign_up)

        edtEmail.addTextChangedListener(textWatcher)
        edtPassword.addTextChangedListener(textWatcher)
        edtUsername.addTextChangedListener(textWatcher)
        btnRegister.isEnabled = true
        btnRegister.setOnClickListener {
            clearFocus()
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
            rootView.requestFocus()
        }
    }

    fun validateForm(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isValidEmail() && password.length >= 8 && password.isNotEmpty()
    }

    private fun String.isValidEmail(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun getUsername(): String {
        return edtUsername.text.toString().trim()
    }

    fun getEmail(): String {
        return edtEmail.text.toString().trim()
    }

    fun getPassword(): String {
        return edtPassword.text.toString().trim()
    }

    fun setOnRegisterClickListener(listener: OnClickListener) {
        btnRegister.setOnClickListener(listener)
    }
}