package chesspresso

import spock.lang.Specification

import static chesspresso.Chess.*

class ChessTest extends Specification {

    def 'coorToSqi'() {
        expect:
        coorToSqi(0,0) == A1
        coorToSqi(0,7) == A8
        coorToSqi(7,0) == H1
        coorToSqi(7,7) == H8
    }

    def 'sqiToRow'() {
        expect:
        sqiToRow(A1) == 0
        sqiToRow(A2) == 1
        sqiToRow(H8) == 7
    }

    // sqiToCol
    // deltaRow
    // deltaCol
    // colToChar
    // rowToChar
    // sqiToStr
    // isWhiteSquare
    // charToCol
    // charToRow
    // strToSqi
    // strToSqi

}
