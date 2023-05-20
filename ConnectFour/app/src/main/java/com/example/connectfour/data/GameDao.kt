package com.example.connectfour.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.connectfour.models.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): List<Game>

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameById(id: Long): Game

    @Insert
    fun insertGame(game: Game): Long

    @Update
    fun updateGame(game: Game)

    @Delete
    fun deleteGame(game: Game)
}