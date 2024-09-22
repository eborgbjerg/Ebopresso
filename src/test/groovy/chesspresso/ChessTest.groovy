package chesspresso

import spock.lang.Specification

class ChessTest extends Specification {

    def 'x'() {
        expect:
        Chess.A1 < Chess.A2
    }

}
