package com.example.connectfour.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.connectfour.models.Move

@Dao
interface MoveDao {
    @Query("SELECT * FROM moves")
    fun getAllMoves(): List<Move>

    @Query("SELECT * FROM moves WHERE id = :id")
    fun getMoveById(id: Long): Move

    @Insert
    fun insertMove(move: Move): Long

    @Update
    fun updateMove(move: Move)

    @Delete
    fun deleteMove(move: Move)
}