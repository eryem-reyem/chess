class Queen(position: Pair<Int, Int>, sign: Char, color: Char = 'w'): Player(position, sign) {
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
    override val maxSteps: Int = 8
    override val color = color
}