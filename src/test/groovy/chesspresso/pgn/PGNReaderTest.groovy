package chesspresso.pgn

import chesspresso.game.GameModel
import spock.lang.Specification

class PGNReaderTest extends Specification {

    def 'johns test'() {
        given:
        String pgn =
                "[Event \"FICS rated lightning game\"]" +
                        "[Site \"FICS\"]" +
                        "[FICSGamesDBGameNo \"240071715\"]" +
                        "[White \"metalshredde\"]" +
                        "[Black \"Anna\"]" +
                        "[WhiteElo \"1745\"]" +
                        "[BlackElo \"1627\"]" +
                        "[TimeControl \"60+0\"]" +
                        "[Date \"2010.01.01\"]" +
                        "[Time \"23:02:00\"]" +
                        "[WhiteClock \"0:01:00.000\"]" +
                        "[BlackClock \"0:01:00.000\"]" +
                        "[ECO \"C16\"]" +
                        "[PlyCount \"47\"]" +
                        "[Result \"1-0\"]" +

                        "1. e4 e6 2. d4 d5 3. Nc3 Bb4 4. e5 Bxc3+ 5. bxc3 c5 6. Qg4 c4 7. Qxg7 Ne7 8." +
                        "Qxh8+ Kd7 9. Qxh7 Qb6 10. Qxf7 Qa5 11. Bd2 Nc6 12. Nf3 a6 13. Ng5 Kc7 14. Nxe6+ " +
                        "Kb8 15. Nc5 b5 16. Be2 Ra7 17. O-O Nf5 18. Qf6 Qd8 19. Qxc6 Bb7 20. Nxb7 Rxb7 " +
                        "21. Qxa6 Kc7 22. Rab1 Qb8 23. Bf4 Kd7 24. e6+ {Black resigns} 1-0";

        InputStream stream = new ByteArrayInputStream(pgn.getBytes());
        PGNReader rdr = new PGNReader(stream, "test");
        GameModel model = rdr.parseGame()

        expect:
        with (model.headerModel) {
            resultStr == '1-0'
            white == 'metalshredde'
            black == 'Anna'
            blackElo == 1627
            whiteElo == 1745
        }
        with (model.moveModel) {
            hashCode == -46130833
            totalNumOfPlies == 47
            totalCommentSize == 13
        }
    }

    // todo
    //  https://github.com/BernhardSeybold/Chesspresso/issues/4#issue-2100816741
    //
    // I played a bit with the chesspresso library as part of my bachelor's thesis and found an error.
    // I noticed that, when a certain situation occurs, the PGN Reader makes the wrong move and,
    // as a result, messes up the entire game.
    //
    // The situation is the following:
    // When there exist two knights, which can jump to one common field,
    // but one of the knights is blocking chess.
    // Then the pgn annotation won't specify, which knight will be moving to the one position,
    // as there is only one legal way to do so.
    // The PGN Reader however, reads it like that that the one knight blocking check
    // will jump to another square, which is actually invalid.
    // Look at the following game with this PGN:
    //
    //[Event "?"]
    //[Site "?"]
    //[Date "????.??.??"]
    //[Round "?"]
    //[White "?"]
    //[Black "?"]
    //[Result "*"]
    //
    //    1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nxe4 4. Nf3 Nf6 5. Ke2 Qe7+ 6. Kd3 Qc5 7. Nc3 d6 8.
    //    Nd5 Nfd7 9. Ne3 a6 10. Nf5 a5 11. Ke4 a4 12. Kf4 a3 13. Kg3 axb2 14. Bxb2 Ra7
    //    15. d4 Ra8 16. d5 Ra7 17. Bxg7 Bxg7 18. a4 Bf8 19. Rc1 Qc3 20. Nd4 Qa3 21. Qd3
    //    Qxa4 22. Ne5 Qa3 23. Nef3 Ra8 24. Qb5 *
    //
    // Before move 20. the position is the following
    // (in FEN: 1nb1kb1r/rppn1p1p/3p4/3P1N2/P7/2q2NK1/2P2PPP/2RQ1B1R w k - 3 20).
    // And then the move Ne5 is played.
    // Officially the position should now be the following:
    // (1nb1kb1r/rppn1p1p/3p4/3P4/P2N4/2q2NK1/2P2PPP/2RQ1B1R w k - 3 20).
    // But the PGN Reader reads it like that:
    // 1nb1kb1r/rppn1p1p/3p4/3P1N2/P2N4/2q3K1/2P2PPP/2RQ1B1R b k - 4 20.
    // This is invalid.
    // And the entire game afterwards does not make any sense anymore.
    //

}
