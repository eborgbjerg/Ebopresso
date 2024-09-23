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

    def 'sqiToCol'() {
        expect:
        sqiToCol(A1) == 0
        sqiToCol(B1) == 1
        sqiToCol(H1) == 7
    }

    def 'deltaRow'() {
        expect:
        deltaRow(A1, A1) == 0
        deltaRow(B1, C1) == 0
        deltaRow(B1, B2) == 1
        deltaRow(A1, A8) == 7
        deltaRow(B2, B1) == -1
    }

    def 'deltaCol'() {
        expect:
        deltaCol(A1, A1) == 0
        deltaCol(A1, A2) == 0
        deltaCol(A1, B2) == 1
        deltaCol(A1, H8) == 7
        deltaCol(H8, A1) == -7
    }

    def 'colToChar'() {
        expect:
        colToChar(0) == ('a' as char)
        colToChar(1) == ('b' as char)
        colToChar(7) == ('h' as char)
    }

    def 'rowToChar'() {
        expect:
        rowToChar(0) == ('1' as char)
        rowToChar(1) == ('2' as char)
        rowToChar(7) == ('8' as char)
    }

    def 'sqiToStr'() {
        expect:
        sqiToStr(A1) == 'a1'
        sqiToStr(A2) == 'a2'
        sqiToStr(B2) == 'b2'
        sqiToStr(G7) == 'g7'
        sqiToStr(H8) == 'h8'
    }

    def 'isWhiteSquare'() {
        expect:
        !isWhiteSquare(A1)
        !isWhiteSquare(A3)
        !isWhiteSquare(H8)
        isWhiteSquare(A2)
        isWhiteSquare(H1)
        isWhiteSquare(D5)
        isWhiteSquare(A8)
    }

    def 'charToCol'() {
        expect:
        charToCol('a' as char) == 0
        charToCol('b' as char) == 1
        charToCol('h' as char) == 7
        charToCol('i' as char) == NO_COL
    }

    def 'charToRow'() {
        expect:
        charToRow('1' as char) == 0
        charToRow('2' as char) == 1
        charToRow('8' as char) == 7
        charToRow('9' as char) == NO_ROW
    }

    def 'strToSqi'() {
        expect:
        strToSqi('a1') == A1
        strToSqi('b1') == B1
        strToSqi('b2') == B2
        strToSqi('h8') == H8
        strToSqi(null) == NO_SQUARE
        strToSqi('i8') == NO_SQUARE
        strToSqi('h9') == NO_SQUARE
    }

    def 'strToSqi #2'() {
        expect:
        strToSqi('a' as char, '1' as char) == A1
        strToSqi('a' as char, '2' as char) == A2
        strToSqi('b' as char, '2' as char) == B2
        strToSqi('i' as char, '2' as char) == NO_SQUARE
        strToSqi('h' as char, '9' as char) == NO_SQUARE
    }

    def 'stoneToColor'() {
        expect:
        stoneToColor(WHITE_KING) == WHITE
        stoneToColor(WHITE_KNIGHT) == WHITE
        stoneToColor(BLACK_BISHOP) == BLACK
        stoneToColor(BLACK_KNIGHT) == BLACK
        stoneToColor(NO_STONE) == NOBODY
    }

    def 'stoneHasColor'() {
        expect:
        stoneHasColor(WHITE_KING, WHITE)
        stoneHasColor(WHITE_KNIGHT, WHITE)
        stoneHasColor(BLACK_BISHOP, BLACK)
    }

    def 'stoneToPiece'() {
        expect:
        stoneToPiece(WHITE_KNIGHT) == KNIGHT
        stoneToPiece(BLACK_ROOK) == ROOK
    }

    def 'getOpponentStone'() {
        expect:
        getOpponentStone(NO_STONE) == NO_STONE
        getOpponentStone(WHITE_KNIGHT) == BLACK_KNIGHT
        getOpponentStone(BLACK_KNIGHT) == WHITE_KNIGHT
    }

    def 'charToPiece'() {
        expect:
        charToPiece('N' as char) == KNIGHT
        charToPiece('R' as char) == ROOK
        charToPiece('r' as char) == NO_PIECE
    }

    def 'pieceToChar'() {
        expect:
        pieceToChar(ROOK) == 'R' as char
        pieceToChar(QUEEN) == 'Q' as char
        pieceToChar(WHITE_ROOK) == '?' as char
        pieceToChar(MAX_PIECE + 1) == '?' as char
        pieceToChar(NO_PIECE) == ' ' as char
        pieceToChar(MAX_PIECE) == 'K' as char
    }

    def 'stoneToChar'() {
        expect:
        stoneToChar(WHITE_ROOK) == 'R' as char
        stoneToChar(BLACK_KNIGHT) == 'N' as char
        stoneToChar(WHITE_KNIGHT) == 'N' as char
        stoneToChar(MAX_STONE) == 'K' as char
        stoneToChar(MIN_STONE) == 'K' as char
        stoneToChar(NO_STONE) == ' ' as char
    }

    def 'stoneToChar - illegal args'() {
        when:
        stoneToChar(stone)
        then:
        thrown(ArrayIndexOutOfBoundsException)
        where:
        stone         || _
        MAX_STONE + 1 || _
        MIN_STONE - 1 || _
    }

    def 'pieceToStone'() {
        expect:
        pieceToStone(KNIGHT, BLACK) == BLACK_KNIGHT
        pieceToStone(ROOK, WHITE) == WHITE_ROOK
        pieceToStone(ROOK, 2) == NO_PIECE
    }

    def 'otherPlayer'() {
        expect:
        otherPlayer(WHITE) == BLACK
        otherPlayer(BLACK) == WHITE
        otherPlayer(NOBODY) == 2
    }

    def 'isWhitePly'() {
        expect:
        isWhitePly(0)
        !isWhitePly(1)
    }

    def 'plyToMoveNumber'() {
        expect:
        plyToMoveNumber(0) == 1
        plyToMoveNumber(1) == 1
        plyToMoveNumber(2) == 2
    }

}
