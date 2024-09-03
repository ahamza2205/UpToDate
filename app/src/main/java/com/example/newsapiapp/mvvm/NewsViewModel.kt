package com.example.newsapiapp.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapiapp.Utils.Companion.updateLiveData
import com.example.newsapiapp.database.News
import com.example.newsapiapp.database.SavedArticle
import com.example.newsapiapp.wrapper.Resource
import kotlinx.coroutines.launch

class NewsViewModel(val newsRepo: NewsRepo, application: Application) :
    AndroidViewModel(application) {

    //adding news data
    val breakingNews: MutableLiveData<Resource<News>> = MutableLiveData()
    val pageNumber = 1
    // get category news
    val categoryNews: MutableLiveData<Resource<News>> = MutableLiveData()
    // to get saved news
    //val getSavedNews = newsRepo.getAllSavedNews()

    init {
        getBreakingNews("eg")

    }

    fun getBreakingNews(code: String) = viewModelScope.launch {
        val response = newsRepo.getBreakingNews(code, pageNumber)
        updateLiveData(response, breakingNews)
    }

    fun getCategory(cat: String) = viewModelScope.launch {
        val response = newsRepo.getCategoryNews(cat)
        updateLiveData(response, categoryNews)

    }

    fun insertArticle(savedArt: SavedArticle) = viewModelScope.launch {


        newsRepo.insertNews(savedArt)
    }

    fun deleteAllArtciles() = viewModelScope.launch {
        newsRepo.deleteAll()
    }
}