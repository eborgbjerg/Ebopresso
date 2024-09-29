package chesspresso.position

import chesspresso.Chess
import chesspresso.move.IllegalMoveException
import spock.lang.Specification

class LightWeightPositionTest extends Specification {

    def 'all'() {
        given: 'a LightWeightPosition'
        def positionListener = Mock(PositionListener)
        def positionChangeListener = Mock(PositionChangeListener)
        def p = new LightWeightPosition(Position.createInitialPosition())
        p.addPositionListener(positionListener)
        p.addPositionChangeListener(positionChangeListener)

        expect: 'post-construction state'
        p.legal
        !p.canUndoMove()
        !p.undoMove()
        !p.canRedoMove()
        !p.redoMove()
        p.toPlay == Chess.WHITE
        p.castles == 0b1111

        when:
        p.doMove(0 as short)
        then: 'doMove fails'
        thrown(IllegalMoveException)

        when:
        p.getLastMove()
        then: 'get last move fails'
        thrown(IllegalMoveException)

        when:
        p.getLastShortMove()
        then: 'get last short move fails'
        thrown(IllegalMoveException)

        when: 'we update the position a bit'
        p.halfMoveClock = 42
        p.plyNumber = 10
        p.toPlay = Chess.BLACK
        p.setStone(Chess.A1, Chess.WHITE_BISHOP)
        p.setCastles(0b1110)
        p.setSqiEP(Chess.E3)
        then: 'the update is reflected'
        p.halfMoveClock == 42
        p.plyNumber == 10
        p.toPlay == Chess.BLACK
        p.getStone(Chess.A1) == Chess.WHITE_BISHOP
        p.getCastles() == 0b1110
        p.getSqiEP() == Chess.E3
        !p.legal

        and: 'listeners got notified about the updates'
        1 * positionListener.halfMoveClockChanged(42)
        1 * positionListener.plyNumberChanged(10)
        1 * positionListener.toPlayChanged(Chess.BLACK)
        1 * positionListener.squareChanged(Chess.A1, Chess.WHITE_BISHOP)
        1 * positionListener.castlesChanged(0b1110)
        1 * positionListener.sqiEPChanged(Chess.E3)
        6 * positionChangeListener.notifyPositionChanged(p)

        and: 'that was all!'
        0 * positionListener._
        0 * positionChangeListener._
    }

}
