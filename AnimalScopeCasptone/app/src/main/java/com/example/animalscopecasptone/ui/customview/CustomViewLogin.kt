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

class CustomViewLogin @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: MaterialButton

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btnLogin.isEnabled = true
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (s.hashCode() == edtEmail.text.hashCode() && !TextUtils.isEmpty(email) && !email.isValidEmail()) {
                edtEmail.error = "Please enter a valid email address"
            } else {
                edtEmail.error = null
            }

            if (s.hashCode() == edtPassword.text.hashCode() && password.isNotEmpty() && password.length < 8) {
                edtPassword.error = "Password must be at least 8 characters"
            } else {
                edtPassword.error = null
            }

            if (validateForm(email, password)) {
                btnLogin.isEnabled = true
            } else {
                btnLogin.isEnabled = false
            }
            btnLogin.isEnabled = validateForm(email, password)
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.costum_view_login, this, true)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btn_sign_in)

        edtEmail.addTextChangedListener(textWatcher)
        edtPassword.addTextChangedListener(textWatcher)

        btnLogin.isEnabled = true
        btnLogin.setOnClickListener {
            clearFocus()
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
            rootView.requestFocus()
        }
    }

    fun validateForm(email: String, password: String): Boolean {
        return email.isValidEmail() && password.length >= 8
    }

    private fun String.isValidEmail(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun getEmail(): String {
        return edtEmail.text.toString().trim()
    }

    fun getPassword(): String {
        return edtPassword.text.toString().trim()
    }

    fun setOnLoginClickListener(listener: OnClickListener) {
        btnLogin.setOnClickListener(listener)
    }
}