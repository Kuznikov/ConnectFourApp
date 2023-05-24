package com.example.connectfour.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connectfour.data.UserDao
import com.example.connectfour.models.GameModel
import com.example.connectfour.models.User
import kotlinx.coroutines.launch

class GameViewModel() : ViewModel() {
    private val _gameModel = MutableLiveData<GameModel>()
    val gameModel: LiveData<GameModel> = _gameModel
    init {
        resetGame()
    }
    fun resetGame() {
        val board = Array(6) { IntArray(7) }
        _gameModel.value = GameModel(board, 1, false)
    }
    fun updateBoard(board: Array<IntArray>, currentPlayer: Int, gameOver: Boolean) {
        _gameModel.value = GameModel(board, currentPlayer, gameOver)
    }
}