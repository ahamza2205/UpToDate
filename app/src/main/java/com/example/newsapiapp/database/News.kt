package com.example.newsapiapp.database

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)