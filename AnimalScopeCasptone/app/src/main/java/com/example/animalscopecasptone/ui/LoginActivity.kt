package com.example.animalscopecasptone.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import com.example.animalscopecasptone.R
import com.example.animalscopecasptone.databinding.ActivityLoginBinding
import com.example.animalscopecasptone.response.login.LoginRequest
import com.example.animalscopecasptone.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvDonTHaveAnAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.customLogin.setOnLoginClickListener {
            val email = binding.customLogin.getEmail()
            val password = binding.customLogin.getPassword()

            authViewModel.loginUser(LoginRequest(email, password))
        }
        setupObservers()

        onBackPressedDispatcher.addCallback(this) {
            finishAffinity()
        }
    }

    private fun setupObservers() {
        authViewModel.loginUser.observe(this) { loginUser ->
            loginUser?.let {
                saveCredentials(binding.customLogin.getEmail(), binding.customLogin.getPassword())
                showLoginSuccess()
                val bearerToken = loginUser.token
                Log.d("tokenObserver", "inLoginActivity : $bearerToken ")
                val sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("bearerToken", bearerToken)
                    apply()
                }
            }
        }
    }

    private fun saveCredentials(email: String, password: String) {
        val sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    private fun showLoginSuccess() {
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}