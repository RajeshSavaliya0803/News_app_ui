package com.sendstory.newsapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sendstory.newsapp.data.NewsItem
import com.sendstory.newsapp.data.NewsPagingSource
import com.sendstory.newsapp.retrofit.NewsAPI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(private  val service:NewsAPI) {

    fun getNews(category: String, country :String) : Flow<PagingData<NewsItem>>{
        return  Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false,
                initialLoadSize = 5
            ),
            pagingSourceFactory = {
                NewsPagingSource(category,country,service)
            }
        ).flow
    }

    suspend fun getNewsDetails(id:String) = service.getNewsDetails(id)
}