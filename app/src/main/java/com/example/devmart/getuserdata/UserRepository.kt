package com.example.devmart.getuserdata


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    suspend fun getUserData(userId: String): ApiResponse {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getUserData(userId)
        }
    }
}