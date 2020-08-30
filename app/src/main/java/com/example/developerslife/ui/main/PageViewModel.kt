package com.example.developerslife.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.developerslife.Api
import com.example.developerslife.GifProperty
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    fun setIndex(index: Int) {
        _index.value = index
    }
    fun getIndex() : Int {
        return _index.value ?: 0
    }

init {
}
    fun getGif() {
        Api.retrofitService.getProperties().enqueue( object : Callback<GifProperty> {
            override fun onFailure(call: Call<GifProperty>, t: Throwable) {
                //_response.value = "Failure: " + t.message
            }

            override fun onResponse(call: Call<GifProperty>, response: Response<GifProperty>) {
                _response.value = "${response.body()?.gifUrlSource}"
                _description.value = "${response.body()?.description}"
            }
        })
    }
}