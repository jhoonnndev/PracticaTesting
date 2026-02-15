import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BacteriaLifeUITest {

    @Mock
    BacteriaLifeLogic mockLogic;

    // CORRECCIÓN: Quitamos @InjectMocks.
    // Crearemos la UI manualmente dentro del test para evitar el NullPointerException.
    BacteriaLifeUI ui;

    @Test
    void testEvolutionStepUpdatesUI() {
        int[][] initialBoard = new int[30][30];
        when(mockLogic.generateInitialGen()).thenReturn(initialBoard);

        ui = new BacteriaLifeUI(mockLogic);

        int[][] nextBoard = new int[30][30];
        nextBoard[0][1] = 1;

        when(mockLogic.generateNewGen(any())).thenReturn(nextBoard);
        when(mockLogic.getRound()).thenReturn(1);

        ui.performEvolutionStep();

        verify(mockLogic, times(1)).generateNewGen(any());

        assertArrayEquals(nextBoard, ui.getBacteriaGen());

        assertEquals("Round: 1", ui.getRoundLabel().getText());


        JPanel genPanel = ui.getGenPanel();
        BacteriaLifeUI.Circle circleNewAlive = (BacteriaLifeUI.Circle) genPanel.getComponent(1);
        assertEquals(Color.BLACK, circleNewAlive.getColor(), "La célula viva debería ser negra");
    }
}