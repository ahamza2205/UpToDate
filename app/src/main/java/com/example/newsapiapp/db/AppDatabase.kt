package com.example.newsapiapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapiapp.mvvm.NewsDao

@Database(entities = [SavedArticle::class], version = 1)
@TypeConverters(ClassConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedArticleDao(): NewsDao
}
