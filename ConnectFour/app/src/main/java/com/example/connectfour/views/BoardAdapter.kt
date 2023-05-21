package com.example.connectfour.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import com.example.connectfour.R
import com.example.connectfour.viewmodels.GameViewModel

class BoardAdapter(    private var winnerTextView: TextView? = null,
                       private val context: Context,
    private val gameViewModel: GameViewModel

) : BaseAdapter() {

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
            1 -> R.drawable.game_red_coin
            2 -> R.drawable.game_blue_coin
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