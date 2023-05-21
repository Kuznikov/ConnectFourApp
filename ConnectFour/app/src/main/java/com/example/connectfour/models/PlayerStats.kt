package com.example.connectfour.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_stats")
data class PlayerStats(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val winPercentage: Float = 0f
)