package com.sendstory.newsapp.retrofit

import com.sendstory.newsapp.model.NewsDetailModel
import com.sendstory.newsapp.model.NewsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("index.php?system=news&format=mobile")
    suspend fun getNewsList(@Query("category") category: String, @Query("country") country: String, @Query("page") page: String): Response<NewsModel>

    @GET("index.php?system=news&format=mobile&action=detail")
    suspend fun getNewsDetails(@Query("id") id: String): Response<NewsDetailModel>

}