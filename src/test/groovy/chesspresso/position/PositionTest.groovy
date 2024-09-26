package chesspresso.position

import chesspresso.Chess
import chesspresso.move.Move
import spock.lang.Specification

class PositionTest extends Specification {

    def 'initial position - play 1.Na3 Na6'() {
        when:
        Position p = Position.createInitialPosition()

        then: 'query the initial position'
        p.getPiece(Chess.A1) == Chess.ROOK
        p.getStone(Chess.A1) == Chess.WHITE_ROOK
        p.getPiece(Chess.B1) == Chess.KNIGHT
        p.getStone(Chess.B1) == Chess.WHITE_KNIGHT
        p.getPiece(Chess.C1) == Chess.BISHOP
        p.getStone(Chess.C1) == Chess.WHITE_BISHOP
        p.getFEN() == FEN.START_POSITION
        p.getHalfMoveClock() == 0
        p.getHashCode() == 6186144174769381545

        when:
        def moves = p.getAllMoves()

        then: 'query the move list'
        moves.length == 20
        Move.getFromSqi(moves[0]) == Chess.B1
        Move.getToSqi(moves[0]) == Chess.A3
        p.getPieceMove(Chess.KNIGHT, 1, 0, Chess.A3) == moves[0]

        Move.getFromSqi(moves[1]) == Chess.B1
        Move.getToSqi(moves[1]) == Chess.C3
        p.getPieceMove(Chess.KNIGHT, 1, 0, Chess.C3) == moves[1]

        Move.getFromSqi(moves[2]) == Chess.G1
        Move.getToSqi(moves[2]) == Chess.F3
        p.getPieceMove(Chess.KNIGHT, 6, 0, Chess.F3) == moves[2]

        Move.getFromSqi(moves[3]) == Chess.G1
        Move.getToSqi(moves[3]) == Chess.H3
        p.getPieceMove(Chess.KNIGHT, 6, 0, Chess.H3) == moves[3]

        Move.getFromSqi(moves[4]) == Chess.A2
        Move.getToSqi(moves[4]) == Chess.A3
        p.getPawnMove(Chess.NO_COL, Chess.A3, Chess.NO_PIECE) == moves[4]

        when: 'play 1.Na3'
        p.doMove(moves[0])

        then: 'query the position'
        p.getPiece(Chess.A3) == Chess.KNIGHT
        p.getStone(Chess.A3) == Chess.WHITE_KNIGHT
        p.getFEN() == 'rnbqkbnr/pppppppp/8/8/8/N7/PPPPPPPP/R1BQKBNR b KQkq - 1 1'
        p.getHalfMoveClock() == 1
        p.getHashCode() == 3415703449857218074

        when:
        moves = p.getAllMoves()

        then: 'query the move list'
        moves.length == 20
        Move.getFromSqi(moves[0]) == Chess.B8
        Move.getToSqi(moves[0]) == Chess.A6

        when: 'undo 1.Na3'
        p.undoMove()

        then: 'we are back in the initial position'
        p.getPiece(Chess.B1) == Chess.KNIGHT
        p.getStone(Chess.B1) == Chess.WHITE_KNIGHT
        p.getPiece(Chess.A3) == Chess.NO_PIECE
        p.getStone(Chess.A3) == Chess.NO_STONE
        p.getFEN() == FEN.START_POSITION
        p.getHalfMoveClock() == 0
        p.getHashCode() == 6186144174769381545
    }


    // todo
    //  use positions from
    //  https://www.chessprogramming.org/Perft_Results

    def 'a R endgame'() {
        when:
        Position p = new Position('4k3/4r3/8/8/8/8/4R3/4K3 w - - 0 1', true)

        then:
        p.getPiece(Chess.E1) == Chess.KING
        p.getStone(Chess.E1) == Chess.WHITE_KING
        p.getPiece(Chess.E8) == Chess.KING
        p.getStone(Chess.E8) == Chess.BLACK_KING
        p.getPiece(Chess.E2) == Chess.ROOK
        p.getStone(Chess.E2) == Chess.WHITE_ROOK
        p.getPiece(Chess.E7) == Chess.ROOK
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

    def 'test move generation'() {
        // set depth higher to achieve
        // 1) better coverage
        // 2) performance testing data
        expect:
        // https://www.chessprogramming.org/Perft_Results
        Perft(Position.createInitialPosition(), 1) == 20
        Perft(Position.createInitialPosition(), 2) == 400
        Perft(Position.createInitialPosition(), 3) == 8902
//        Perft(Position.createInitialPosition(), 4) == 197281
//        Perft(Position.createInitialPosition(), 5) == 4865609
//        Perft(Position.createInitialPosition(), 6) == 119060324
    }

    static long Perft(Position p, int depth) {
        //  https://www.chessprogramming.org/Perft
        if (depth == 0) {
            return 1;
        }
        long nodes = 0
        def moves = p.getAllMoves()
        for (int i = 0; i < moves.length; i++) {
            p.doMove(moves[i])
            nodes += Perft(p, depth - 1)
            p.undoMove()
        }
        return nodes
    }

}
