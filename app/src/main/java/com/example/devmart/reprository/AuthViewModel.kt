package com.example.devmart.reprository



import androidx.lifecycle.*

import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    val authState = MutableLiveData<String>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repo.login(email, password)
            authState.value = result.fold(onSuccess = { "Login Success" }, onFailure = { it.message ?: "Login Failed" })
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            val result = repo.signup(email, password)
            authState.value = result.fold(onSuccess = { "Signup Success" }, onFailure = { it.message ?: "Signup Failed" })
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            val result = repo.sendResetPassword(email)
            authState.value = result.fold(onSuccess = { "Reset Link Sent" }, onFailure = { it.message ?: "Failed to Send Email" })
        }
    }
}
