package com.example.newsapp.repository

import com.example.newsapp.model.Article
import com.example.newsapp.repository.db.ArticleDatabase
import com.example.newsapp.repository.service.RetrofitClient


data class NewsRepository(
    val db : ArticleDatabase
){
    suspend fun getBreakingNews(contryCode : String, pageNumber : Int) =
        RetrofitClient.api.getBreakingNews(contryCode, pageNumber)

    suspend fun getSearchNews(q : String, pageNumber : Int) =
        RetrofitClient.api.getSerachNews(q, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDoa().insert(article)
    suspend fun delete(article: Article) = db.getArticleDoa().deleteArticle(article)

    fun getAllArticles() = db.getArticleDoa().getArticles()

}
