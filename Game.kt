class Game {
    val board: Board = Board()
    var capturedWhitePieces = mutableListOf<Player>()
    var capturedBlackPieces = mutableListOf<Player>()
    var color: Char = 'w'
    var player = ""




    fun setMove(a: Int?, b: Int?, possibleMoves: List<Pair<Int, Int>>, piece: Player): Boolean{
        if(Pair<Int?, Int?>(a, b) in possibleMoves){
            // Schlägt eine gegnerische Figur, wenn diese auf dem Zugfeld steht
            if(Pair<Int, Int>(a!!, b!!) in board.piecePositions) capturePiece(a , b)

            // Schlägt einen Pon En Passant
            if(piece is Pon && board.xyToBoardposition(Pair(a, b)) == board.fen.enPassant){
                if(piece.color == 'b'){
                    capturePiece(a-1, b)
                    board.board[a-1][b].emptyField()
                }
                else if(piece.color == 'w'){
                    capturePiece(a+1, b)
                    board.board[a+1][b].emptyField()
                }
            }

            // Setzt En Passant Variable in der Klasse Fen
            if(piece is Pon && possibleMoves.size == 2 && possibleMoves[1] == Pair<Int, Int>(a!!, b!!)){
                val rightSide = Pair(a, b+1)
                val leftSide = Pair(a, b-1)
                val pieceRight = board.piecePositions[rightSide]
                val pieceLeft = board.piecePositions[leftSide]

                if(pieceRight is Pon && pieceRight.color != piece.color ||
                    pieceLeft is Pon && pieceLeft.color != piece.color){
                    board.fen.enPassant = board.xyToBoardposition(possibleMoves[0])

                }else board.fen.enPassant = "-"

            } else board.fen.enPassant = "-"

            // Casteling
            if(board.fen.castling != "-"){
                if(piece is King) {
                    var castle: Boolean = false
                    if (piece.castelingPositions.size != 0 && Pair<Int, Int>(a!!, b!!) in piece.castelingPositions) {
                        castle = piece.castle(board, Pair<Int, Int>(a!!, b!!))
                    }

                    if (piece.color == 'w') {
                        if ("Q" in board.fen.castling) board.fen.castling = board.fen.castling.replace("Q", "")
                        if ("K" in board.fen.castling) board.fen.castling = board.fen.castling.replace("K", "")
                    }           // removes castling on fen for white
                    else if (piece.color == 'b') {
                        if ("q" in board.fen.castling) board.fen.castling = board.fen.castling.replace("q", "")
                        if ("k" in board.fen.castling) board.fen.castling = board.fen.castling.replace("k", "")
                    }       // removes castling on fen for black

                    if (castle) return castle
                }

                if(piece is Rook){
                    if("k" in board.fen.castling && piece.position == Pair(0, 7)) board.fen.castling = board.fen.castling.replace("k", "")
                    else if("q" in board.fen.castling && piece.position == Pair(0, 0)) board.fen.castling =  board.fen.castling.replace("q", "")
                    else if("K" in board.fen.castling && piece.position == Pair(7, 7)) board.fen.castling = board.fen.castling.replace("K", "")
                    else if("Q" in board.fen.castling && piece.position == Pair(7, 0)) board.fen.castling = board.fen.castling.replace("Q", "")
                    println(board.fen.castling)
                }
            }

            board.board[a][b].char2Piece(piece.sign)
            board.board[piece.position.first][piece.position.second].emptyField()
            board.piecePositions.minusAssign(Pair<Int, Int>(piece.position.first, piece.position.second))
            board.piecePositions.put(Pair(a, b), piece)
            piece.position = Pair(a, b)
            println("${piece.sign} zieht auf ${board.xyToBoardposition(piece.position)}")
            println("Gültiger Zug! Nächster Spieler.")

            if(piece.color == 'b') board.fen.fullMoveNumber += 1

            board.fen.halfMoveClock += 1
            if(piece is Pon) board.fen.halfMoveClock = 0

            return true
        }
        else{
            println("Kein gültiger Zug!")
            return false
        }
    }

    fun capturePiece(a: Int, b: Int){
        val toCapture = board.piecePositions[Pair(a,b)]

        if (toCapture != null) {                            // Setzt Piece auf Tod und fügt es einer Liste für tote Pieces zu
            toCapture.isDead = true
            if(toCapture.color == 'w') capturedWhitePieces.add(toCapture)
            else capturedBlackPieces.add(toCapture)

            board.piecePositions.remove(Pair(a,b))             // löscht das geschlagene Piece aus dem Dictionary/Map mit activen Figuren

            board.fen.halfMoveClock = 0

            println("Capture Piece: ${toCapture.sign}")
            println("Geschlagene weiße Figuren ${capturedWhitePieces}")
            println("Geschlagene schwarze Figuren $capturedBlackPieces")
        }
    }

    // gibt die Figur zurück die dem König schach gibt und den König der im Schach steht
    fun kingIsCheck(piece: Player): Pair<Player, Player>? {
        val allPossibleMoves = board.allPossibleMoves(board)
        var blackKing: Player? = null
        var whiteKing: Player? = null

        // Pair<Boolean, Pair<Pair<Int, Int>, Pair<Int, Int>>
        for(element in board.piecePositions.values){
            if(element is King){
                if(element.color == 'w') whiteKing = element
                else  blackKing = element
            }
        }

        if(blackKing != null && whiteKing != null) {
            if (piece.color == 'w') {
                for (list in allPossibleMoves.first) {
                    for (element in list.value) {
                        if (element == blackKing.position) {
                            return Pair(blackKing, piece)
                        }
                    }
                }
            } else {
                for (list in allPossibleMoves.second) {
                    for (element in list.value) {
                        if (element == whiteKing.position) {
                            return Pair(whiteKing, piece)
                        }
                    }
                }
            }
        }
        return null
    }

    fun positionsToDefendCheck(kingIsCheck: Pair<Player, Player>): MutableList<Pair<Int, Int>> {
        val kingPosition = kingIsCheck.first.position
        val attackerPosition = kingIsCheck.second.position

        var defendingPositons = mutableListOf<Pair<Int, Int>>()

        var smalestX: Int
        var biggestX: Int

        var smalestY: Int
        var biggestY: Int

        if(kingPosition.first < attackerPosition.first){
            smalestY = kingPosition.first
            biggestY = attackerPosition.first
        } else {
            smalestY = attackerPosition.first
            biggestY = kingPosition.first
        }

        if(kingPosition.second < attackerPosition.second){
            smalestX = kingPosition.second
            biggestX = attackerPosition.second
        }else {
            smalestX = attackerPosition.second
            biggestX = kingPosition.second
        }

        for(y in smalestY..biggestY){
            for(x in smalestX..biggestX){
                defendingPositons.add(Pair(y, x))
            }
        }

        defendingPositons.minusAssign(kingPosition)

        for(piece in board.piecePositions.values){
            if(piece.color != kingIsCheck.second.color){
                val possibleMoves = piece.getPossibleMoves(board)
                for(position in possibleMoves){
                    if(position in defendingPositons) defendingPositons.minusAssign(position)
                }
            }
        }

        return defendingPositons
    }

    fun movePlayer(activeColor: String): Player {
        if(board.fen.castling == "") board.fen.castling = "-"
        while (true){
            board.printBoard()
            var piece: Player

            // Verändert "Field" im "board" um mögliche Züge einer Figur in der Shell darzustellen
            fun showPossibleMoves(possibleMoves: List<Pair<Int, Int>>){

                for(i in possibleMoves){
                    if(board.board[i.first][i.second].status == "| _ |"){
                        board.board[i.first][i.second].string2Piece("|(_)|")
                    }
                    else{
                        val status = "|(${board.board[i.first][i.second].status[2]})|"
                        board.board[i.first][i.second].string2Piece(status)
                    }
                }
                return board.printBoard()
            }

            // setzt den status des boards zurück, der mit show moves veränderd wurde
            fun returnShowPossibleMoves(possibleMoves: List<Pair<Int, Int>>){

                for(i in possibleMoves){
                    if(board.board[i.first][i.second].status.equals("|(_)|") ){
                        board.board[i.first][i.second].emptyField()
                    }
                    else{
                        board.board[i.first][i.second].char2Piece(board.board[i.first][i.second].status[2])
                    }
                }
            }

            // für Printausgabe
            fun setPlayer(){
                if(activeColor == "w"){
                    player = "Player 1"
                    color = 'w'
                }
                else{
                    player = "Player 2"
                    color = 'b'
                }
            }


            setPlayer()

            val input = board.inputPositionToXy(player, "welche Figur willst du bewegen?")

            if(input in board.piecePositions && board.piecePositions[input]?.color == color){
                piece = board.piecePositions[input]!!
            }
            else {
                println("Not a valid Move!")
                continue
            }

            val possibleMoves: List<Pair<Int, Int>>
            if(piece.color == 'w')  possibleMoves = board.allPossibleMoves(board).first[piece]!!
            else possibleMoves = board.allPossibleMoves(board).second[piece]!!

            print("${piece.sign} hat folgende Möglichkeiten zu Ziehen: ")           // Ausgabe möglicher Züge
            for(move in possibleMoves) print("${board.xyToBoardposition(move)} /")
            println()

            if(possibleMoves.size == 0) {
                println("Keine gültigen Züge mit der Figur ${piece.sign}!")
                if(board.defendCheck.size != 0) println("${player} dein König steht im Schach!")
                continue
            }

            showPossibleMoves(possibleMoves) // Printausgabe ändert werte im Bord

            val inputTo = board.inputPositionToXy(player,  "wohin willst du ziehen?")

            returnShowPossibleMoves(possibleMoves)

            if (setMove(inputTo.first, inputTo.second, possibleMoves, piece)) return piece


        }
    }
}