package chesspresso.pgn

import chesspresso.game.Game
import spock.lang.Specification

class PGNWriterTest extends Specification {

    def 'ctor'() {
        given:
        Writer writer = new StringWriter()
        def pgnWriter = new PGNWriter(writer)
        def g = new Game()
        short[] moves = g.position.getAllMoves()
        g.position.doMove(moves[0])
        g.model.headerModel.setTag('Result', '*')

        when:
        pgnWriter.write(g.getModel())

        then:
        writer.toString().startsWith('[Event ')
        writer.toString().contains('[Result ')
    }

}
