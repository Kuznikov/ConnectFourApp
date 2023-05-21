package com.example.connectfour.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectfour.models.GameModel

class GameViewModel : ViewModel() {

    private val _gameModel = MutableLiveData<GameModel>()
    val gameModel: LiveData<GameModel> = _gameModel

    init {
        resetGame()
    }

    fun makeMove(row: Int, col: Int) {
        val currentGameModel = _gameModel.value ?: return
        if (currentGameModel.gameOver || currentGameModel.currentPlayer != 1) {
            return
        }

        val board = currentGameModel.board.copyOf()
        if (board[0][col] != 0) {
            return
        }

        var i = 5
        while (i >= 0 && board[i][col] != 0) {
            i--
        }

        board[i][col] = 1
        val gameOver = checkWin(1, i, col)
        val currentPlayer = if (gameOver) 0 else 2

        _gameModel.value = GameModel(board, currentPlayer, gameOver)
        if (!gameOver) {
            makeComputerMove()
        }
    }

    private fun makeComputerMove() {
        val currentGameModel = _gameModel.value ?: return

        for (col in 0..6) {
            if (currentGameModel.board[0][col] == 0) {
                var row = 5
                while (row >= 0 && currentGameModel.board[row][col] != 0) {
                    row--
                }

                val board = currentGameModel.board.copyOf()
                board[row][col] = 2

                if (checkWin(2, row, col)) {
                    _gameModel.value = GameModel(board, 0, true)
                    return
                }
            }
        }

        for (col in 0..6) {
            if (currentGameModel.board[0][col] == 0) {
                var row = 5
                while (row >= 0 && currentGameModel.board[row][col] != 0) {
                    row--
                }

                val board = currentGameModel.board.copyOf()
                board[row][col] = 1

                if (checkWin(1, row, col)) {
                    board[row][col] = 2
                    _gameModel.value = GameModel(board, 1, false)
                    return
                }
            }
        }

        var col = (0..6).random()
        var row = 5
        while (row >= 0 && currentGameModel.board[row][col] != 0) {
            row--
        }

        if (row >= 0) {
            val board = currentGameModel.board.copyOf()
            board[row][col] = 2
            _gameModel.value = GameModel(board, 1, false)
        } else {
            for (c in 0..6) {
                if (currentGameModel.board[0][c] == 0) {
                    col = c
                    row = 5
                    while (row >= 0 && currentGameModel.board[row][col] != 0) {
                        row--
                    }

                    val board = currentGameModel.board.copyOf()
                    board[row][col] = 2
                    _gameModel.value = GameModel(board, 1, false)
                    return
                }
            }
        }
    }

    private fun checkWin(player: Int, row: Int, col: Int): Boolean {
        val board = _gameModel.value?.board ?: return false

        // Check horizontal line
        var count = 0
        for (j in 0..6) {
            if (board[row][j] == player) {
                count++
                if (count == 4) {
                    return true
                }
            } else {
                count = 0
            }
        }

        // Check vertical line
        count = 0
        for (i in 0..5) {
            if (board[i][col] == player) {
                count++
                if (count == 4) {
                    return true
                }
            } else {
                count = 0
            }
        }

        // Check diagonal line from top left to bottom right
        count = 0
        var i = row
        var j = col
        while (i > 0 && j > 0) {
            i--
            j--
        }
        while (i <= 5 && j <= 6) {
            if (board[i][j] == player) {
                count++
                if (count == 4) {
                    return true
                }
            } else {
                count = 0
            }
            i++
            j++
        }

        // Check diagonal line from top right to bottom left
        count = 0
        i = row
        j = col
        while (i > 0 && j < 6) {
            i--
            j++
        }
        while (i <= 5 && j >= 0) {
            if (board[i][j] == player) {
                count++
                if (count == 4) {
                    return true
                }
            } else {
                count = 0
            }
            i++
            j--
        }

        return false
    }

    fun resetGame() {
        val board = Array(6) { IntArray(7) }
        _gameModel.value = GameModel(board, 1, false)
    }

    fun updateBoard(board: Array<IntArray>, currentPlayer: Int, gameOver: Boolean) {
        _gameModel.value = GameModel(board, currentPlayer, gameOver)
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up any resources if needed
    }
}