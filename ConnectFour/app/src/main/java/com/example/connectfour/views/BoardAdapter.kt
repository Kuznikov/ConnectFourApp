package com.example.connectfour.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.example.connectfour.R
import com.example.connectfour.viewmodels.BoardViewModel

class BoardAdapter(private val context: Context, private val viewModel: BoardViewModel) : BaseAdapter() {
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
        val row = position / 7
        val col = position % 7
        val board = viewModel.getBoard()
        val value = board[row][col]
        val coinDrawable = when (value) {
            1 -> R.drawable.game_red_coin
            2 -> R.drawable.game_blue_coin
            else -> 0
        }
        button.setCompoundDrawablesWithIntrinsicBounds(0, coinDrawable, 0, 0)
        button.setOnClickListener {
            viewModel.makeMove(row, col)
        }
        return button
    }
}
