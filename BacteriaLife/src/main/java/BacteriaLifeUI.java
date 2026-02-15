import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class BacteriaLifeUI {
    // Constants
    BacteriaLifeLogic LOGIC; // Quitamos private
    private static final int BACTERIA_SIZE = 10;
    private static final Color BG_COLOR = new Color(141, 69, 220);
    private static final int DIMENSION = 30;

    // Quitamos private para testear
    JPanel genPanel;
    int[][] bacteriaGen;
    JLabel roundLabel; // Esta variable debe inicializarse antes de usarse
    Timer timer;

    // Circle class - Quitamos private
    static class Circle extends JButton {
        private Color color;
        private final int diameter;

        public Circle(Color color) {
            this.color = color;
            this.diameter = BACTERIA_SIZE;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setEnabled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(color);
            g.fillOval(0, 0, diameter, diameter);
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(diameter, diameter);
        }

        public void setCircleColor(Color c) {
            this.color = c;
            repaint();
        }

        public Color getColor() {
            return color;
        }
    }

    // Generate a generation
    private JPanel generateGen() {
        JPanel gen = new JPanel();
        gen.setLayout(new GridLayout(DIMENSION, DIMENSION, 3, 3));
        gen.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        gen.setBackground(BG_COLOR);
        // Row
        for (int i = 0; i < DIMENSION; i++) {
            // Column
            for (int j = 0; j < DIMENSION; j++) {
                Color color = Color.WHITE;
                if (bacteriaGen[i][j] == 1) {
                    color = Color.BLACK;
                }
                Circle bacteria = new Circle(color);
                gen.add(bacteria);
            }
        }
        return gen;
    }

    // Refresh the grid after generating a new round
    private void refreshGenPanel() {
        genPanel.removeAll();
        genPanel.setLayout(new GridLayout(DIMENSION, DIMENSION, 3, 3));

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                Color color = (bacteriaGen[i][j] == 1) ? Color.BLACK : Color.WHITE;
                genPanel.add(new Circle(color));
            }
        }

        genPanel.revalidate(); // To avoid bugs
        genPanel.repaint();
    }

    // A bottom panel with a round label and a start button
    private JPanel bottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_COLOR);

        // IMPORTANTE: Aquí ya no hacemos 'new JLabel', usamos la que creamos en el constructor
        // para asegurarnos de que la variable de clase 'this.roundLabel' no sea null.
        roundLabel.setText("Round: " + LOGIC.getRound());

        JButton startButton = getStartButton();

        startButton.setPreferredSize(new Dimension(70, 50));
        startButton.setBackground(Color.WHITE);
        startButton.setContentAreaFilled(true);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);

        bottomPanel.add(roundLabel, BorderLayout.WEST);   // Left side
        bottomPanel.add(startButton, BorderLayout.EAST);  // Right side

        return bottomPanel;
    }

    // Start button
    private JButton getStartButton() {
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> startEvolution());
        return startButton;
    }

    // Método extraído para poder testear la lógica de evolución sin esperar al Timer
    public void performEvolutionStep() {
        int[][] oldGen = deepCopy(bacteriaGen);
        int[][] newGen = LOGIC.generateNewGen(oldGen);

        if (BacteriaLifeLogic.checkStableGen(oldGen, newGen)) {
            if (timer != null) timer.stop();
            return;
        }

        bacteriaGen = newGen;
        refreshGenPanel();

        if (roundLabel != null) {
            roundLabel.setText("Round: " + LOGIC.getRound());
        }
    }

    // Método para iniciar el timer
    public void startEvolution() {
        if (timer == null) {
            timer = new Timer(100, ev -> performEvolutionStep());
            timer.start();
        }
    }

    // To copy the gen
    private int[][] deepCopy(int[][] bacteriaGen) {
        if (bacteriaGen == null) return null;
        int[][] copy = new int[bacteriaGen.length][];
        for (int i = 0; i < bacteriaGen.length; i++) {
            copy[i] = Arrays.copyOf(bacteriaGen[i], bacteriaGen[i].length);
        }
        return copy;
    }

    // Getters para test
    public JLabel getRoundLabel() {
        return roundLabel;
    }

    public int[][] getBacteriaGen() {
        return bacteriaGen;
    }

    public JPanel getGenPanel() {
        return genPanel;
    }

    // Main
    public BacteriaLifeUI(BacteriaLifeLogic logic) {
        this.LOGIC = logic;
        this.bacteriaGen = LOGIC.generateInitialGen();

        this.roundLabel = new JLabel();

        // Main frame
        JFrame mainFrame = new JFrame("BacteriaLife");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Add the gen
        this.genPanel = generateGen();
        mainFrame.add(genPanel, BorderLayout.CENTER);

        // Add the bottom label
        mainFrame.add(bottomPanel(), BorderLayout.SOUTH);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}