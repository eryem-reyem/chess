class Game: Move(){
    val board: Board = Board()
    var color: Char = 'w'
    var player = ""
    var gameActiv = true
    var isCheck = false
    var isCheckmate = false
    lateinit var piece: Player
    lateinit var possibleMoves: List<Pair<Int, Int>>



    fun checkGamestatus(piece: Player){

        fun checkKingIsCheck(piece: Player) {
            var king: Player? = null
            var tempPossibleMoves = mapOf<Player, List<Pair<Int, Int>>>()

            for(a_piece in board.piecePositions){
                if(a_piece.value is King && a_piece.value.color != piece.color) king = a_piece.value
            }

            if(piece.color == 'w') tempPossibleMoves = tempPossibleMoves(board).first
            else tempPossibleMoves = tempPossibleMoves(board).second

            if(king != null){
                for(a_piece in tempPossibleMoves){
                    for(a_move in a_piece.value){
                        if(a_move == king.position){
                            isCheck = true
                            break
                        }
                    }
                    if(isCheck) break
                }
            }
        }

        fun checkKingIsCheckmate(piece: Player){
            var king: Player? = null
            var tempPossibleMoves = mapOf<Player, List<Pair<Int, Int>>>()

            for(a_piece in board.piecePositions){
                if(a_piece.value is King && a_piece.value.color != piece.color) king = a_piece.value
            }

            if(piece.color == 'w') tempPossibleMoves = tempPossibleMoves(board).second
            else tempPossibleMoves = tempPossibleMoves(board).first


            if(king != null){
                for(a_piece in tempPossibleMoves){
                    for(a_move in a_piece.value){
                        isCheckmate = !checkMyKing(board, a_move, a_piece.key, king!!)
                        if(!isCheckmate) break
                    }
                    if(!isCheckmate) break
                }
            }
        }


        checkKingIsCheck(piece)

        if(isCheck) checkKingIsCheckmate(piece)

        if(isCheckmate){
            isCheck = false
            gameActiv = false
        }


    }

    fun movePlayer(activeColor: String): Player {
        color = activeColor.single()

        if(activeColor == "w") player = "Player 1" else player = "Player 2"

        while (true){
            // Nutzereingbe welche figur ziehen soll
            fun pieceFrom(): Boolean {
                val input = board.inputPositionToXy(player, "welche Figur willst du bewegen?")

                if(input in board.piecePositions && board.piecePositions[input]?.color == color){
                    piece = board.piecePositions[input]!!
                }
                else {
                    println("Not a valid Move!")
                    return false
                }
                return true
            }


            // shell Ausgabe des Boards
            board.printBoard()


            // aus der Nutzereingabe wird eine Figur der Variablen "piece zugewiesen"
            if(!pieceFrom()) continue


            // Liste mit Positionen auf die das piece ziehen kann
            possibleMoves = allPossibleMoves(board, piece)


            // unterbricht den Loop, wenn keine Züge möglich sind
            if(possibleMoves.size == 0) {
                println("Keine gültigen Züge mit der Figur ${piece.sign}!")
                continue
            }


            // Ausgabe der Liste möglicher Züge
            print("${piece.sign} hat folgende Möglichkeiten zu Ziehen: ")
            for(move in possibleMoves) print("${board.xyToBoardposition(move)} /")
            println()


            // zeigt mögliche Züge auf dem Board
            board.showPossibleMoves(possibleMoves)


            // hier findet die Eingabe statt auf welches Feld gezogen werden soll
            val inputToXy = board.inputPositionToXy(player,  "wohin willst du ziehen?")


            //  setzt werte im Bord zurück die in "board.showPossibleMoves" gändert wurden
            //board.returnShowPossibleMoves(possibleMoves)
            board.setPlayerOnBoard()


            // führt den gewählten Zug aus, wenn er gültig ist
            if(inputToXy in board.coordinates){
                if (setMove(inputToXy, possibleMoves, piece, board)){
                    println("${piece.sign} zieht auf ${board.xyToBoardposition(piece.position)}")
                    return piece
                }else println("Kein gültiger Zug!")
            }


        }
    }
}