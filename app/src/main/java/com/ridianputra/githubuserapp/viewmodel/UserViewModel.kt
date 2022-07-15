package com.ridianputra.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ridianputra.githubuserapp.response.ItemsItem
import com.ridianputra.githubuserapp.response.UserResponse
import com.ridianputra.githubuserapp.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel: ViewModel() {

    val users = MutableLiveData<ArrayList<ItemsItem>>()

    fun setFoundUser(query: String) {
        val client = ApiConfig.getApiService().getListUsers(query)
        client.enqueue(object: Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful) {
                    users.postValue(response.body()?.items)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFoundUser(): LiveData<ArrayList<ItemsItem>> = users

    companion object{
        private const val TAG = "UserViewModel"
    }

}