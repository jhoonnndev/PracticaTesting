import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class MasterMindLogicTest {

    // Colores de prueba
    private final Color[] PALETTE = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private final String[] LABELS = {"R", "G", "B", "Y"};

    @Test
    void testGenerateSecretLength() {
        MasterMindLogic logic = new MasterMindLogic(PALETTE, 4, LABELS);
        assertEquals(4, logic.SECRET.length);
    }

    @Test
    void testCheckGuessWinner() {
        MasterMindLogic logic = new MasterMindLogic(PALETTE, 4, LABELS);
        MasterMindLogic.Result result = logic.checkGuess(logic.SECRET);
        assertEquals(4, result.blacks, "Debería haber 4 negras (acierto total)");
        assertEquals(0, result.whites, "Debería haber 0 blancas");
    }

    @Test
    void testCheckGuessAllWhite() {
        MasterMindLogic logic = new MasterMindLogic(PALETTE, 4, LABELS);

        logic.SECRET[0] = Color.RED;
        logic.SECRET[1] = Color.RED;
        logic.SECRET[2] = Color.GREEN;
        logic.SECRET[3] = Color.GREEN;

        Color[] guess = {Color.GREEN, Color.GREEN, Color.RED, Color.RED};

        MasterMindLogic.Result result = logic.checkGuess(guess);

        assertEquals(0, result.blacks);
        assertEquals(4, result.whites);
    }

    @Test
    void testCheckGuessNoMatch() {
        MasterMindLogic logic = new MasterMindLogic(PALETTE, 4, LABELS);

        for(int i=0; i<4; i++) {
            logic.SECRET[i] = Color.RED;
        }

        Color[] guess = {Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE};

        MasterMindLogic.Result result = logic.checkGuess(guess);
        assertEquals(0, result.blacks);
        assertEquals(0, result.whites);
    }

    @Test
    void testShowSecret() {
        MasterMindLogic logic = new MasterMindLogic(PALETTE, 4, LABELS);

        logic.SECRET[0] = Color.RED;
        logic.SECRET[1] = Color.GREEN;
        logic.SECRET[2] = Color.BLUE;
        logic.SECRET[3] = Color.YELLOW;

        assertEquals("RGBY", logic.showSecret());
    }
}