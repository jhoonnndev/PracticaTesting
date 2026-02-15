import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasterMindUITest {

    @Mock
    MasterMindLogic mockLogic;

    MasterMindUI ui;
    Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    String[] labels = {"R", "G", "B", "Y"};

    @BeforeEach
    void setUp() {
        ui = new MasterMindUI(colors, labels, 10, mockLogic) {
            @Override
            public void showMessage(String message) {
                System.out.println("Mensaje capturado en test: " + message);
            }
        };
    }

    @Test
    void testInitialization() {
        assertNotNull(ui);
        assertEquals(10, ui.guessRows.size());
        assertEquals(10, ui.pinRows.size());
        assertEquals(0, ui.currentRow);
    }

    @Test
    void testSelectColorUpdatesSelection() {
        ui.selectedColor = Color.RED;
        assertEquals(Color.RED, ui.selectedColor);
    }

    @Test
    void testClickSlotColorsIt() {
        ui.selectedColor = Color.GREEN;

        MasterMindUI.Circle slot = ui.guessRows.get(0)[0];

        slot.doClick();

        assertEquals(Color.GREEN, slot.getColor());
    }

    @Test
    void testClickSlotInWrongRowDoesNothing() {
        ui.selectedColor = Color.GREEN;

        ui.currentRow = 5;

        MasterMindUI.Circle slot = ui.guessRows.get(0)[0];
        slot.doClick();

        assertNotEquals(Color.GREEN, slot.getColor());
    }

    @Test
    void testCheckRoundWithIncompleteRow() {
        ui.checkRound();
        verify(mockLogic, never()).checkGuess(any());
        assertEquals(0, ui.currentRow);
    }

    @Test
    void testCheckRoundWithFullRow() {
        MasterMindLogic.Result mockResult = new MasterMindLogic.Result(1, 1);
        when(mockLogic.checkGuess(any())).thenReturn(mockResult);

        ui.selectedColor = Color.RED;
        for (MasterMindUI.Circle slot : ui.guessRows.get(0)) {
            slot.setCircleColor(Color.RED);
        }

        ui.checkRound();

        verify(mockLogic, times(1)).checkGuess(any());
        assertEquals(1, ui.currentRow);

        MasterMindUI.Circle[] pins = ui.pinRows.get(0);
        assertEquals(Color.BLACK, pins[0].getColor());
        assertEquals(Color.WHITE, pins[1].getColor());
    }

    @Test
    void testWinCondition() {
        MasterMindLogic.Result winningResult = new MasterMindLogic.Result(4, 0);
        when(mockLogic.checkGuess(any())).thenReturn(winningResult);

        ui.selectedColor = Color.BLUE;
        for (MasterMindUI.Circle slot : ui.guessRows.get(0)) {
            slot.setCircleColor(Color.BLUE);
        }

        ui.checkRound();

        MasterMindUI.Circle[] pins = ui.pinRows.get(0);
        assertEquals(Color.BLACK, pins[0].getColor());
        assertEquals(Color.BLACK, pins[3].getColor());
    }

    @Test
    void testLoseCondition() {
        when(mockLogic.checkGuess(any())).thenReturn(new MasterMindLogic.Result(0,0));
        when(mockLogic.showSecret()).thenReturn("ABCD");

        ui.currentRow = 9;

        for (MasterMindUI.Circle slot : ui.guessRows.get(9)) {
            slot.setCircleColor(Color.YELLOW);
        }

        ui.checkRound();

        verify(mockLogic, times(1)).showSecret();
    }
}