package chesspresso.game

import chesspresso.Chess
import spock.lang.Specification

class GameHeaderModelTest extends Specification {

    def 'read 7 tag roster, standard tags + a custom tag'() {
        given: 'a game header model'
        DataInput input = Mock(DataInput)
        DataOutput output = Mock(DataOutput)
        def model = new GameHeaderModel()
        and:
        1 * input.readUTF() >> '[Event "WCh."]'
        1 * input.readUTF() >> '[Site "Copenhagen"]'
        1 * input.readUTF() >> '[Date "2024.01.01"]'
        1 * input.readUTF() >> '[Round "1"]'
        1 * input.readUTF() >> '[White "Bobby"]'
        1 * input.readUTF() >> '[Black "Bent"]'
        1 * input.readUTF() >> '[Result "*"]'
        if (mode == GameHeaderModel.MODE_STANDARD_TAGS) {
            1 * input.readUTF() >> '[WhiteElo "2700"]'
            1 * input.readUTF() >> '[BlackElo "2650"]'
            1 * input.readUTF() >> '[EventDate "2024.01.01"]'
            1 * input.readUTF() >> '[ECO "E12"]'
        }

        when: 'loading the headers'
        model.load(input, mode)

        then: 'the headers are reflected'
        model.white == '[White "Bobby"]'
        model.black == '[Black "Bent"]'
        model.event == '[Event "WCh."]'
        model.date == '[Date "2024.01.01"]'
        model.round == '[Round "1"]'
        model.resultStr == '[Result "*"]'
        model.result == Chess.NO_RES
        model.whiteElo == 0
        model.blackElo == 0
        model.eventDate == (mode == GameHeaderModel.MODE_SEVEN_TAG_ROASTER ? null : '[EventDate "2024.01.01"]')
        model.blackEloStr == (mode == GameHeaderModel.MODE_SEVEN_TAG_ROASTER ? null : '[BlackElo "2650"]')
        model.whiteEloStr == (mode == GameHeaderModel.MODE_SEVEN_TAG_ROASTER ? null : '[WhiteElo "2700"]')
        model.ECO == (mode == GameHeaderModel.MODE_SEVEN_TAG_ROASTER ? null : '[ECO "E12"]')
        model.site == '[Site "Copenhagen"]'
        model.tags == (mode == GameHeaderModel.MODE_SEVEN_TAG_ROASTER ? ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result'] : ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result', 'WhiteElo', 'BlackElo', 'EventDate', 'ECO']) as String[]
        model.toString() == '[White "Bobby"] - [Black "Bent"] [Result "*"] ([Date "2024.01.01"])'

        when: 'adding a tag'
        model.setTag('Annotator', '20')

        then: 'the added tag is reflected'
        model.getTag('Annotator') == '20'
        model.tags == (mode == GameHeaderModel.MODE_SEVEN_TAG_ROASTER ? ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result', 'Annotator'] : ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result', 'WhiteElo', 'BlackElo', 'EventDate', 'ECO', 'Annotator']) as String[]

        when: 'removing the tag'
        model.removeTag('Annotator')

        then: 'the tag is gone'
        model.tags == (mode == GameHeaderModel.MODE_SEVEN_TAG_ROASTER ? ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result'] : ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result', 'WhiteElo', 'BlackElo', 'EventDate', 'ECO']) as String[]

        when: 'saving the headers'
        model.save(output, mode)

        then: 'all tags get saved'
        7 * output.writeUTF(_ as String)
        if (mode == GameHeaderModel.MODE_STANDARD_TAGS) {
            4 * output.writeUTF(_ as String)
        }

        where:
        mode                                   || _
        GameHeaderModel.MODE_SEVEN_TAG_ROASTER || _
        GameHeaderModel.MODE_STANDARD_TAGS     || _
    }

}
