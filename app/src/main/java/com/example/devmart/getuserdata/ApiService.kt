package com.example.devmart.getuserdata


import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("get_data.php")
    suspend fun getUserData(@Query("user_id") userId: String): ApiResponse
}