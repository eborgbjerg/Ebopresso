package chesspresso.game

import chesspresso.Chess
import chesspresso.position.FEN
import spock.lang.Specification

class GameTest extends Specification {

    def 'ctor'() {
        when:
        def g = new Game()
        def moves = g.position.getAllMoves()

        then:
        moves.length == 20
        g.position.getToPlay() == Chess.WHITE

        when:
        g.position.doMove(moves[0])

        then: 'after 1.Na3'
        g.position.FEN == 'rnbqkbnr/pppppppp/8/8/8/N7/PPPPPPPP/R1BQKBNR b KQkq - 1 1'
        g.goBack()
        g.position.FEN == FEN.START_POSITION
    }

}
