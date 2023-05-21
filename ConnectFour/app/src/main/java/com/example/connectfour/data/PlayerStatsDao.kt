package com.example.connectfour.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.connectfour.models.PlayerStats

@Dao
interface PlayerStatsDao {
    @Query("SELECT * FROM player_stats LIMIT 1")
    fun getPlayerStats(): LiveData<PlayerStats?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlayerStats(playerStats: PlayerStats)
}