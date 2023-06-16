package com.example.animalscopecasptone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.animalscopecasptone.R
import com.example.animalscopecasptone.databinding.ActivityRegisterBinding
import com.example.animalscopecasptone.response.register.RegisterRequest
import com.example.animalscopecasptone.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.customRegister.setOnRegisterClickListener {
            val userName = binding.customRegister.getUsername()
            val email = binding.customRegister.getEmail()
            val password = binding.customRegister.getPassword()

            authViewModel.registerUser(RegisterRequest(userName, email, password ))
        }

        binding.tvAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setupObservers()
    }

    private fun setupObservers() {
        authViewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        authViewModel.registerUser.observe(this) { registerResponse ->
            registerResponse?.let {
                showRegistrationSuccess()
            }
        }

//        authViewModel.isLoading.observe(this) { isLoading ->
//            binding.lottieLoading.visibility =
//                if (isLoading) View.VISIBLE else View.GONE
//        }
    }

    private fun showRegistrationSuccess() {
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}