package com.example.connectfour.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.connectfour.models.Player

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAllPlayers(): List<Player>

    @Query("SELECT * FROM players WHERE id = :id")
    fun getPlayerById(id: Long): Player

    @Insert
    fun insertPlayer(player: Player): Long

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)
}