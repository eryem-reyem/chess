abstract class Player(var position: Pair<Int, Int>, val sign: Char) {
    var isDead: Boolean = false
    abstract val moves: List<Pair<Int, Int>>
    abstract val maxSteps: Int
    abstract val color: Char

    fun positionOnBoard(a: Int, b:Int): Boolean{
        if (a < 0 || b < 0 || a > 7 || b > 7) {
            return false
        }
        return true
    }    // check für "getPossibleMoves" ob nächste Position noch auf dem Board ist

    fun checkColor(board: Board, a: Int, b: Int): Char{
        if(board.board[a][b].status[2].isUpperCase()) return 'w'
        return 'b'
    }

    // Wenn ein König im Schach steht
    open fun realPossible(board: Board, possibleMoves: MutableList<Pair<Int, Int>>): MutableList<Pair<Int, Int>> {
        if(board.defendCheck.size != 0 && possibleMoves.size != 0){
            val notPossible = mutableListOf<Pair<Int, Int>>()
            for(position in possibleMoves){
                if(position !in board.defendCheck) notPossible.add(position)
            }
            for(position in notPossible) possibleMoves.remove(position)
        }
        return possibleMoves
    }

    open fun getPossibleMoves(board: Board): List<Pair<Int, Int>> {
        var possibleMoves = mutableListOf<Pair<Int, Int>>()
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

        return possibleMoves
    }
}