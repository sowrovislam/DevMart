package com.example.devmart.userupload

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("image.php") // Replace with your endpoint
    fun uploadData(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("numbar") numbar: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("description") description: RequestBody,
        @Part("user_id") user_id: RequestBody
    ): Call<UploadResponse>
}