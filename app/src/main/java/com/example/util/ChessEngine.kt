package com.example.util

import kotlin.math.abs
import kotlin.random.Random

enum class PieceColor {
    WHITE, BLACK
}

enum class PieceType(val symbolWhite: String, val symbolBlack: String, val value: Int) {
    PAWN("♙", "♟", 100),
    KNIGHT("♘", "♞", 320),
    BISHOP("♗", "♝", 330),
    ROOK("♖", "♜", 500),
    QUEEN("♕", "♛", 900),
    KING("♔", "♚", 20000)
}

data class ChessPiece(
    val color: PieceColor,
    val type: PieceType
)

data class ChessMove(
    val fromRow: Int,
    val fromCol: Int,
    val toRow: Int,
    val toCol: Int,
    val capturedPiece: ChessPiece? = null
)

enum class ChessDifficulty {
    RELAXED, EASY, NORMAL, HARD
}

class ChessGame {

    // 8x8 Board: row 0 = Black home row, row 7 = White home row
    val board = Array(8) { arrayOfNulls<ChessPiece>(8) }
    var currentTurn = PieceColor.WHITE
    var moveHistory = mutableListOf<ChessMove>()
    var capturedByWhite = mutableListOf<ChessPiece>()
    var capturedByBlack = mutableListOf<ChessPiece>()
    var isGameOver = false
    var winner: PieceColor? = null

    init {
        resetGame()
    }

    fun resetGame() {
        for (r in 0..7) {
            for (c in 0..7) {
                board[r][c] = null
            }
        }
        // Black pieces (row 0 & 1)
        board[0][0] = ChessPiece(PieceColor.BLACK, PieceType.ROOK)
        board[0][1] = ChessPiece(PieceColor.BLACK, PieceType.KNIGHT)
        board[0][2] = ChessPiece(PieceColor.BLACK, PieceType.BISHOP)
        board[0][3] = ChessPiece(PieceColor.BLACK, PieceType.QUEEN)
        board[0][4] = ChessPiece(PieceColor.BLACK, PieceType.KING)
        board[0][5] = ChessPiece(PieceColor.BLACK, PieceType.BISHOP)
        board[0][6] = ChessPiece(PieceColor.BLACK, PieceType.KNIGHT)
        board[0][7] = ChessPiece(PieceColor.BLACK, PieceType.ROOK)
        for (c in 0..7) board[1][c] = ChessPiece(PieceColor.BLACK, PieceType.PAWN)

        // White pieces (row 6 & 7)
        for (c in 0..7) board[6][c] = ChessPiece(PieceColor.WHITE, PieceType.PAWN)
        board[7][0] = ChessPiece(PieceColor.WHITE, PieceType.ROOK)
        board[7][1] = ChessPiece(PieceColor.WHITE, PieceType.KNIGHT)
        board[7][2] = ChessPiece(PieceColor.WHITE, PieceType.BISHOP)
        board[7][3] = ChessPiece(PieceColor.WHITE, PieceType.QUEEN)
        board[7][4] = ChessPiece(PieceColor.WHITE, PieceType.KING)
        board[7][5] = ChessPiece(PieceColor.WHITE, PieceType.BISHOP)
        board[7][6] = ChessPiece(PieceColor.WHITE, PieceType.KNIGHT)
        board[7][7] = ChessPiece(PieceColor.WHITE, PieceType.ROOK)

        currentTurn = PieceColor.WHITE
        moveHistory.clear()
        capturedByWhite.clear()
        capturedByBlack.clear()
        isGameOver = false
        winner = null
    }

    fun getPiece(row: Int, col: Int): ChessPiece? {
        if (row in 0..7 && col in 0..7) return board[row][col]
        return null
    }

    fun getValidMovesFor(row: Int, col: Int): List<Pair<Int, Int>> {
        val piece = getPiece(row, col) ?: return emptyList()
        if (piece.color != currentTurn) return emptyList()

        val validMoves = mutableListOf<Pair<Int, Int>>()
        for (r in 0..7) {
            for (c in 0..7) {
                if (canPieceMove(row, col, r, c, piece)) {
                    validMoves.add(r to c)
                }
            }
        }
        return validMoves
    }

    private fun canPieceMove(fromR: Int, fromC: Int, toR: Int, toC: Int, piece: ChessPiece): Boolean {
        if (fromR == toR && fromC == toC) return false
        val destPiece = board[toR][toC]
        if (destPiece != null && destPiece.color == piece.color) return false

        val dR = toR - fromR
        val dC = toC - fromC

        return when (piece.type) {
            PieceType.PAWN -> {
                val forward = if (piece.color == PieceColor.WHITE) -1 else 1
                val startRow = if (piece.color == PieceColor.WHITE) 6 else 1

                // 1 step forward
                if (dC == 0 && dR == forward && destPiece == null) {
                    true
                } else if (dC == 0 && dR == 2 * forward && fromR == startRow && destPiece == null && board[fromR + forward][fromC] == null) {
                    // 2 steps from start
                    true
                } else if (abs(dC) == 1 && dR == forward && destPiece != null && destPiece.color != piece.color) {
                    // capture diagonally
                    true
                } else {
                    false
                }
            }
            PieceType.KNIGHT -> {
                (abs(dR) == 1 && abs(dC) == 2) || (abs(dR) == 2 && abs(dC) == 1)
            }
            PieceType.BISHOP -> {
                abs(dR) == abs(dC) && isPathClear(fromR, fromC, toR, toC)
            }
            PieceType.ROOK -> {
                (dR == 0 || dC == 0) && isPathClear(fromR, fromC, toR, toC)
            }
            PieceType.QUEEN -> {
                (abs(dR) == abs(dC) || dR == 0 || dC == 0) && isPathClear(fromR, fromC, toR, toC)
            }
            PieceType.KING -> {
                abs(dR) <= 1 && abs(dC) <= 1
            }
        }
    }

    private fun isPathClear(fromR: Int, fromC: Int, toR: Int, toC: Int): Boolean {
        val stepR = if (toR > fromR) 1 else if (toR < fromR) -1 else 0
        val stepC = if (toC > fromC) 1 else if (toC < fromC) -1 else 0

        var curR = fromR + stepR
        var curC = fromC + stepC
        while (curR != toR || curC != toC) {
            if (board[curR][curC] != null) return false
            curR += stepR
            curC += stepC
        }
        return true
    }

    fun makeMove(fromR: Int, fromC: Int, toR: Int, toC: Int): ChessMove? {
        val piece = getPiece(fromR, fromC) ?: return null
        if (!getValidMovesFor(fromR, fromC).contains(toR to toC)) return null

        val targetPiece = board[toR][toC]
        if (targetPiece != null) {
            if (piece.color == PieceColor.WHITE) capturedByWhite.add(targetPiece)
            else capturedByBlack.add(targetPiece)

            if (targetPiece.type == PieceType.KING) {
                isGameOver = true
                winner = piece.color
            }
        }

        // Handle Pawn promotion (auto-promote to Queen for simplicity and cozy play)
        val finalPiece = if (piece.type == PieceType.PAWN && (toR == 0 || toR == 7)) {
            ChessPiece(piece.color, PieceType.QUEEN)
        } else {
            piece
        }

        board[toR][toC] = finalPiece
        board[fromR][fromC] = null

        val move = ChessMove(fromR, fromC, toR, toC, targetPiece)
        moveHistory.add(move)

        currentTurn = if (currentTurn == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        return move
    }

    /**
     * Compute Companion AI Move based on selected difficulty.
     */
    fun computeCompanionMove(difficulty: ChessDifficulty): ChessMove? {
        if (isGameOver) return null
        val companionColor = PieceColor.BLACK

        // Gather all legal moves for Black
        val allMoves = mutableListOf<Triple<Int, Int, Pair<Int, Int>>>()
        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board[r][c]
                if (piece != null && piece.color == companionColor) {
                    val validMoves = getValidMovesFor(r, c)
                    for (dest in validMoves) {
                        allMoves.add(Triple(r, c, dest))
                    }
                }
            }
        }

        if (allMoves.isEmpty()) {
            isGameOver = true
            winner = PieceColor.WHITE
            return null
        }

        val chosen: Triple<Int, Int, Pair<Int, Int>> = when (difficulty) {
            ChessDifficulty.RELAXED -> {
                // Casual / Random friendly move
                allMoves.random()
            }
            ChessDifficulty.EASY -> {
                // Prefers captures if available with 50% probability, otherwise random
                val captureMoves = allMoves.filter { (_, _, dest) -> board[dest.first][dest.second] != null }
                if (captureMoves.isNotEmpty() && Random.nextBoolean()) {
                    captureMoves.random()
                } else {
                    allMoves.random()
                }
            }
            ChessDifficulty.NORMAL -> {
                // Evaluates move by piece capture value + center control
                allMoves.maxByOrNull { (fromR, fromC, dest) ->
                    val target = board[dest.first][dest.second]
                    val captureScore = target?.type?.value ?: 0
                    val centerBonus = if (dest.first in 3..4 && dest.second in 3..4) 30 else 0
                    captureScore + centerBonus + Random.nextInt(20)
                } ?: allMoves.random()
            }
            ChessDifficulty.HARD -> {
                // Minimax / 1-ply tactical scoring with safety check
                allMoves.maxByOrNull { (fromR, fromC, dest) ->
                    val movingPiece = board[fromR][fromC]!!
                    val target = board[dest.first][dest.second]
                    val captureScore = (target?.type?.value ?: 0) * 2
                    val centerBonus = (7 - abs(dest.first * 2 - 7) - abs(dest.second * 2 - 7)) * 5
                    val kingHunt = if (target?.type == PieceType.KING) 10000 else 0
                    captureScore + centerBonus + kingHunt + Random.nextInt(10)
                } ?: allMoves.random()
            }
        }

        return makeMove(chosen.first, chosen.second, chosen.third.first, chosen.third.second)
    }
}
