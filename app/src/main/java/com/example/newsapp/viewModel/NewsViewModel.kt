package com.example.newsapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel (
    val newsRepository : NewsRepository
) : ViewModel (){

    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingPageNUmber = 1
    var breakingNewsResponse : NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchPageNumber = 1
    var searchNewsResponse : NewsResponse? = null

    lateinit var articles : LiveData<PagedList<Article>>
    init {
        getBreakingNews("in")
    }
    private fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingPageNUmber)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
    if (response.isSuccessful){
        response.body()?.let { resultResponse ->
            breakingPageNUmber++
            if (breakingNewsResponse == null){
                breakingNewsResponse = resultResponse
            }else{
                val oldArticles = breakingNewsResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
            return Resource.Success(breakingNewsResponse ?: resultResponse)
        }
    }
        return  Resource.Error(response.message())
    }
    fun getSearchedNews(queryString: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val searchNewsResponse = newsRepository.getSearchNews(queryString, searchPageNumber)
        searchNews.postValue(handleSearchNewsResponse(searchNewsResponse))
    }

    private fun handleSearchNewsResponse(respons: Response<NewsResponse>): Resource<NewsResponse>? {
        if (respons.isSuccessful){
           respons.body()?.let {
               resultResponse ->
               searchPageNumber++
               if (searchNewsResponse == null){
                   searchNewsResponse == resultResponse
               }else{
                   val oldArticles = searchNewsResponse?.articles
                   val newArticles = resultResponse.articles
                   oldArticles?.addAll(newArticles)
               }
               return Resource.Success(searchNewsResponse ?: resultResponse)
           }
        }
        return Resource.Error(respons.message())
    }
    fun insertArticles(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun deleteArticles(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }
    fun getSavedArticles() = newsRepository.getAllArticles()

    fun getBreakingNews() : LiveData<PagedList<Article>>{
        return articles
    }
}

