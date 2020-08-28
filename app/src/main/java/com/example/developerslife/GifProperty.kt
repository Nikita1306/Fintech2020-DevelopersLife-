package com.example.developerslife

import com.squareup.moshi.Json

data class GifProperty(
    @Json(name = "description")
    val description: String,
    @Json(name = "gifURL")
    val gifUrlSource: String
)