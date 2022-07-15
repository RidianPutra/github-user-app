package com.ridianputra.githubuserapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ridianputra.githubuserapp.response.DetailResponse
import com.ridianputra.githubuserapp.api.ApiConfig
import com.ridianputra.githubuserapp.database.UsersEntity
import com.ridianputra.githubuserapp.database.UsersRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): AndroidViewModel(application) {

    private val usersRepository: UsersRepository = UsersRepository(application)
    val users = MutableLiveData<DetailResponse>()

    fun setDetailUser(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object: Callback<DetailResponse>{
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if(response.isSuccessful) {
                    users.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailUser(): LiveData<DetailResponse> = users

    fun insertUser(id: Int, login: String, avatarUrl: String) {
        viewModelScope.launch {
            val user = UsersEntity(id, login, avatarUrl)
            usersRepository.insertUser(user)
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            usersRepository.deleteUser(id)
        }
    }

    suspend fun checkUser(id: Int) = usersRepository.checkUser(id)

    companion object{
        private const val TAG = "DetailViewModel"
    }

}