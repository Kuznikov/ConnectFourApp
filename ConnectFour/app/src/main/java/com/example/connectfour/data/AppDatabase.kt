package com.example.connectfour.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.connectfour.models.Game
import com.example.connectfour.models.Move
import com.example.connectfour.models.Player
import com.example.connectfour.models.User

@Database(entities = [Game::class, Player::class, Move::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao
    abstract fun moveDao(): MoveDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "connect_four_database"
                ).build().also { instance = it }
            }
        }
    }
}