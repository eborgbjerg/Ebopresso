package chesspresso.game

import chesspresso.Chess
import chesspresso.move.Move
import spock.lang.Specification

class GameMoveModelTest extends Specification {

    def 'read moves - empty game (corner case)'() {
        given: 'an (empty) game move model'
        DataInput input = Stub(DataInput)
        DataOutput output = Stub(DataOutput)
        def model = new GameMoveModel()

        when: 'loading the input'
        model.load(input, 0)
        then: 'there are no moves'
        model.totalNumOfPlies == 0
        model.totalCommentSize == 0
        model.getMove(0) == 0 as short
        model.getComment(0) == null
        model.getNumOfNextMoves(0) == 0
        !model.hasNag(0, 1 as short)
        !model.hasLines()

        when:
        model.getNags(0)
        then:
        thrown(RuntimeException)

        when:
        model.addNag(0, 1 as short)
        then:
        thrown(RuntimeException)

        when:
        model.removeNag(0, 1 as short)
        then:
        thrown(RuntimeException)

        when:
        model.pack(0)
        then:
        _

        when:
        model.save(output, 0)
        then:
        _

        when:
        model.addComment(0, 'before first move')
        then:
        model.getComment(0) == 'before first move'

        when:
        model.setComment(0, 'changed the comment!')
        then:
        model.getComment(0) == 'changed the comment!'

        when:
        model.removeComment(0)
        then:
        model.getComment(0) == null

        when:
        model.deleteCurrentLine(0)

        then:
        _
    }

    def 'a short game'() {
        given: 'a game move model'
        def model = new GameMoveModel()

        expect:
        !model.hasNextMove(0)
        !model.hasLines()
        model.hashCode == 0
        model.getMove(0) == Move.NO_MOVE

        when:
        model.appendAsRightMostLine(0, Move.getPawnMove(Chess.E2, Chess.E4, false, Chess.NO_PIECE))

        then:
        model.totalNumOfPlies == 1
        model.totalCommentSize == 0
        model.hashCode == 11800

        when:
        def n = model.goBack(0, true)

        then:
        n == -1
        model.hasNextMove(0)
        model.goForward(0) == 1
        // these methods don't do what one might think!?
        model.hasNextMove(0)
        model.goForward(0) == 1
        model.hasNextMove(0)
        model.hashCode == 11800

        when:
        model.goBack(0, true)

        then:
        model.hashCode == 11800
    }

}
