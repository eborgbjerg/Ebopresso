package chesspresso.game

import spock.lang.Specification

class GameHeaderModelTest extends Specification {

    def 'read 7 tag roster + other tags'() {
        given:
        DataInput input = Mock(DataInput)
        def model = new GameHeaderModel()
        and:
        1 * input.readUTF() >> '[Event "WCh."]'
        1 * input.readUTF() >> '[Site "Copenhagen"]'
        1 * input.readUTF() >> '[Date "2024.01.01"]'
        1 * input.readUTF() >> '[Round "1"]'
        1 * input.readUTF() >> '[White "Bobby"]'
        1 * input.readUTF() >> '[Black "Bent"]'
        1 * input.readUTF() >> '[Result "*"]'

        when:
        model.load(input, GameHeaderModel.MODE_SEVEN_TAG_ROASTER)

        then:
        model.white == '[White "Bobby"]'
        model.black == '[Black "Bent"]'
        model.event == '[Event "WCh."]'
        model.date == '[Date "2024.01.01"]'
        model.round == '[Round "1"]'
        model.resultStr == '[Result "*"]'
        model.tags == ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result'] as String[]

        when:
        model.setTag('Annotator', '20')

        then:
        model.getTag('Annotator') == '20'
        model.tags == ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result', 'Annotator'] as String[]

        when:
        model.removeTag('Annotator')

        then:
        model.tags == ['Event', 'Site', 'Date', 'Round', 'White', 'Black', 'Result'] as String[]
    }

}
