package com.example.devmart.getuserdata


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository()
    private val _userData = MutableLiveData<ApiResponse>()
    val userData: LiveData<ApiResponse> get() = _userData

    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserData(userId)
                _userData.postValue(response)
            } catch (e: Exception) {
                _userData.postValue(ApiResponse(success = false, data = emptyList(), message = e.message))
            }
        }
    }
}