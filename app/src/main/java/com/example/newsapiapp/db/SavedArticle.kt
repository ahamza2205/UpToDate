package com.example.newsapiapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NEWSARTICLE")
data class SavedArticle(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0, // Default value to allow for auto-generate

    @ColumnInfo(name = "description")
    val description: String, // Changed from Any to String

    @ColumnInfo(name = "publishedAt")
    val publishedAt: String,

    @ColumnInfo(name = "source")
    val source: Source?, // Ensure Source has a Type Converter

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "urlToImage")
    val urlToImage: String // Changed from Any to String
)
