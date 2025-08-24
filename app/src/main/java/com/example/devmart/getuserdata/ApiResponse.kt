package com.example.devmart.getuserdata


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
    @SerializedName("success")
    val success: Boolean? = null,
    val message: String?
) {
    data class Data(
        @SerializedName("amount")
        val amount: String? = null,
        @SerializedName("date")
        val date: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("image")
        val image: String? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("numbar")
        val numbar: String? = null,
        @SerializedName("user_id")
        val userId: String? = null
    )
}