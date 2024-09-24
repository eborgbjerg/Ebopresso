package chesspresso.move

import chesspresso.Chess
import spock.lang.Specification

class MoveStaticTest extends Specification {

    def 'constants'() {
        Move.ILLEGAL_MOVE == 0 as short
        Move.SPECIAL_MOVE == 0 as short
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
        Move.getRegularMove(Chess.A1,Chess.A2,false) == 4608 as short
    }

    def 'getPawnMove'() {
        expect:
        Move.getPawnMove(Chess.A1,Chess.A1, false, 0) == 4096 as short
    }

    // getEPMove
    // getShortCastle
    // getLongCastle
    // getFromSqi
    // getToSqi
    // isCapturing
    // isPromotion
    // getPromotionPiece
    // isEPMove
    // isCastle
    // isShortCastle
    // isLongCastle
    // isSpecial
    // isValid
    // getBinaryString
    // getString
    //

    def 'createIllegalMove'() {
        when:
        def m = Move.createIllegalMove()

        then:
        !m.capturing
        !m.check
    }

    // createCastle
    // createShortCastle
    // createLongCastle

}
