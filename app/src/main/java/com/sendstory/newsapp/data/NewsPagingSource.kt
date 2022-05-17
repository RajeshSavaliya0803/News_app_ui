package com.sendstory.newsapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sendstory.newsapp.retrofit.NewsAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NewsPagingSource(private val category: String, private val country: String,private val apiService: NewsAPI) : PagingSource<Int,NewsItem>() {
    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        val pageNumber= params.key ?: 1
        try {
            Log.e("TAG", "In Load: Before fetching", )
            val response = withContext(Dispatchers.IO){
                apiService.getNewsList(category,country,pageNumber)
            }
            val pagedResponse = response.body()
            Log.e("TAG", "In Load: after fetching : ${pagedResponse?.news?.size}", )

            val data = pagedResponse?.news

            val next = if (pageNumber < pagedResponse?.pages!!){
                pageNumber +1
            }else{
                null
            }

            Log.e("TAG", "In Load: Returning =  CurrentPage-${pageNumber}, Data Length-${data?.size}, Next Key-${next}", )
           return LoadResult.Page(
                data = data.orEmpty(),
                prevKey = if(pageNumber == 1) null else pageNumber-1,
                nextKey = next
            )
        }
        catch (e: UnknownHostException){
            Log.e("TAG", "Load Exception: $e ")
            return  LoadResult.Error(e)
        }
        catch (e: SocketTimeoutException){
            Log.e("TAG", "Timeout Exception $e")
            return  LoadResult.Error(e)
        }

    }
}