package com.example.connectfour.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String,
    var wins: Int,
    var losses: Int,
    var playerOne: String,
    var playerTwo: String,
    var coinColor: Int,
    var timer: Int,
    var mode: Int
)