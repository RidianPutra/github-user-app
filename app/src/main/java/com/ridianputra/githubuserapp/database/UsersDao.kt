package com.ridianputra.githubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDao {
    @Insert
    suspend fun insertUser(user: UsersEntity)

    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<UsersEntity>>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE id = :id)")
    suspend fun checkUser(id: Int): Boolean

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int)
}