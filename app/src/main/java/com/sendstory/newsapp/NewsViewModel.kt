package com.sendstory.newsapp

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.sendstory.newsapp.data.NewsDetailModel
import com.sendstory.newsapp.repository.Repository
import com.sendstory.newsapp.retrofit.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: Repository)  : ViewModel(){

    private  val  _responseLiveData: MutableLiveData<ApiResponse<NewsDetailModel>> = MutableLiveData()

    val responseObserve get() = _responseLiveData

    fun fetchNews(category:String,country: String) = repository.getNews(category,country).cachedIn(viewModelScope)

    fun getNewsDetail(id:String) {
        _responseLiveData.value = ApiResponse.Loading()
        try {
            viewModelScope.launch(Dispatchers.IO){
                val response = repository.getNewsDetails(id)
                withContext(Dispatchers.Main){
                    if(response.code() == 200){
                        _responseLiveData.value = ApiResponse.Success(response.body()!!)
                    }else{
                        _responseLiveData.value = ApiResponse.Error(response.message())
                    }
                }
            }

        } catch (e: Exception) {
            _responseLiveData.value = ApiResponse.Error("Exception Caught when fetching request")
        }
    }
}