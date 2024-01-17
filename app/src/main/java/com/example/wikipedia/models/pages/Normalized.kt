package com.example.wikipedia.models.pages


import com.google.gson.annotations.SerializedName

data class Normalized(
    @SerializedName("from")
    val from: String,
    @SerializedName("fromencoded")
    val fromencoded: Boolean,
    @SerializedName("to")
    val to: String
)