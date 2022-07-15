package com.ridianputra.githubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ridianputra.githubuserapp.database.UsersEntity
import com.ridianputra.githubuserapp.database.UsersRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val usersRepository: UsersRepository = UsersRepository(application)

    fun getUsers(): LiveData<List<UsersEntity>> = usersRepository.getUsers()

}