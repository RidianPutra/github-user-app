package com.ridianputra.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ridianputra.githubuserapp.response.ItemsItem
import com.ridianputra.githubuserapp.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<ItemsItem>>()

    fun setListFollowing(username: String) {
        val client = ApiConfig.getApiService().getListFollowing(username)
        client.enqueue(object: Callback<ArrayList<ItemsItem>>{
            override fun onResponse(call: Call<ArrayList<ItemsItem>>, response: Response<ArrayList<ItemsItem>>) {
                if(response.isSuccessful) {
                    listFollowing.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getListFollowing(): LiveData<ArrayList<ItemsItem>> = listFollowing

    companion object{
        private const val TAG = "FollowingViewModel"
    }
}