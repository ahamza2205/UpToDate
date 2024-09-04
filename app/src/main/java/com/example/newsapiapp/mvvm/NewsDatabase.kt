package com.example.newsapiapp.mvvm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapiapp.db.ClassConverters
import com.example.newsapiapp.db.SavedArticle

@Database(entities = [SavedArticle::class], version = 1)
@TypeConverters(ClassConverters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract val newsDao: NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
