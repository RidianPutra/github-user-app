package com.ridianputra.githubuserapp.database

import android.app.Application
import androidx.lifecycle.LiveData

class UsersRepository(application: Application) {
    private val usersDao: UsersDao

    init {
        val db = UsersDatabase.getDatabase(application)
        usersDao = db.usersDao()
    }

    fun getUsers(): LiveData<List<UsersEntity>> = usersDao.getUsers()

    suspend fun insertUser(user: UsersEntity) { usersDao.insertUser(user) }

    suspend fun checkUser(id: Int) = usersDao.checkUser(id)

    suspend fun deleteUser(id: Int) { usersDao.deleteUser(id) }
}