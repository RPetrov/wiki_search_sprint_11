package com.example.wikipedia

import com.example.wikipedia.models.Models
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiService {

    // action=query&list=search&srsearch=%D0%BC%D0%BE%D1%81%D0%BA%D0%B2%D0%B0&utf8=&format=json
    @GET("marvel")
    open fun search(
        @Query("action") action: String = "query",
        @Query("list") list: String = "search",
        @Query("format") format: String = "json",
        @Query("srsearch")srsearch: String
    ): Call<Models?>?

}