package com.example.connectfour.viewmodels

import com.example.connectfour.data.UserDao
import com.example.connectfour.models.User

class AuthManager(private val userDao: UserDao) {
    suspend fun register(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun login(username: String, password: String): User? {
        return userDao.getUserByUsernameAndPassword(username, password)
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}