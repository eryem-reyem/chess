class Field() {
    var status: String = "| _ |"


    /*private fun getStatus(): String{
        return piece.sign
    }*/
    fun char2Piece(char: Char){         // für Felder mit Pieces
        status = "|_${char}_|"
    }

    fun string2Piece(string: String){   // für Felder die geschlagen werden können
        status = string
    }

    fun emptyField(){                   //für leere Felder
        status = "| _ |"
    }
}