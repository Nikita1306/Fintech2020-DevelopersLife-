package com.example.developerslife

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class GifProperty(
    @SerializedName("description")
    val description: String,
    @SerializedName("gifURL")
    val gifUrlSource: String
)