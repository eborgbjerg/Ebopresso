package chesspresso.move

import spock.lang.Specification

class MoveInstanceTest extends Specification {

    def 'ctor'() {
        when:
        def m = new Move(0 as short, 0, 0, 0, false, false, false)

        then:
        !m.check
        !m.capturing
        !m.mate
        m.colFrom == 0
        m.rowFrom == 0

        and: 'the move is illegal, so cannot be parsed'
        m.getLAN() == '<illegal move>'
        m.getSAN() == '<illegal move>'
    }

}
