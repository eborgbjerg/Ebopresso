package chesspresso.position

import spock.lang.Ignore
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
