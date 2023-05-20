package com.example.connectfour.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val player1Id: Long,
    val player2Id: Long,
    val winnerId: Long?,
    val startTime: Long,
    val endTime: Long?
)
