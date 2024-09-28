package chesspresso.move

import chesspresso.Chess
import spock.lang.Specification

class MoveStaticTest extends Specification {

    def 'constants'() {
        Move.ILLEGAL_MOVE == 0 as short
        Move.SPECIAL_MOVE == 0
        Move.getString(Move.ILLEGAL_MOVE)
    }

    def 'normalize order - empty'() {
        given:
        def moves = new short[]{}

        when:
        Move.normalizeOrder(moves)

        then:
        moves.length == 0
    }

    def 'normalize order - 1 move'() {
        given:
        def moves = new short[]{0}

        when:
        Move.normalizeOrder(moves)

        then:
        moves.length == 1
        moves[0] == 0 as short
    }

    def 'getRegularMove'() {
        expect:
        Move.getRegularMove(Chess.A1,Chess.A1,false) == 4096 as short
        Move.getString(4096 as short) == 'a1-a1'
        Move.getRegularMove(Chess.A1,Chess.A2,false) == 4608 as short
        Move.getString(4608 as short) == 'a1-a2'
        Move.getRegularMove(Chess.A1,Chess.A8,true) == -25088 as short
        Move.getString(-25088 as short) == 'a1xa8'
        Move.getRegularMove(Chess.A1,Chess.A8,false) == 7680 as short
        Move.getString(7680 as short) == 'a1-a8'
    }

    def 'getPawnMove'() {
        expect:
        Move.getPawnMove(Chess.A1,Chess.A1, false, Chess.NO_PIECE) == 4096 as short
        Move.getString(4096 as short) == 'a1-a1'
        Move.getPawnMove(Chess.A1,Chess.A1, true, Chess.NO_PIECE) == -28672 as short
        Move.getString(-28672 as short) == 'a1xa1'
    }

    def 'getEPMove'() {
        expect:
        Move.getEPMove(Chess.E5, Chess.F6) == -5276 as short
        Move.getString(-5276 as short) == 'e5xf6'
        Move.getEPMove(Chess.E4, Chess.F3) == -6820 as short
        Move.getString(-6820 as short) == 'e4xf3'
    }

    def 'getShortCastle'() {
        expect:
        Move.getShortCastle(Chess.WHITE) == 29060 as short
        Move.getFromSqi(29060 as short) == Chess.E1
        Move.getToSqi(29060 as short) == Chess.G1
        !Move.isCapturing(29060 as short)
        !Move.isPromotion(29060 as short)
        !Move.isEPMove(29060 as short)
        Move.isCastle(29060 as short)
        !Move.isLongCastle(29060 as short)
        Move.isShortCastle(29060 as short)
        Move.isValid(29060 as short)
        !Move.isSpecial(29060 as short)
        Move.getPromotionPiece(29060 as short) == Chess.NO_PIECE
        Move.getBinaryString(29060 as short) == '0111000110000100'
        Move.getString(29060 as short) == 'O-O'

        Move.getShortCastle(Chess.BLACK) == 32700 as short
        Move.getFromSqi(32700 as short) == Chess.E8
        Move.getToSqi(32700 as short) == Chess.G8
        !Move.isCapturing(32700 as short)
        !Move.isPromotion(32700 as short)
        !Move.isEPMove(32700 as short)
        Move.isCastle(32700 as short)
        !Move.isLongCastle(32700 as short)
        Move.isShortCastle(32700 as short)
        Move.isValid(32700 as short)
        !Move.isSpecial(32700 as short)
        Move.getPromotionPiece(32700 as short) == Chess.NO_PIECE
        Move.getBinaryString(32700 as short) == '0111111110111100'
        Move.getString(32700 as short) == 'O-O'
    }

    def 'getLongCastle'() {
        expect:
        Move.getLongCastle(Chess.WHITE) == 28804 as short
        Move.getFromSqi(28804 as short) == Chess.E1
        Move.getToSqi(28804 as short) == Chess.C1
        !Move.isCapturing(28804 as short)
        !Move.isPromotion(28804 as short)
        !Move.isEPMove(28804 as short)
        Move.isCastle(28804 as short)
        Move.isLongCastle(28804 as short)
        !Move.isShortCastle(28804 as short)
        Move.isValid(28804 as short)
        !Move.isSpecial(28804 as short)
        Move.getPromotionPiece(28804 as short) == Chess.NO_PIECE
        Move.getBinaryString(28804 as short) == '0111000010000100'
        Move.getString(28804 as short) == 'O-O-O'

        Move.getLongCastle(Chess.BLACK) == 32444 as short
        Move.getFromSqi(32444 as short) == Chess.E8
        Move.getToSqi(32444 as short) == Chess.C8
        !Move.isCapturing(32444 as short)
        !Move.isPromotion(32444 as short)
        !Move.isEPMove(32444 as short)
        Move.isCastle(32444 as short)
        Move.isLongCastle(32444 as short)
        !Move.isShortCastle(32444 as short)
        Move.isValid(32444 as short)
        !Move.isSpecial(32444 as short)
        Move.getPromotionPiece(32444 as short) == Chess.NO_PIECE
        Move.getBinaryString(32444 as short) == '0111111010111100'
        Move.getString(32444 as short) == 'O-O-O'
    }

    def 'createIllegalMove'() {
        when:
        def m = Move.createIllegalMove()

        then:
        !m.capturing
        !m.check
    }

    def 'create castling moves'() {
        when:
        Move m = Move.createShortCastle(0, false, false, false)

        then:
        m.valid

        when:
        m = Move.createLongCastle(0, false, false, false)

        then:
        m.valid

        when:
        m = Move.createCastle(0 as short, false, false, false)

        then:
        !m.valid
    }


}
