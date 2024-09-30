package chesspresso.game

import chesspresso.Chess
import chesspresso.move.Move
import chesspresso.position.FEN
import chesspresso.position.Position
import spock.lang.Specification

class GameTest extends Specification {

    def 'create game and update it'() {
        when:
        def g = new Game()
        def moves = g.position.getAllMoves()
        GameModelChangeListener gmcl = Mock(GameModelChangeListener)
        g.addChangeListener(gmcl)

        then:
        moves.length == 20
        g.position.getToPlay() == Chess.WHITE
        g.event == null
        g.site == null
        g.round == null
        g.white == null
        g.black == null
        g.whiteElo == 0
        g.blackElo == 0
        g.whiteEloStr == null
        g.blackEloStr == null
        g.eventDate == null
        g.result == Chess.NO_RES
        g.resultStr == null
        g.curNode == 0
        g.rootNode == 0
        g.model
        g.getTags() == [] as String[]
        g.containsPosition(Position.createInitialPosition())
        g.infoString == 'null - null null'
        g.longInfoString == 'null - null, null, null  -1'
        g.nags == null
        g.comment == null
        g.currentPly == 0
        g.currentMoveNumber == 0
        g.nextMoveNumber == 1
        g.totalNumOfPlies == 0
        !g.lastMove
        !g.nextMove
        !g.nextShortMove
        !g.hasNextMove()
        g.numOfNextMoves == 0
        g.nextMoves == [] as Move[]
        g.mainLine == [] as Move[]
        !g.goForward()
        g.gotoStart()

        when:
        g.position.doMove(moves[0])

        then: 'after 1.Na3'
        g.position.FEN == 'rnbqkbnr/pppppppp/8/8/8/N7/PPPPPPPP/R1BQKBNR b KQkq - 1 1'
        g.goBack()
        g.position.FEN == FEN.START_POSITION
        1 * gmcl.moveModelChanged(g)

        when:
        g.setTag('White', 'Karpov,A.')
        g.setTag('Black', 'Larsen,B.')

        then:
        g.white == 'Karpov,A.'
        g.black == 'Larsen,B.'
        //1 * gmcl.headerModelChanged(g)
        g.getTags() == ['White', 'Black'] as String[]

        and:
        g.getHeaderString(0) == 'Karpov,A. - Larsen,B.  null  (1)'
        g.getHeaderString(1) == 'null, null, null  [null]'
        g.getHeaderString(2) == null
        g.infoString == 'Karpov,A. - Larsen,B. null'
        g.longInfoString == 'Karpov,A. - Larsen,B., null, null  -1'
    }

}
