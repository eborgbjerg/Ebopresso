package chesspresso.move

import chesspresso.Chess
import spock.lang.Specification

class MoveInstanceTest extends Specification {

    def 'ctor'() {
        when:
        def m = new Move(0 as short, 0, 0, 0, false, false, false)

        then:
        !m.check
        !m.capturing
        !m.mate
        m.colFrom == 0
        m.rowFrom == 0

        and: 'the move is illegal, so cannot be parsed'
        m.getLAN() == '<illegal move>'
        m.getSAN() == '<illegal move>'
    }

    def 'e2-e4'() {
        given:
        short e2e4 = Move.getPawnMove(Chess.E2,Chess.E4, false, Chess.NO_PIECE)

        when:
        def e2_e4 = new Move(e2e4, Chess.PAWN, 4, 2, check, false, true)

        then:
        e2_e4.check == check
        e2_e4.valid
        !e2_e4.capturing
        !e2_e4.mate
        e2_e4.LAN == (check? 'e2-e4+' : 'e2-e4')
        e2_e4.SAN == (check? 'e3e4+' : 'e3e4')  // from E3 ???
        e2_e4.toString() == e2_e4.SAN
        !e2_e4.longCastle
        !e2_e4.shortCastle
        !e2_e4.promotion
        e2_e4.promo == Chess.NO_PIECE
        e2_e4.shortMoveDesc == 5900 as short
        e2_e4.colFrom == 4
        e2_e4.rowFrom == 2
        e2_e4.whiteMove
        e2_e4.equals(e2_e4)

        where:
        check || _
        false || _
        true  || _
    }

    def 'e2xf3'() {
        given:
        short e2f3 = Move.getPawnMove(Chess.E2,Chess.F3, true, Chess.NO_PIECE)

        when:
        def e2xf3 = new Move(e2f3, Chess.PAWN, 4, 2, false, false, true)

        then:
        !e2xf3.check
        e2xf3.valid
        e2xf3.capturing
        !e2xf3.mate
        e2xf3.LAN == 'e2xf3'
        e2xf3.SAN == 'e3xf3'  // from E3 ???
        e2xf3.toString() == e2xf3.SAN
        !e2xf3.longCastle
        !e2xf3.shortCastle
        !e2xf3.promotion
        e2xf3.promo == Chess.NO_PIECE
        e2xf3.shortMoveDesc == -27316 as short
        e2xf3.colFrom == 4
        e2xf3.rowFrom == 2
        e2xf3.whiteMove
        e2xf3.equals(e2xf3)
    }

    def 'e5xf6 (e.p.)'() {
        given:
        short e5f6 = Move.getPawnMove(Chess.E5,Chess.F6, true, Chess.NO_PIECE)

        when:
        def e5xf6_ep = new Move(e5f6, Chess.PAWN, 4, 5, false, false, true)

        then:
        !e5xf6_ep.check
        e5xf6_ep.valid
        e5xf6_ep.capturing
        !e5xf6_ep.mate
        e5xf6_ep.LAN == 'e5xf6'
        e5xf6_ep.SAN == 'e6xf6'  // from E6 ???
        e5xf6_ep.toString() == e5xf6_ep.SAN
        !e5xf6_ep.longCastle
        !e5xf6_ep.shortCastle
        !e5xf6_ep.promotion
        e5xf6_ep.promo == Chess.NO_PIECE
        e5xf6_ep.shortMoveDesc == -25756 as short
        e5xf6_ep.colFrom == 4
        e5xf6_ep.rowFrom == 5
        e5xf6_ep.whiteMove
        e5xf6_ep.equals(e5xf6_ep)
    }

    def 'e7-e5'() {
        given:
        short e7e5 = Move.getPawnMove(Chess.E7,Chess.E5, false, Chess.NO_PIECE)

        when:
        def e7_e5 = new Move(e7e5, Chess.PAWN, 4, 6, check, false, false)

        then:
        e7_e5.check == check
        e7_e5.valid
        !e7_e5.capturing
        !e7_e5.mate
        e7_e5.LAN == (check? 'e7-e5+' : 'e7-e5')
        e7_e5.SAN == (check? 'e7e5+' : 'e7e5')
        e7_e5.toString() == e7_e5.SAN
        !e7_e5.longCastle
        !e7_e5.shortCastle
        !e7_e5.promotion
        e7_e5.promo == Chess.NO_PIECE
        e7_e5.shortMoveDesc == 6452 as short
        e7_e5.colFrom == 4
        e7_e5.rowFrom == 6
        !e7_e5.whiteMove
        e7_e5.equals(e7_e5)

        where:
        check || _
        false || _
        true  || _
    }

    def 'not equals'() {
        given:
        short e7e5 = Move.getPawnMove(Chess.E7,Chess.E5, false, Chess.NO_PIECE)
        def e7_e5 = new Move(e7e5, Chess.PAWN, 4, 6, false, false, false)
        short e2e4 = Move.getPawnMove(Chess.E2,Chess.E4, false, Chess.NO_PIECE)
        def e2_e4 = new Move(e2e4, Chess.PAWN, 4, 2, false, false, true)

        expect:
        !e2_e4.equals(e7_e5)
        !e2_e4.equals('str')
    }

    def 'short castling'() {
        given:
        short oo = Move.getShortCastle(toMove)
        int rowFrom = toMove==Chess.WHITE? 1 : 7

        when:
        def OO = new Move(oo, Chess.KING, 4, rowFrom, check, false, toMove==Chess.WHITE)

        then:
        OO.check == check
        OO.valid
        !OO.capturing
        !OO.mate
        OO.LAN == (check? 'O-O+' : 'O-O')
        OO.SAN == (check? 'O-O+' : 'O-O')
        OO.toString() == OO.SAN
        !OO.longCastle
        OO.shortCastle
        !OO.promotion
        OO.promo == Chess.NO_PIECE
        OO.shortMoveDesc == shortMoveDesc as short
        OO.colFrom == 4
        OO.rowFrom == rowFrom
        OO.whiteMove == (toMove==Chess.WHITE)
        OO.equals(OO)

        where:
        toMove      | check | shortMoveDesc
        Chess.WHITE | false | 29060
        Chess.WHITE | true  | 29060
        Chess.BLACK | false | 32700
        Chess.BLACK | true  | 32700
    }

    def 'long castling'() {
        given:
        short ooo = Move.getLongCastle(toMove)
        int rowFrom = toMove==Chess.WHITE? 1 : 7

        when:
        def OOO = new Move(ooo, Chess.KING, 4, rowFrom, check, false, toMove==Chess.WHITE)

        then:
        OOO.check == check
        OOO.valid
        !OOO.capturing
        !OOO.mate
        OOO.LAN == (check? 'O-O-O+' : 'O-O-O')
        OOO.SAN == (check? 'O-O-O+' : 'O-O-O')
        OOO.toString() == OOO.SAN
        OOO.longCastle
        !OOO.shortCastle
        !OOO.promotion
        OOO.promo == Chess.NO_PIECE
        OOO.shortMoveDesc == shortMoveDesc as short
        OOO.colFrom == 4
        OOO.rowFrom == rowFrom
        OOO.whiteMove == (toMove==Chess.WHITE)
        OOO.equals(OOO)

        where:
        toMove      | check | shortMoveDesc
        Chess.WHITE | false | 28804
        Chess.WHITE | true  | 28804
        Chess.BLACK | false | 32444
        Chess.BLACK | true  | 32444
    }

    // todo
    //  getMovingPiece == NO_PIECE (LAN + SAN)
    //  promotion
    //  regular move (not pawn, not castling)
    //  special move

}
