package com.example.connectfour


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.connectfour.data.AppDatabase
import com.example.connectfour.viewmodels.GameViewModel

class DuoBoardAdapter(
    private var winnerTextView: TextView? = null,
    private val context: Context,
    private val gameViewModel: GameViewModel,
    private val player1QueueImageView: ImageView,
    private val player2QueueImageView: ImageView,

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

        val currentPlayer = gameModel?.currentPlayer ?: 1
        val gameOver = gameModel?.gameOver ?: false
        if (!gameOver) {
            player1QueueImageView.visibility = if (currentPlayer == 1) View.VISIBLE else View.INVISIBLE
            player2QueueImageView.visibility = if (currentPlayer == 2) View.VISIBLE else View.INVISIBLE
        }

        button.setOnClickListener {
            val currentPlayer = gameModel?.currentPlayer ?: 1
            val gameOver = gameModel?.gameOver ?: false

            if (!gameOver) {
                val board = gameModel?.board?.copyOf() ?: return@setOnClickListener

                // Check if the column is not filled
                if (board[0][col] == 0) {
                    // Add the coin to the board
                    var i = 5
                    while (i >= 0 && board[i][col] != 0) {
                        i--
                    }
                    board[i][col] = currentPlayer

                    val isWin = checkWin(currentPlayer, i, col, board)
                    gameViewModel.updateBoard(board, 3 - currentPlayer, isWin)

                    if (isWin) {
                        val winner = if (currentPlayer == 1) {
                            "Победил игрок под номером 1"
                        } else {
                            "Победил игрок под номером 2"
                        }
                        winnerTextView?.text = winner
                        winnerTextView?.visibility = View.VISIBLE

                        return@setOnClickListener
                    }

                    // Switch the turn to the other player
                    val nextPlayer = if (currentPlayer == 1) 2 else 1
                    gameViewModel.updateBoard(board, nextPlayer, false)
                } else {
                    Toast.makeText(context, "Эта колонка уже заполнена!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return button
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