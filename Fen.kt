import java.io.File

class Fen {
    var newFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    val fen = newFen.split(' ')
    var fenList = mutableListOf<Map<Int, String>>()

    var piecePosition = fen[0]
    var activeColor = fen[1]
    var castling = fen[2]
    var enPassant = fen[3]
    var halfMoveClock = fen[4].toInt()
    var fullMoveNumber = fen [5].toInt()

    fun fen2Board(board: Board){
        var y: Int = 0
        var x: Int = 0

        for(element in piecePosition){
            if(element.isDigit()) {
                x += Character.getNumericValue(element)
                continue
            }
            else if(element.isLetter()){
                var piece: Player = King(Pair(y,x),element, 'x')
                var color: Char

                if(element.isLowerCase()) color = 'b' else color = 'w'

                if(element.toLowerCase() == 'k') piece = King(Pair(y,x), element, color)
                else if(element.toLowerCase() == 'q') piece = Queen(Pair(y,x), element, color)
                else if(element.toLowerCase() == 'b') piece = Bishop(Pair(y,x), element, color)
                else if(element.toLowerCase() == 'n') piece = Knight(Pair(y,x), element, color)
                else if(element.toLowerCase() == 'r') piece = Rook(Pair(y,x), element, color)
                else if(element.toLowerCase() == 'p') piece = Pon(Pair(y,x), element, color)


                board.piecePositions.put(Pair(y,x),piece)
                x += 1
            }
            else if(element == '/'){
                y +=1
                x = 0
            }
        }
    }       // fügt aus dem String "piecePositions" Pieces Klassen dem Dict/Map "board/piecePositions" hinzu

    fun board2Fen(board: Board, color: Char): String {
        var string: String = ""
        var count = 0

        for(y in board.board){
            for(x in y){
                if(x.status[2] == '_') count += 1
                else{
                    if(count != 0) string += count.toString()
                    string += x.status[2]
                    count = 0
                }
            }
            if(count != 0) string += count
            string += '/'
            count = 0
        }

        string = string.subSequence(0, string.length).toString()

        if(color == 'w'){
            string += " b"
            activeColor = "b"
        }
        else{
            string += " w"
            activeColor = "w"
        }

        string += " $castling"

        string += " $enPassant"

        string += " $halfMoveClock"

        string += " $fullMoveNumber"
        return string
    }

    fun loadFen(board: Board, fen: String){
        piecePosition = fen.split(' ')[0]
        activeColor = fen.split(' ')[1]
        castling = fen.split(' ')[2]
        enPassant = fen.split(' ')[3]
        halfMoveClock = fen.split(' ')[4].toInt()
        fullMoveNumber = fen.split(' ')[5].toInt()
        fen2Board(board)
    }

    fun saveFen(fen: String){
        File("data/data.txt").appendText("\n $fen")
    }

}