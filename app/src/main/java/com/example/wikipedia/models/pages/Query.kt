package com.example.wikipedia.models.pages


import com.google.gson.annotations.SerializedName

data class Query(
    @SerializedName("normalized")
    val normalized: List<Normalized>,
    @SerializedName("pages")
    val pages: List<Page>
)