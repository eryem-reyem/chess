class King(position: Pair<Int, Int>, sign: Char, color: Char = 'w'): Player(position, sign) {
    override val moves = listOf<Pair<Int, Int>>(
        Pair(1, 1),
        Pair(1, -1),
        Pair(-1, 1),
        Pair(-1, -1),
        Pair(0, 1),
        Pair(0, -1),
        Pair(-1, 0),
        Pair(1, 0)
    )
    override val maxSteps: Int = 1
    override val color = color
    var castelingPositions = mutableListOf<Pair<Int, Int>>()

    override fun realPossible(board: Board, possibleMoves: MutableList<Pair<Int, Int>>): MutableList<Pair<Int, Int>> {
        if(board.defendCheck.size != 0 && possibleMoves.size != 0){
            if(board.defendCheck.size >1) {
                var notPossible = mutableListOf<Pair<Int, Int>>()
                for (position in possibleMoves) {
                    if (position in board.defendCheck) notPossible.add(position)
                }
                for (position in notPossible) possibleMoves.remove(position)
            }else{
                var notPossible = mutableListOf<Pair<Int, Int>>()
                for (position in possibleMoves) {
                    if (position !in board.defendCheck) notPossible.add(position)
                }
                for (position in notPossible) possibleMoves.remove(position)
            }
        }
        return possibleMoves
    }

    fun getCastelingPositions(board: Board){
        if(color == 'w' && 'K' in board.fen.castling && board.board[7][5].status == "| _ |" &&
                board.board[7][6].status == "| _ |"){
            castelingPositions.add(Pair(7, 6))
        }
        else if(color == 'b' && 'k' in board.fen.castling && board.board[0][5].status == "| _ |" &&
            board.board[0][6].status == "| _ |"){
            castelingPositions.add(Pair(0, 6))
        }

        if(color == 'w' && 'Q' in board.fen.castling && board.board[7][1].status == "| _ |" &&
            board.board[7][2].status == "| _ |" && board.board[7][3].status == "| _ |"){
            castelingPositions.add(Pair(7, 2))
        }
        else if(color == 'b' && 'q' in board.fen.castling && board.board[0][1].status == "| _ |" &&
            board.board[0][2].status == "| _ |" && board.board[0][3].status == "| _ |"){
            castelingPositions.add(Pair(0, 2))
        }
    }

    override fun getPossibleMoves(board: Board): List<Pair<Int, Int>> {
        var possibleMoves = mutableListOf<Pair<Int, Int>>()
        castelingPositions.clear()

        for (i in moves) {
            for (j in 1..maxSteps) {
                val a: Int = position.first + i.first * j
                val b: Int = position.second + i.second * j
                if(!positionOnBoard(a,b)) break
                if (board.board[a][b].status == "| _ |") {
                    possibleMoves.add(Pair(a, b))
                } else {
                    if(color == 'w' && checkColor(board, a, b) == 'w') break
                    else if(color == 'b' && checkColor(board, a, b) == 'b') break
                    else{
                        possibleMoves.add(Pair(a, b))
                        break
                    }
                }
            }
        }

        possibleMoves = realPossible(board, possibleMoves)

        if(board.defendCheck.size == 0) getCastelingPositions(board)

        if(castelingPositions.size != 0) {
            for(position in castelingPositions) {
                if(position !in possibleMoves){
                    possibleMoves.add(position)
                }
            }
        }
        return possibleMoves
    }

    fun castle(board: Board, position: Pair<Int, Int>): Boolean {
        if(position == Pair(0, 2)){
            println("Black castled Queenside")
            val king = board.piecePositions[Pair(0, 4)]
            val rook = board.piecePositions[Pair(0, 0)]

            if (king != null && rook != null) {
                // für König
                board.piecePositions.put(Pair(0, 2), king)
                board.piecePositions.minusAssign(Pair(0, 4))
                board.board[0][2].char2Piece(king.sign)
                board.board[0][4].emptyField()
                board.board[0][6].emptyField()
                king.position = Pair(0, 2)

                // für rook
                board.piecePositions.put(Pair(0, 3), rook)
                board.piecePositions.minusAssign(Pair(0, 0))
                board.board[0][3].char2Piece(rook.sign)
                board.board[0][0].emptyField()
                rook.position = Pair(0, 3)


                return true
            }
        } // black Quennside

        else if(position == Pair(0, 6)){
            println("Black castled Kingside")
            val king = board.piecePositions[Pair(0, 4)]
            val rook = board.piecePositions[Pair(0, 7)]

            if (king != null && rook != null) {
                // für König
                board.piecePositions.put(Pair(0, 6), king)
                board.piecePositions.minusAssign(Pair(0, 4))
                board.board[0][6].char2Piece(king.sign)
                board.board[0][4].emptyField()
                board.board[0][2].emptyField()
                king.position = Pair(0, 6)

                // für rook
                board.piecePositions.put(Pair(0, 5), rook)
                board.piecePositions.minusAssign(Pair(0, 7))
                board.board[0][5].char2Piece(rook.sign)
                board.board[0][7].emptyField()
                rook.position = Pair(0, 5)

                return true
            }
        } // black Kingside

        else if(position == Pair(7, 2)){
            println("White castled Queenside")
            val king = board.piecePositions[Pair(7, 4)]
            val rook = board.piecePositions[Pair(7, 0)]

            if (king != null && rook != null) {
                // für König
                board.piecePositions.put(Pair(7, 2), king)
                board.piecePositions.minusAssign(Pair(7, 4))
                board.board[7][2].char2Piece(king.sign)
                board.board[7][4].emptyField()
                board.board[7][6].emptyField()
                king.position = Pair(7, 2)

                // für rook
                board.piecePositions.put(Pair(7, 3), rook)
                board.piecePositions.minusAssign(Pair(7, 0))
                board.board[7][3].char2Piece(rook.sign)
                board.board[7][0].emptyField()
                rook.position = Pair(7, 3)

                return true
            }
        } // withe Quennside

        else if(position == Pair(7, 6)){
            println("White castled Kingside")
            val king = board.piecePositions[Pair(7, 4)]
            val rook = board.piecePositions[Pair(7, 7)]

            if (king != null && rook != null) {
                // für König
                board.piecePositions.put(Pair(7, 6), king)
                board.piecePositions.minusAssign(Pair(7, 4))
                board.board[7][6].char2Piece(king.sign)
                board.board[7][4].emptyField()
                board.board[7][2].emptyField()
                king.position = Pair(7, 6)

                // für rook
                board.piecePositions.put(Pair(7, 5), rook)
                board.piecePositions.minusAssign(Pair(7, 7))
                board.board[7][5].char2Piece(rook.sign)
                board.board[7][7].emptyField()
                rook.position = Pair(7, 5)

                return true
            }
        } // black Kingside

        return false
    }
}