package com.example.newsapiapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity ("NEWSARTICLE")
data class SavedArticle (

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo ("id")
    val id : Int,

    @ColumnInfo ("description")
    val description: Any,

    @ColumnInfo ("publishedAt")
    val publishedAt: String,

    @ColumnInfo ("source")
    val source: Source,

    @ColumnInfo ("title")
    val title: String,

    @ColumnInfo ("url")
    val url: String,

    @ColumnInfo ("urlToImage")
    val urlToImage: Any
)