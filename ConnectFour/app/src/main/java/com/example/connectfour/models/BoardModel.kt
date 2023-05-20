package com.example.connectfour.models

import android.content.Context
import android.widget.Toast

class BoardModel(private val context: Context, private val listener: OnBoardChangeListener) {
    private val board = Array(6) { IntArray(7) }
    private var currentPlayer = 1 // 1 - игрок, 2 - компьютер
    private var gameOver = false

    fun getBoard(): Array<IntArray> {
        return board
    }

    fun getCurrentPlayer(): Int {
        return currentPlayer
    }

    fun isGameOver(): Boolean {
        return gameOver
    }

    fun makeMove(row: Int, col: Int): Boolean {
        if (!gameOver && currentPlayer == 1) {
            // проверяем, что столбец не заполнен
            if (board[0][col] == 0) {
                // добавляем монетку на доску
                var i = 5
                while (i >= 0 && board[i][col] != 0) {
                    i--
                }
                board[i][col] = 1
                // проверяем выигрыш игрока
                if (checkWin(1, i, col)) {
                    gameOver = true
                    return true
                }
                // передаем ход компьютеру
                currentPlayer = 2
                makeComputerMove()
                return true
            }
        }
        return false
    }

    private fun makeComputerMove() {
        // Сначала ищем возможность выиграть за следующий ход
        for (col in 0..6) {
            if (board[0][col] == 0) {
                // проверяем, можно ли поставить монетку компьютеру так, чтобы у него получилась линия из четырех монеток
                var row = 5
                while (row >= 0 && board[row][col] != 0) {
                    row--
                }
                board[row][col] = 2
                if (checkWin(2, row, col)) {
                    gameOver = true
                    Toast.makeText(context, "Выиграл компьютер", Toast.LENGTH_SHORT).show()
                    return
                } else {
                    board[row][col] = 0
                }
            }
        }
        // Затем ищем возможность блокировать игрока
        for (col in 0..6) {
            if (board[0][col] == 0) {
                // проверяем, можно ли поставить монетку игроку так, чтобы у него получилась линия из четырех монеток
                var row = 5
                while (row >= 0 && board[row][col] != 0) {
                    row--
                }
                board[row][col] = 1
                if (checkWin(1, row, col)) {
                    // блокируем игрока
                    board[row][col] = 2
                    listener.onBoardChanged()
                    currentPlayer = 1
                    return
                } else {
                    board[row][col] = 0
                }
            }
        }

        // Наконец, выбираем столбец случайным образом, но так, чтобы компьютер выбирал тот столбец, в котором уже есть монетка игрока.
        var col = (0..6).random()
        var row = 5
        while (row >= 0 && board[row][col] != 0) {
            row--
        }
        if (row >= 0) {
            board[row][col] = 2
            listener.onBoardChanged()
            currentPlayer = 1
        } else {
            // если столбец полностью заполнен, выбираем другой столбец
            for (c in 0..6) {
                if (board[0][c] == 0) {
                    col = c
                    row = 5
                    while (row >= 0 && board[row][col] != 0) {
                        row--
                    }
                    board[row][col] = 2
                    listener.onBoardChanged()
                    currentPlayer = 1
                    break
                }
            }
        }
    }

    private fun checkWin(player: Int, row: Int, col: Int): Boolean {
        // проверяем горизонтальную линию
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
        // проверяем вертикальную линию
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
        // проверяем диагональную линию слева направо
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
        // проверяем диагональную линию справа налево
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

    interface OnBoardChangeListener {
        fun onBoardChanged()
    }
}
