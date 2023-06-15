package com.example.animalscopecasptone.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.animalscopecasptone.R
import com.example.animalscopecasptone.databinding.ActivityMainBinding
import com.example.animalscopecasptone.response.login.LoginRequest
import com.example.animalscopecasptone.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCameraX.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        binding.btnGallery.setOnClickListener {
            selectImageFromGallery()
        }

        checkSession()
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_PHOTO_REQUEST_CODE)
    }


    private fun checkSession() {
        val sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)
        val password = sharedPref.getString("password", null)

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            authViewModel.loginUser(LoginRequest(email, password))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "file")
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)

        return file
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                clearSharedPreferences()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearSharedPreferences() {
        val sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        Toast.makeText(this, "Log out successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    companion object {
        private const val SELECT_PHOTO_REQUEST_CODE = 100
    }
}