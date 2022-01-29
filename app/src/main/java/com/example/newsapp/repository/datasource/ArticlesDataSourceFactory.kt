package com.example.newsapp.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.newsapp.model.Article
import kotlinx.coroutines.CoroutineScope

class ArticlesDataSourceFactory (private val scope: CoroutineScope) : DataSource.Factory<Int, Article>(){
    val articlesDataSourceLiveData = MutableLiveData<ArticleDataSource>()
    override fun create(): DataSource<Int, Article> {
        val newArticleDataSource = ArticleDataSource(scope)
        articlesDataSourceLiveData.postValue(newArticleDataSource)
        return newArticleDataSource
    }

}