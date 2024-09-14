package com.example.flashfeed.mvvm
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.newsapiapp.mvvm.NewsDatabase
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapiapp.db.SavedArticle
import com.example.newsapiapp.db.Source
import com.example.newsapiapp.mvvm.NewsDao
import getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NewsDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var newsDao: NewsDao
    private lateinit var database: NewsDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).allowMainThreadQueries().build()
        newsDao = database.newsDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insert() = runBlocking {
        val source = Source(id = "43", name = "techcrunch")
        val article = SavedArticle(id = 1, "description", "url", source, "title", "urlToImage", "publishedAt")

        // Inserting the article
        newsDao.insertNews(article)

        val allArticles = newsDao.getAllNews().getOrAwaitValue()

        val insertedArticle = allArticles.first()
        assertThat(insertedArticle.id).isEqualTo(article.id)

        assertThat(insertedArticle.description).isEqualTo(article.description)
        assertThat(insertedArticle.publishedAt).isEqualTo(article.publishedAt)
        assertThat(insertedArticle.title).isEqualTo(article.title)
        assertThat(insertedArticle.url).isEqualTo(article.url)
        assertThat(insertedArticle.urlToImage).isEqualTo(article.urlToImage)
    }

    @Test
    fun getNewsById() = runBlocking {
        val source = Source(id = "43", name = "techcrunch")
        val article = SavedArticle(id = 1, "description", "url", source, "title", "urlToImage", "publishedAt")

        // Insert the article
        newsDao.insertNews(article)

        // Fetch the article by its ID
        val fetchedArticle = newsDao.getNewsById().getOrAwaitValue()

        // Validate the article data
        assertThat(fetchedArticle.id).isEqualTo(article.id)
        assertThat(fetchedArticle.description).isEqualTo(article.description)
        assertThat(fetchedArticle.publishedAt).isEqualTo(article.publishedAt)
        assertThat(fetchedArticle.title).isEqualTo(article.title)
        assertThat(fetchedArticle.url).isEqualTo(article.url)
        assertThat(fetchedArticle.urlToImage).isEqualTo(article.urlToImage)
    }
    @Test
    fun deleteAllArticles() = runBlocking {
        val source = Source(id = "43", name = "techcrunch")
        val article = SavedArticle(id = 1, "description", "url", source, "title", "urlToImage", "publishedAt")

        newsDao.insertNews(article)

        newsDao.delteAll()

        val allArticles = newsDao.getAllNews().getOrAwaitValue()
        assertThat(allArticles).isEmpty()
    }

}