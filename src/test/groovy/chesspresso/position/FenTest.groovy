package chesspresso.position

import chesspresso.Chess
import spock.lang.Specification

class FenTest extends Specification {

    def 'stone -> char'() {
        expect: 'valid stone -> correct char'
        FEN.stoneToFenChar(Chess.WHITE_KING) == 'K' as char
        FEN.stoneToFenChar(Chess.BLACK_KING) == 'k' as char

        and: 'invalid stones -> ?'
        FEN.stoneToFenChar(Chess.MAX_STONE + 1) == '?' as char
    }

    def 'char -> stone'() {
        expect: 'valid char -> correct stone'
        FEN.fenCharToStone('K' as char) == Chess.WHITE_KING
        FEN.fenCharToStone('k' as char) == Chess.BLACK_KING

        and: 'invalid char -> no stone'
        FEN.fenCharToStone('Z' as char) == Chess.NO_STONE
    }

    def 'init from fen - start position'() {
        given:
        LightWeightPosition p = new LightWeightPosition()

        when:
        FEN.initFromFEN(p, FEN.START_POSITION, strict)

        then:
        p.hashCode == 6186144174769381545
        p.FEN == FEN.START_POSITION
        p.toPlay == Chess.WHITE
        p.castles == 0b1111
        p.halfMoveClock == 0
        p.plyNumber == 0
        p.sqiEP == Chess.NO_SQUARE

        when:
        FEN.initFromFEN(p, '4k3/4r3/8/4N3/8/8/4R3/4K3 w - - 0 1', strict)

        then:
        p.hashCode == 5519181397842373324
        p.FEN == '4k3/4r3/8/4N3/8/8/4R3/4K3 w - - 0 1'
        p.toPlay == Chess.WHITE
        p.castles == 0b0000
        p.halfMoveClock == 0
        p.plyNumber == 0
        p.sqiEP == Chess.NO_SQUARE

        where:
        strict || _
        true   || _
        false  || _
    }

    def 'init from fen'() {
        given:
        LightWeightPosition p = new LightWeightPosition()

        when:
        FEN.initFromFEN(p, '4k3/4r3/8/4N3/8/8/4R3/4K3 ' + toPlay + ' - ' + ep_square + ' 0 1', strict)

        then:
        p.hashCode == hash
        p.FEN == '4k3/4r3/8/4N3/8/8/4R3/4K3 ' + toPlay + ' - ' + ep_square + ' 0 1'
        p.toPlay == (toPlay == 'w'? Chess.WHITE : Chess.BLACK)
        p.castles == 0b0000
        p.halfMoveClock == 0
        p.plyNumber == (toPlay == 'w'? 0 : 1)
        p.sqiEP == (ep_square == '-'? Chess.NO_SQUARE : Chess.strToSqi(ep_square))

        where:
        strict | toPlay | ep_square | hash
        true   | 'w'    | '-'       | 5519181397842373324
        false  | 'w'    | '-'       | 5519181397842373324
        true   | 'b'    | '-'       | 5519181397850761932
        false  | 'b'    | '-'       | 5519181397850761932
        false  | 'b'    | 'e3'      | 5519181397850761932
    }

    def 'init from invalid fen'() {
        given:
        LightWeightPosition p = new LightWeightPosition()

        when:
        FEN.initFromFEN(p, fen, true)

        then:
        thrown(exceptionType)

        where:
        fen                                                        | exceptionType
        // row empty
        '/4r3/8/4N3/8/8/4R3/4K3 w - - 0 1'                         | IllegalArgumentException
        // too many pieces in a row
        'NNNNNNNNN/4r3/8/4N3/8/8/4R3/4K3 w - - 0 1'                | ArrayIndexOutOfBoundsException
        // missing k
        '8/8/8/4N3/8/8/4R3/4K3 w - - 0 1'                          | IllegalArgumentException
        // missing fields
        '4k3/4r3/8/4N3/8/8/4R3/4K3'                                | IllegalArgumentException
        // wrong counts
        '4k2/4r3/8/4N3/8/8/4R3/4K3 w - - 0 1'                      | IllegalArgumentException
        '4k3/4r3/7/4N3/8/8/4R3/4K3 w - - 0 1'                      | IllegalArgumentException
        '4k3/4r3/8/4N3/8/0/4R3/4K3 w - - 0 1'                      | IllegalArgumentException
        '4k3/4r3/8/4N3/8/8/4R3/4K4 w - - 0 1'                      | IllegalArgumentException
        '/8// w - - 0 1'                                           | IllegalArgumentException
        '4k3/4r3/8/4N3/8/8/4R3/4K3 X - - 0 1'                      | IllegalArgumentException
        // invalid castling char
        'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w X - 0 1'    | IllegalArgumentException
        // missing pieces at the end
        '4k3/4r3/8/4N3/8/8/4R3/4K2 w - - 0 1'                      | IllegalArgumentException
        // missing full move no
        '4k3/4r3/8/4N3/8/8/4R3/4K3 w - - 0  '                      | IllegalArgumentException
        // fen stops before the e.p. square
        'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq'       | IllegalArgumentException
        // fen stops before halfmove clock
        'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -'     | IllegalArgumentException
        // fen stops before full move number
        'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0'   | IllegalArgumentException
        // nonsense e.p. square
        'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq Y 0 1' | IllegalArgumentException
    }

}
