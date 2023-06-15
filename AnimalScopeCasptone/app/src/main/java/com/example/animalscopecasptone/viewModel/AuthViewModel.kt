package com.example.animalscopecasptone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalscopecasptone.repository.AuthRepository
import com.example.animalscopecasptone.response.login.LoginRequest
import com.example.animalscopecasptone.response.login.LoginResponse
import com.example.animalscopecasptone.response.register.RegisterRequest
import com.example.animalscopecasptone.response.register.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _registerUser = MutableLiveData<RegisterResponse?>()
    val registerUser: LiveData<RegisterResponse?> = _registerUser

    private val _loginUser = MutableLiveData<LoginResponse?>()
    val loginUser: LiveData<LoginResponse?> = _loginUser

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private fun handleRegistrationError(response: Response<RegisterResponse>) {
        val errorMessage = response.errorBody()?.string()
        if (errorMessage != null) {
            try {
                val errorJson = JSONObject(errorMessage)
                val error = errorJson.getString("message")
                Log.d("registerUserMessage", "error $error")
                _errorMessage.value = error
            } catch (e: JSONException) {
                Log.d("registerUserMessage", "JSON parsing error: ${e.message}")
                _errorMessage.value = "Unexpected error occurred"
            }
        } else {
            _errorMessage.value = "Unexpected error occurred"
        }
    }

    private fun handleLoginError(response: Response<LoginResponse>) {
        val errorMessage = response.errorBody()?.string()
        if (errorMessage != null) {
            try {
                val errorJson = JSONObject(errorMessage)
                val error = errorJson.getString("message")
                Log.d("registerUserMessage", "error $error")
                _errorMessage.value = error
            } catch (e: JSONException) {
                Log.d("registerUserMessage", "JSON parsing error: ${e.message}")
                _errorMessage.value = "Unexpected error occurred"
            }
        } else {
            _errorMessage.value = "Unexpected error occurred"
        }
    }

    fun registerUser(registerRequest: RegisterRequest) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authRepository.registerUser(registerRequest)
            }.onSuccess { response ->
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _registerUser.value = response.body()
                    } else {
                        handleRegistrationError(response)
                    }
                }
            }.onFailure { e ->
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message
                }
            }.also {
                withContext(Dispatchers.Main) {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun loginUser(loginUserRequestBody: LoginRequest) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authRepository.loginUser(loginUserRequestBody)
            }.onSuccess { response ->
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _loginUser.value = response.body()
                    } else {
                        handleLoginError(response)
                    }
                }
            }.onFailure { e ->
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message
                }
            }.also {
                withContext(Dispatchers.Main) {
                    _isLoading.postValue(false)
                }
            }
        }
    }
}