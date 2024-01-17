package com.example.wikipedia.models.pages


import com.google.gson.annotations.SerializedName

data class Page(
    @SerializedName("extract")
    val extract: String,
    @SerializedName("ns")
    val ns: Int,
    @SerializedName("pageid")
    val pageid: Int,
    @SerializedName("title")
    val title: String
)