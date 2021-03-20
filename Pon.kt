class Pon(position: Pair<Int, Int>, sign: Char, color: Char = 'w'): Player(position, sign) {
    override val moves = if (color == 'b') {
        mutableListOf<Pair<Int, Int>>(
            Pair(1, 0)
        )
    } else {
        mutableListOf<Pair<Int, Int>>(
            Pair(-1, 0)
        )
    }
    override val maxSteps: Int = 1
    override val color = color

    override fun getPossibleMoves(board: Board): MutableList<Pair<Int, Int>> {
        var possibleMoves = mutableListOf<Pair<Int, Int>>()
        var newMaxSteps: Int = maxSteps

        if(color == 'w' && position.first == 6 || color == 'b' && position.first == 1) newMaxSteps += 1

        fun checkSide(){
            var newMoves = mutableListOf<Pair<Int, Int>>()
            if(color == 'w') newMoves = mutableListOf(Pair(-1, -1), Pair(-1, 1))
            else if( color == 'b') newMoves = mutableListOf(Pair(1, -1), Pair(1, 1))
            for(i in newMoves){
                val a: Int = position.first + i.first
                val b: Int = position.second + i.second
                if(!positionOnBoard(a,b)) break
                if (board.board[a][b].status != "| _ |") {
                    if(color == 'w' && checkColor(board, a, b) == 'w') break
                    else if(color == 'b' && checkColor(board, a, b) == 'b') break
                    possibleMoves.add(Pair(a, b))
                }
            }
        }

        for (i in moves) {
            for (j in 1..newMaxSteps) {
                val a: Int = position.first + i.first * j
                val b: Int = position.second + i.second * j
                if(!positionOnBoard(a,b)) break
                if (board.board[a][b].status == "| _ |") {
                    possibleMoves.add(Pair(a, b))
                } else break
            }
        }

        if(board.fen.enPassant != "-"){
            val p1 = board.xyToBoardposition(Pair(position.first + moves[0].first, position.second - 1))
            val p2 = board.xyToBoardposition(Pair(position.first + moves[0].first, position.second + 1))
            if(p1 == board.fen.enPassant) possibleMoves.add(board.boardposition2Xy(p1))
            else if (p2 == board.fen.enPassant) possibleMoves.add(board.boardposition2Xy(p2))
        }

        checkSide()

        possibleMoves = realPossible(board, possibleMoves)

        return possibleMoves
    }

}