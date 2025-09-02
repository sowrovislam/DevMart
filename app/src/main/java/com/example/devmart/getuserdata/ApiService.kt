package com.example.devmart.getuserdata


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @GET("get_data.php")
    suspend fun getUserData(@Query("user_id") userId: String): ApiResponse


    @PUT("updatedelateandget.php")
    fun updateRecord(@Body data: ApiResponse.Data): Call<ApiResponse>

    // Delete record
    @DELETE("updatedelateandget.php")
    fun deleteRecord(@Query("id") id: Int): Call<ApiResponse>








}