package com.ridianputra.githubuserapp.api

import com.ridianputra.githubuserapp.response.DetailResponse
import com.ridianputra.githubuserapp.response.ItemsItem
import com.ridianputra.githubuserapp.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getListUsers(@Query("q") q: String): Call<UserResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getListFollowers(@Path("username") username: String): Call<ArrayList<ItemsItem>>

    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getListFollowing(@Path("username") username: String): Call<ArrayList<ItemsItem>>
}