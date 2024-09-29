package chesspresso.position

import chesspresso.Chess
import spock.lang.Specification

class CompactPositionTest extends Specification {

    def 'ctor 1'() {
        given:
        def p = new CompactPosition()

        expect:
        // this is hardly how it was intended to work
        // I see no way to populate the board from here
        !p.legal
        p.FEN == '8/8/8/8/8/8/8/8 w KQkq - 0 1'

        and: 'query some more to increase coverage'
        p.getStone(Chess.A1) == Chess.NO_STONE
        p.sqiEP == Chess.NO_SQUARE
        p.castles == 0b1111
        p.toPlay == Chess.WHITE
        p.plyNumber == 0
        p.halfMoveClock == 0
    }

    def 'ctor 2'() {
        given:
        def p = new CompactPosition(Position.createInitialPosition())

        expect:
        // this is hardly how it was intended to work
        // I see no way to populate the board from here
        !p.legal
        p.FEN == '8/8/8/8/8/8/8/8 w KQkq - 0 1'
    }

}
