package chesspresso.position

import chesspresso.Chess
import chesspresso.move.Move
import spock.lang.Specification

class PositionTest extends Specification {

    def 'initial position'() {
        when:
        Position p = Position.createInitialPosition()

        then:
        p.getStone(Chess.A1) == Chess.WHITE_ROOK
        p.getStone(Chess.B1) == Chess.WHITE_KNIGHT
        p.getStone(Chess.C1) == Chess.WHITE_BISHOP

        when:
        def moves = p.getAllMoves()

        then:
        moves.length == 20
        Move.getFromSqi(moves[0]) == Chess.B1
        Move.getToSqi(moves[0]) == Chess.A3

        when:
        p.doMove(moves[0])

        then:
        p.getStone(Chess.A3) == Chess.WHITE_KNIGHT

        when:
        moves = p.getAllMoves()

        then:
        moves.length == 20
        Move.getFromSqi(moves[0]) == Chess.B8
        Move.getToSqi(moves[0]) == Chess.A6
    }

    def 'a R endgame'() {
        when:
        Position p = new Position('4k3/4r3/8/8/8/8/4R3/4K3 w - - 0 1', true)

        then:
        p.getStone(Chess.E1) == Chess.WHITE_KING
        p.getStone(Chess.E8) == Chess.BLACK_KING
        p.getStone(Chess.E2) == Chess.WHITE_ROOK
        p.getStone(Chess.E7) == Chess.BLACK_ROOK

        when:
        def moves = p.getAllMoves()

        then:
        moves.length == 9
        Move.getFromSqi(moves[0]) == Chess.E2
        Move.getToSqi(moves[0]) == Chess.E3
        Move.getToSqi(moves[1]) == Chess.E4
        Move.getToSqi(moves[2]) == Chess.E5
        Move.getToSqi(moves[3]) == Chess.E6
        Move.getToSqi(moves[4]) == Chess.E7
    }

}
