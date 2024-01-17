package com.example.wikipedia.models

import com.google.gson.annotations.SerializedName

data class WikiResponse(
    val batchcomplete: String,

    @SerializedName("continue")
    val continueValue: Continue,

    val query: Query
)