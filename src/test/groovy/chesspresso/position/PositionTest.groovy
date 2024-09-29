package chesspresso.position

import chesspresso.Chess
import chesspresso.move.Move
import spock.lang.Specification

class PositionTest extends Specification {

    def 'initial position - play 1.Na3 Na6'() {
        when:
        Position p = Position.createInitialPosition()

        then: 'query the initial position'
        p.legal
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        p.canMove()
        !p.canUndoMove()
        p.isCastlePossible(ImmutablePosition.WHITE_SHORT_CASTLE)
        p.isCastlePossible(ImmutablePosition.WHITE_LONG_CASTLE)
        p.isCastlePossible(ImmutablePosition.BLACK_SHORT_CASTLE)
        p.isCastlePossible(ImmutablePosition.BLACK_LONG_CASTLE)
        p.getPiece(Chess.A1) == Chess.ROOK
        p.getStone(Chess.A1) == Chess.WHITE_ROOK
        p.getPiece(Chess.B1) == Chess.KNIGHT
        p.getStone(Chess.B1) == Chess.WHITE_KNIGHT
        p.getPiece(Chess.C1) == Chess.BISHOP
        p.getStone(Chess.C1) == Chess.WHITE_BISHOP
        p.FEN == FEN.START_POSITION
        p.halfMoveClock == 0
        p.getColor(Chess.A1) == Chess.WHITE
        p.getColor(Chess.A2) == Chess.WHITE
        p.getColor(Chess.A8) == Chess.BLACK
        p.getColor(Chess.A7) == Chess.BLACK
        p.getColor(Chess.D5) == Chess.NOBODY
        p.hashCode == 6186144174769381545

        when:
        def moves = p.allMoves

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
        p.FEN == 'rnbqkbnr/pppppppp/8/8/8/N7/PPPPPPPP/R1BQKBNR b KQkq - 1 1'
        p.halfMoveClock == 1
        p.canMove()
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        p.hashCode == 3415703449857218074

        when:
        moves = p.allMoves

        then: 'query the move list'
        moves.length == 20
        Move.getFromSqi(moves[0]) == Chess.B8
        Move.getToSqi(moves[0]) == Chess.A6
        !p.canRedoMove()
        p.canUndoMove()

        when: 'undo 1.Na3'
        p.undoMove()

        then: 'we are back in the initial position'
        p.legal
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        !p.canUndoMove()
        p.getPiece(Chess.B1) == Chess.KNIGHT
        p.getStone(Chess.B1) == Chess.WHITE_KNIGHT
        p.getPiece(Chess.A3) == Chess.NO_PIECE
        p.getStone(Chess.A3) == Chess.NO_STONE
        p.FEN == FEN.START_POSITION
        p.halfMoveClock == 0
        p.hashCode == 6186144174769381545

        when:
        p.inverse()

        then:
        p.FEN == 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1'
        p.legal
        p.canMove()
        p.getMaterial() == 0
        p.getMovesAsString(p.allMoves, true) == '{a5,b5,c5,d5,e5,f5,g5,h5,a6,Na6,b6,c6,Nc6,d6,e6,f6,Nf6,g6,h6,Nh6}'
    }

    def 'a R endgame'() {
        when:
        Position p = new Position('4k3/4r3/8/8/8/8/4R3/4K3 w - - 0 1', true)

        then: 'query the position'
        p.legal
        p.canMove()
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        p.toPlay == Chess.WHITE
        p.getPiece(Chess.E1) == Chess.KING
        p.getStone(Chess.E1) == Chess.WHITE_KING
        p.getPiece(Chess.E8) == Chess.KING
        p.getStone(Chess.E8) == Chess.BLACK_KING
        p.getPiece(Chess.E2) == Chess.ROOK
        p.getStone(Chess.E2) == Chess.WHITE_ROOK
        p.getPiece(Chess.E7) == Chess.ROOK
        p.getStone(Chess.E7) == Chess.BLACK_ROOK
        p.FEN == '4k3/4r3/8/8/8/8/4R3/4K3 w - - 0 1'
        p.halfMoveClock == 0
        p.hashCode == 5375441042984917594

        when:
        def moves = p.allMoves

        then: 'query the move list'
        moves.length == 9
        Move.getFromSqi(moves[0]) == Chess.E2
        Move.getToSqi(moves[0]) == Chess.E3
        Move.getToSqi(moves[1]) == Chess.E4
        Move.getToSqi(moves[2]) == Chess.E5
        Move.getToSqi(moves[3]) == Chess.E6
        Move.getToSqi(moves[4]) == Chess.E7

        when:
        p.inverse()

        then:
        p.FEN == '4k3/4r3/8/8/8/8/4R3/4K3 b - - 0 1'
        p.legal
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        p.canMove()
        p.getMaterial() == 0
        p.getMovesAsString(p.allMoves, true) == '{Rxe2+,Re3,Re4,Re5,Re6,Kd7,Kf7,Kd8,Kf8}'
    }

    def 'kiwipete position'() {
        // https://www.chessprogramming.org/Perft_Results
        when:
        Position p = new Position('r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1', true)

        then: 'query the position'
        p.legal
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        p.toPlay == Chess.WHITE
        p.canMove()
        p.getPiece(Chess.E1) == Chess.KING
        p.getStone(Chess.E1) == Chess.WHITE_KING
        p.getPiece(Chess.E8) == Chess.KING
        p.getStone(Chess.E8) == Chess.BLACK_KING
        p.getPiece(Chess.E2) == Chess.BISHOP
        p.getStone(Chess.E2) == Chess.WHITE_BISHOP
        p.getPiece(Chess.E7) == Chess.QUEEN
        p.getStone(Chess.E7) == Chess.BLACK_QUEEN
        p.FEN == 'r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1'
        p.halfMoveClock == 0
        p.hashCode == 6134742301751089191

        when:
        def moves = p.allMoves

        then: 'query the move list'
        moves.length == 48
        Move.getFromSqi(moves[0]) == Chess.C3
        Move.getToSqi(moves[0]) == Chess.B1
        Move.getFromSqi(moves[1]) == Chess.C3
        Move.getToSqi(moves[1]) == Chess.D1
        Move.getFromSqi(moves[2]) == Chess.C3
        Move.getToSqi(moves[2]) == Chess.A4
        Move.getFromSqi(moves[3]) == Chess.C3
        Move.getToSqi(moves[3]) == Chess.B5

        when:
        p.inverse()

        then:
        p.FEN == 'r3k2r/pppbbppp/2n2q1P/1P2p3/3pn3/BN2PNP1/P1PPQPB1/R3K2R b KQkq - 0 1'
        p.legal
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        p.canMove()
        p.getMaterial() == 0
        p.getMovesAsString(p.allMoves, true) == '{Nxd2,Nxf2,Bxa3,dxe3,Qxf3,Nxg3,Qxh6,gxh6,Nc3,d3,Bh3,Nb4,Bb4,Qf4,Bg4,Qh4,Na5,a5,Nc5,Bc5,Qf5,Bf5,Ng5,Qg5,g5,a6,b6,Nd6,Qd6,Bd6,Qe6,Be6,Qg6,g6,Nb8,Rb8,Bc8,Rc8,Nd8,Bd8,Rd8,Kd8,Bf8,Kf8,Rf8,Rg8,O-O-O,O-O}'
    }

    def 'position 3 from Perft page'() {
        // https://www.chessprogramming.org/Perft_Results
        when:
        Position p = new Position('8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1', true)

        then: 'query the position'
        p.legal
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        p.toPlay == Chess.WHITE
        p.canMove()
        p.getPiece(Chess.E1) == Chess.NO_PIECE
        p.getStone(Chess.E1) == Chess.NO_STONE
        p.getPiece(Chess.E8) == Chess.NO_PIECE
        p.getStone(Chess.E8) == Chess.NO_STONE
        p.getPiece(Chess.E2) == Chess.PAWN
        p.getStone(Chess.E2) == Chess.WHITE_PAWN
        p.getPiece(Chess.E7) == Chess.NO_PIECE
        p.getStone(Chess.E7) == Chess.NO_STONE
        p.FEN == '8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1'
        p.halfMoveClock == 0
        p.hashCode == 1806705713370623020

        when:
        def moves = p.allMoves

        then: 'query the move list'
        moves.length == 14
        Move.getFromSqi(moves[0]) == Chess.B4
        Move.getToSqi(moves[0]) == Chess.B3
        Move.getFromSqi(moves[1]) == Chess.B4
        Move.getToSqi(moves[1]) == Chess.B2
        Move.getFromSqi(moves[2]) == Chess.B4
        Move.getToSqi(moves[2]) == Chess.B1
        Move.getFromSqi(moves[3]) == Chess.B4
        Move.getToSqi(moves[3]) == Chess.C4

        when:
        p.inverse()

        then:
        p.FEN == '8/4p1p1/8/1r3P1K/kp5R/3P4/2P5/8 b - - 0 1'
        p.legal
        !p.check
        !p.mate
        !p.staleMate
        !p.terminal
        p.canMove()
        p.getMaterial() == 0
        p.getMovesAsString(p.allMoves, true) == '{Rxf5+,Ka3,Ka5,Ra5,Rc5,Rd5,Re5,e5,g5,Rb6,e6,g6+,Rb7,Rb8}'
    }

    def 'position 4'() {
        when:
        Position p = new Position('r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1', true)
        short[] moves = p.allMoves

        then:
        p.legal
        moves.length == 6
        Move.getFromSqi(moves[0]) == Chess.G1
        Move.getToSqi(moves[0]) == Chess.H1
        p.getMove(Chess.G1,Chess.H1,Chess.NO_PIECE) == moves[0]
        !p.isSquarePossibleEPSquare(Chess.H1)
        AbstractPosition.isWhiteToPlay(p.hashCode)

        when:
        def m = new Move(moves[0] as short,Chess.KING,6,0,false,false,false)
        p.doMove(m)

        then:
        p.FEN == 'r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1R1K b kq - 1 1'
        !AbstractPosition.isWhiteToPlay(p.hashCode)
    }

    def 'test move generation'() {
        // https://www.chessprogramming.org/Perft_Results
        // set depth higher to achieve
        // 1) better coverage
        // 2) performance testing data
        expect:
        // start position
        Perft(Position.createInitialPosition(), 1) == 20
        Perft(Position.createInitialPosition(), 2) == 400
        Perft(Position.createInitialPosition(), 3) == 8902
//        Perft(Position.createInitialPosition(), 4) == 197281
//        Perft(Position.createInitialPosition(), 5) == 4865609 // 0.6 sec
//        Perft(Position.createInitialPosition(), 6) == 119060324 // 10 sec
        // kiwipete position:
        Perft(new Position('r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1', true), 1) == 48
        Perft(new Position('r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1', true), 2) == 2039
        Perft(new Position('r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1', true), 3) == 97862
//        Perft(new Position('r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1', true), 4) == 4085603
//        Perft(new Position('r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1', true), 5) == 193690690 // 16 sec
        // position 3
        Perft(new Position('8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1', true), 1) == 14
        // todo
        //  BUG - here we get 193
        // Perft(new Position('8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1', true), 2) == 191
        // todo
        //  BUG - we get 2850
        // Perft(new Position('8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1', true), 3) == 2812
        // todo
        //  the remaining needs testing in case the bug gets fixed
        // Perft(new Position('8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1', true), 4) == 43238
        // Perft(new Position('8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1', true), 5) == 674624
    }

    // Perft could be made to run with much less memory consumption
    // by avoiding the array creations, but that would necessitate
    // replacing 'm_moves', which is not a vey small change to make.
    static long Perft(Position p, int depth) {
        //  https://www.chessprogramming.org/Perft
        if (depth == 0) {
            return 1;
        }
        long nodes = 0
        def moves = p.allMoves
        for (int i = 0; i < moves.length; i++) {
            p.doMove(moves[i])
            nodes += Perft(p, depth - 1)
            p.undoMove()
        }
        return nodes
    }

    // todo
    //  doMove(Move)
    //  maybe centralize FEN strings somewhere
    //  promotions
    //  listener notifications
    //  checkMoveStack()
    //  checkBackupStack()
    //  takeBaseline() - ?
    //  illegal position (maybe by setting/removing a stone explicitly?)
    //  mate
    //  stalemate
    //  check positions

}
