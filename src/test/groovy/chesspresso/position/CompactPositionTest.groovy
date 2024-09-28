package chesspresso.position

import spock.lang.Ignore
import spock.lang.Specification

class CompactPositionTest extends Specification {

    @Ignore('seems not to work')
    def 'ctor 1'() {
        given:
        def p = new CompactPosition()

        expect:
        p.legal
    }

    @Ignore('seems not to work')
    def 'ctor 2'() {
        given:
        def p = new CompactPosition(Position.createInitialPosition())

        expect:
        p.legal
    }

}
