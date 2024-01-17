package com.example.wikipedia

import com.example.wikipedia.models.WikiResponse
import com.example.wikipedia.models.pages.PageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WikiService {


    @GET("api.php")
    fun search(
        @Query("action") action: String = "query",
        @Query("list") list: String = "search",
        @Query("srsearch") query: String,
        @Query("utf8") utf8: String = "",
        @Query("format") format: String = "json"
    ): Call<WikiResponse?>?

    // action=query&prop=extracts&exsentences=10&exlimit=1&titles=Pet_door&explaintext=1&formatversion=2
    @GET("api.php")
    fun getPage(
        @Query("action") action: String = "query",
        @Query("formatversion") formatversion: String = "2",
        @Query("prop") prop: String = "extracts",
        @Query("exsentences") rvprop: String = "10",
        @Query("exlimit") rvslots: String = "1",
        @Query("utf8") utf8: String = "",
        @Query("format") format: String = "json",
        @Query("titles") titles: String,

        ): Call<PageResponse?>?

}