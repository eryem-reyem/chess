import java.io.File
import java.time.LocalDateTime

fun main(){
    //erstellt ein neues Spiel
    val game = Game()


    // data.txt erhält vor dem ersten Zug des Spiels einen DateTime Stamp
    File("data/data.txt").writeText(LocalDateTime.now().toString())


    // lädt ein neues Spiel
    //game.board.fen.fen2Board(game.board)


    //lädt ein angefangenes Spiel aus einem Fen
    game.board.fen.loadFen(game.board, "rnbqk1nr/1pppQppp/8/p7/4P3/8/PPPP1PPP/RNB1KBNR/ b KQkq - 1 4")
    // enPassant      ---rnbqkbnr/ppppppp1/8/4P3/6Pp/8/PPPP1P1P/RNBQKBNR/ b KQkq g3 0 3
    //  4k3/8/8/5q2/8/8/r2Q4/4K3/ b - - 0 3
    //  4k3/4N3/8/8/8/8/4n3/4K3/ b - - 0 3
    //


    // lädt die Figuren aus "board.piecePositions" auf "board.board"
    game.board.setPlayerOnBoard()


    // game loop
    while(true) {
        // startet die Eingabe
        val piece = game.movePlayer(game.board.fen.activeColor)

        // kreiert fen und speichert in in data.txt
        game.board.fen.saveFen(game.board, piece.color)

        game.checkGamestatus(piece)

        if(game.isCheck){
            println("${game.player} bietet Schach!")
            game.isCheck = false
        }

        if(game.isCheckmate){
            game.board.printBoard()
            println("Schachmatt! ${game.player} hat gewonnen.")
            break
        }

        if(!game.gameActiv){
            game.board.printBoard()
            println("Unendschieden!")
            break
        }

    }
}

/*
das Spiel findet auf einem Brett statt
auf jedem Brett gibt es zwei Gegner/Spieler
jeder Spieler hat am Anfang die selben Figuren
Es gibt schwarze es gibt weiße Figuren
am Anfang befinden sich alle Figuren auf bestimmten Feldern des Brettes
jede Figur kann sich bewegen
es gibt unteerschiedliche Figuren
unterschiedliche Figuren haben unterschiedliche Möglichkeiten sich zu bewegen
die spieler bewegen ihre Figuren abwechselnd --- Zugrecht
jede Figur kann sterben/ geschlagen werden

wenn der König angegriffen wird und der Angriff noch abgewehrt werden kann gild Schach
wenn der Angriff nicht mehr abgewehrt werden kann gild Schachmatt und das Spiel ist zu ende

wenn der König im Schach steht kann man nur noch Figuren ziehen die den Angriff abwehren

--> next Alpha-Beta-Pruning ai
 */