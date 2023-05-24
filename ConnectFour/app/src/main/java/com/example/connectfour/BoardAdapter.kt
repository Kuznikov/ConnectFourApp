package com.example.connectfour

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.connectfour.R
import com.example.connectfour.data.AppDatabase
import com.example.connectfour.viewmodels.GameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoardAdapter(
    private var winnerTextView: TextView? = null,
    private val context: Context,
    private val gameViewModel: GameViewModel,
    private val username: String,

) : BaseAdapter() {

    val appDatabase = AppDatabase.getInstance(context)
    val userDao = appDatabase.userDao()

    private var userCoinColor = 0
    private var userMode = 0

    init {
        // Получаем текущего пользователя из базы данных и устанавливаем значение userCoinColor
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUserByUsername(username)
            userCoinColor = user?.coinColor ?: 0
            userMode = user?.mode ?: 0
        }
    }
    override fun getCount(): Int {
        return 42
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val button = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            inflater.inflate(R.layout.board_item, parent, false) as Button
        } else {
            convertView as Button
        }

        val gameModel = gameViewModel.gameModel.value

        val row = position / 7
        val col = position % 7

        val coinDrawable = when (gameModel?.board?.getOrNull(row)?.getOrNull(col) ?: 0) {
            1 -> if (userCoinColor == 0) R.drawable.game_red_coin else R.drawable.game_blue_coin
            2 -> if (userCoinColor == 0) R.drawable.game_blue_coin else R.drawable.game_red_coin
            else -> 0
        }
        button.setCompoundDrawablesWithIntrinsicBounds(0, coinDrawable, 0, 0)

        button.setOnClickListener {
            val currentPlayer = gameModel?.currentPlayer ?: 1
            val gameOver = gameModel?.gameOver ?: false

            if (!gameOver && currentPlayer == 1) {
                val board = gameModel?.board?.copyOf() ?: return@setOnClickListener

                // Check if the column is not filled
                if (board[0][col] == 0) {
                    // Add the coin to the board
                    var i = 5
                    while (i >= 0 && board[i][col] != 0) {
                        i--
                    }
                    board[i][col] = 1

                    val isWin = checkWin(1, i, col, board)
                    gameViewModel.updateBoard(board, 2, isWin)

                    if (isWin) {
                        val winner = "Вы победили!"
                        winnerTextView?.text = winner
                        winnerTextView?.visibility = View.VISIBLE

                        CoroutineScope(Dispatchers.IO).launch {
                            val user = userDao.getUserByUsername(username)
                            user?.let {
                                it.wins++
                                userDao.updateUserWins(it)
                            }
                        }
                        return@setOnClickListener
                    }

                    // Pass the turn to the computer
                    makeComputerMove(gameViewModel)
                } else {
                    Toast.makeText(context, "Эта колонка уже заполнена!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return button
    }

    private fun makeComputerMove(gameViewModel: GameViewModel) {
        val gameModel = gameViewModel.gameModel.value ?: return
        val board = gameModel.board.copyOf()

        if (userMode == 0) {
            // First, check if there is a possibility to win on the next move
            for (col in 0..6) {
                if (board[0][col] == 0) {
                    var row = 5
                    while (row >= 0 && board[row][col] != 0) {
                        row--
                    }
                    board[row][col] = 2
                    if (checkWin(2, row, col, board)) {
                        gameViewModel.updateBoard(board, 1, true)
                        val winner = "Победил компьютер!"
                        winnerTextView?.text = winner
                        winnerTextView?.visibility = View.VISIBLE

                        CoroutineScope(Dispatchers.IO).launch {
                            val user = userDao.getUserByUsername(username)
                            user?.let {
                                it.losses++
                                userDao.updateUserLosses(it)
                            }
                        }
                        return
                    } else {
                        board[row][col] = 0
                    }
                }
            }

            // Then, check if there is a need to block the player
            for (col in 0..6) {
                if (board[0][col] == 0) {
                    var row = 5
                    while (row >= 0 && board[row][col] != 0) {
                        row--
                    }
                    board[row][col] = 1
                    if (checkWin(1, row, col, board)) {
                        board[row][col] = 2
                        gameViewModel.updateBoard(board, 1, false)
                        return
                    } else {
                        board[row][col] = 0
                    }
                }
            }

            // Finally, select a random column, but choose the one where the player has already placed a coin.
            var col = (0..6).random()
            var row = 5
            while (row >= 0 && board[row][col] != 0) {
                row--
            }
            if (row >= 0) {
                board[row][col] = 2
                gameViewModel.updateBoard(board, 1, false)
            } else {
                for (c in 0..6) {
                    if (board[0][c] == 0) {
                        col = c
                        row = 5
                        while (row >= 0 && board[row][col] != 0) {
                            row--
                        }
                        board[row][col] = 2
                        gameViewModel.updateBoard(board, 1, false)
                        return
                    }
                }
            }
        }


        // сложного алгоритма ИИ
        if (userMode == 1) {
            // First, check if there is a possibility to win on the next move
            for (col in 0..6) {
                if (board[0][col] == 0) {
                    var row = 5
                    while (row >= 0 && board[row][col] != 0) {
                        row--
                    }
                    board[row][col] = 2
                    if (checkWin(2, row, col, board)) {
                        gameViewModel.updateBoard(board, 1, true)
                        val winner = "Победил компьютер!"
                        winnerTextView?.text = winner
                        winnerTextView?.visibility = View.VISIBLE

                        CoroutineScope(Dispatchers.IO).launch {
                            val user = userDao.getUserByUsername(username)
                            user?.let {
                                it.losses++
                                userDao.updateUserLosses(it)
                            }
                        }
                        return
                    } else {
                        board[row][col] = 0
                    }
                }
            }

            // Then, check if there is a need to block the player
            for (col in 0..6) {
                if (board[0][col] == 0) {
                    var row = 5
                    while (row >= 0 && board[row][col] != 0) {
                        row--
                    }
                    board[row][col] = 1
                    if (checkWin(1, row, col, board)) {
                        board[row][col] = 2
                        gameViewModel.updateBoard(board, 1, false)
                        return
                    } else {
                        board[row][col] = 0
                    }
                }
            }

            // If no winning or blocking move, try to create a two-move setup for the player
            for (col in 0..6) {
                if (board[0][col] == 0) {
                    var row = 5
                    while (row >= 0 && board[row][col] != 0) {
                        row--
                    }
                    board[row][col] = 2
                    if (checkTwoMoveSetup(1, row, col, board)) {
                        gameViewModel.updateBoard(board, 1, false)
                        return
                    } else {
                        board[row][col] = 0
                    }
                }
            }

            // Finally, select a random column, but choose the one where the player has already placed a coin.
            var col = (0..6).random()
            var row = 5
            while (row >= 0 && board[row][col] != 0) {
                row--
            }
            if (row >= 0) {
                board[row][col] = 2
                gameViewModel.updateBoard(board, 1, false)
            } else {
                for (c in 0..6) {
                    if (board[0][c] == 0) {
                        col = c
                        row = 5
                        while (row >= 0 && board[row][col] != 0) {
                            row--
                        }
                        board[row][col] = 2
                        gameViewModel.updateBoard(board, 1, false)
                        return
                    }
                }
            }
        }

    }

    private fun checkTwoMoveSetup(player: Int, row: Int, col: Int, board: Array<IntArray>): Boolean {
        // Check if there is a possibility to create a two-move setup for the player
        val opponent = if (player == 1) 2 else 1

        // Check horizontal setup
        if (col <= 3) {
            if (board[row][col + 1] == player && board[row][col + 2] == opponent && board[row][col + 3] == player) {
                // Check if the two spaces next to the opponent's coin are empty
                if (row == 5 || (row < 5 && board[row + 1][col + 2] == 0)) {
                    return true
                }
            }
        }
        // Check vertical setup
        if (row <= 2) {
            if (board[row + 1][col] == player && board[row + 2][col] == opponent && board[row + 3][col] == player) {
                return true
            }
        }
        // Check diagonal (/) setup
        if (row >= 3 && col <= 3) {
            if (board[row - 1][col + 1] == player && board[row - 2][col + 2] == opponent && board[row - 3][col + 3] == player) {
                // Check if the two spaces next to the opponent's coin are empty
                if (row == 3 || (row > 3 && board[row - 4][col + 2] == 0)) {
                    return true
                }
            }
        }
        // Check diagonal (\) setup
        if (row >= 3 && col >= 3) {
            if (board[row - 1][col - 1] == player && board[row - 2][col - 2] == opponent && board[row - 3][col - 3] == player) {
                // Check if the two spaces next to the opponent's coin are empty
                if (row == 3 || (row > 3 && board[row - 4][col - 2] == 0)) {
                    return true
                }
            }
        }

        return false
    }

    private fun checkWin(player: Int, row: Int, col: Int, board: Array<IntArray>): Boolean {
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

}