package com.example.wikipedia.models.pages


import com.google.gson.annotations.SerializedName

data class PageResponse(
    @SerializedName("batchcomplete")
    val batchcomplete: Boolean,
    @SerializedName("query")
    val query: Query
)