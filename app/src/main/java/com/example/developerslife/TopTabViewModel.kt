package com.example.developerslife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class TopTabViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _response = MutableLiveData<List<GifProperty>>()
    val response: LiveData<List<GifProperty>>
        get() = _response
    var randomPage: Int = 0
    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    fun getGif() {


        Api.retrofitService.getProperties("top",randomPage++).enqueue( object : Callback<Result> {
            override fun onFailure(call: Call<Result>, t: Throwable) {
            }

            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                _response.value = response.body()?.result
            }
        })
    }
}