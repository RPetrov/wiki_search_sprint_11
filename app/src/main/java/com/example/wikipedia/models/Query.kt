package com.example.wikipedia.models

data class Query(
    val search: List<Search>,
    val searchinfo: Searchinfo
)