import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BacteriaLifeLogicTest {

    @Test
    void testInitialGeneration() {
        BacteriaLifeLogic logic = new BacteriaLifeLogic(5);
        int[][] gen = logic.generateInitialGen();
        assertEquals(5, gen.length);
        assertEquals(5, gen[0].length);
    }

    @Test
    void testRulesOfLife() {
        BacteriaLifeLogic logic = new BacteriaLifeLogic(3);
        /*
           Patrón oscilador simple (Blinker) vertical:
           0 1 0
           0 1 0
           0 1 0

           Siguiente generación (Horizontal):
           0 0 0
           1 1 1
           0 0 0
         */
        int[][] initial = {
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
        };

        int[][] nextGen = logic.generateNewGen(initial);

        assertEquals(1, nextGen[1][1], "Célula central debe sobrevivir");
        assertEquals(1, nextGen[1][0], "Célula izquierda debe nacer");
        assertEquals(0, nextGen[0][1], "Célula superior debe morir");
    }

    @Test
    void testCheckNeighbours() {
        int[][] grid = {
                {1, 1, 1},
                {1, 0, 0},
                {0, 0, 0}
        };
        assertEquals(4, BacteriaLifeLogic.checkNeighbours(grid, 1, 1));
    }


    @Test
    void testStableGenCheck() {
        int[][] gen1 = {{0,0}, {0,0}};
        int[][] gen2 = {{0,0}, {0,0}};
        assertTrue(BacteriaLifeLogic.checkStableGen(gen1, gen2));

        int[][] gen3 = {{0,0}, {0,1}};
        assertFalse(BacteriaLifeLogic.checkStableGen(gen1, gen3));
    }
}