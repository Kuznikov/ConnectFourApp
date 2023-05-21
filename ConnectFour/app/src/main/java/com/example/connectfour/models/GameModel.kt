package com.example.connectfour.models

data class GameModel(
    val board: Array<IntArray>,
    val currentPlayer: Int,
    val gameOver: Boolean
)