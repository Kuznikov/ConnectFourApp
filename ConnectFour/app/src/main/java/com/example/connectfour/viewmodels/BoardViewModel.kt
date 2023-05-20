package com.example.connectfour.viewmodels

import android.content.Context
import android.widget.Toast
import com.example.connectfour.models.BoardModel

class BoardViewModel(private val context: Context, private val model: BoardModel) {
    fun makeMove(row: Int, col: Int) {
        val isSuccess = model.makeMove(row, col)
        if (isSuccess) {
            if (model.isGameOver()) {
                Toast.makeText(context, "Выиграл игрок", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Этот столбец уже заполнен", Toast.LENGTH_SHORT).show()
        }
    }

    fun getBoard(): Array<IntArray> {
        return model.getBoard()
    }
}
