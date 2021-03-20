class Board {
    val fen = Fen()
    val board = Array(8){i -> Array(8){i -> Field()}}  // Brett mit 64 Feldern
    var piecePositions = hashMapOf<Pair<Int, Int>, Player>()   // Dictionary/Map mit activen Figuren
    var defendCheck = mutableListOf<Pair<Int, Int>>()

    fun initPlayer(){
        for(element in piecePositions){
            board[element.key.first][element.key.second].char2Piece(element.value.sign)
        }
    }                                      // Initialisiert die Zeichen der Pieces auf dem Brett

    fun printBoard(){
        var count: Int = 8
        println("   ----------------------------------------")
        println("   | A || B || C || D || E || F || G || H |")
        println("   ----------------------------------------")
        for(i in board){
            print("${count}  ")
            for(j in i){
                print((j.status))
            }
            print("  $count")
            count -= 1
            println()
        }
        println("   ----------------------------------------")
        println("   | A || B || C || D || E || F || G || H |")
        println("   ----------------------------------------")

    }                                       // Printed in der Shell


    fun inputPositionToXy(player: String, massage: String): Pair<Int, Int> {
        if(defendCheck.size != 0) println("$player dein König steht im Schach!")
        print("$player $massage: ")

        val a_input = readLine()
        var input = String()

        if(a_input != null && a_input?.length == 2){
            input = a_input!!
        }
        else return Pair(11, 11)

        return boardposition2Xy(input)

    } // Shell eingabe für Position im Format a2... returned xy Koordinate

    fun boardposition2Xy(input: String): Pair<Int, Int> {
        val letters = listOf<Char>('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
        val numbers = listOf<Int>(8, 7, 6 , 5, 4, 3, 2, 1)

        val a = input[1]
        var a2: Int
        val b = input[0]
        var b2: Int

        if(a.isDigit() && b in letters && Character.getNumericValue(a) in numbers) {
            a2 = numbers.indexOf(Character.getNumericValue(a))
            b2 = letters.indexOf(input[0].toLowerCase())
            return Pair(a2, b2)
        }
        else{
            return Pair( 10, 10)
        }
    }

    fun xyToBoardposition(position: Pair<Int, Int>): String {
        val letters = listOf<Char>('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
        val numbers = listOf<Int>(8, 7, 6 , 5, 4, 3, 2, 1)
        if(position.first in 0..7 && position.second in 0..7) return "${letters[position.second]}${numbers[position.first]}"
        return "Keine gültige Position"
    }

    fun allPossibleMoves(board: Board): Pair<Map<Player, List<Pair<Int, Int>>>, Map<Player, List<Pair<Int, Int>>>> {
        var allPossibleWhiteMoves = hashMapOf<Player, List<Pair<Int, Int>>>()
        var allPossibleBlackMoves = hashMapOf<Player, List<Pair<Int, Int>>>()
        for(element in piecePositions){
            val possibleMoves = element.value.getPossibleMoves(board)
            if(element.value.color == 'w') allPossibleWhiteMoves[element.value] = possibleMoves
            else if(element.value.color == 'b') allPossibleBlackMoves[element.value] = possibleMoves
        }
        val allPossibleMoves = Pair<Map<Player, List<Pair<Int, Int>>>, Map<Player, List<Pair<Int, Int>>>>(allPossibleWhiteMoves, allPossibleBlackMoves)
        return allPossibleMoves
    }

}