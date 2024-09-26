import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Select_mole {
    int boardWidth = 600;
    int boardHeight = 700; // Increased for the restart button

    JFrame frame = new JFrame("Mario: Select A Mole");
    JLabel textLabel = new JLabel();
    JLabel timerLabel = new JLabel(); // Timer label for countdown
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel(); 
    JPanel bottomPanel = new JPanel(); // For the restart button

    JButton[] board = new JButton[9];
    ImageIcon moleIcon;
    ImageIcon plantIcon;

    JButton currMoleTile;
    ArrayList<JButton> plantTiles = new ArrayList<>(); // ArrayList for multiple plants

    Random random = new Random();
    Timer setMoleTimer;
    Timer setPlantTimer;
    Timer gameTimer; // Timer for the countdown
    int score = 0;
    int highScore = 0; // Variable to store the high score
    int timeLeft = 60; // Countdown time in seconds
    int moleDelay = 600; // Starting mole delay (in milliseconds)
    int plantDelay = 700; // Starting plant delay (in milliseconds)

    Select_mole() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: " + score + " | High Score: " + highScore);
        textLabel.setOpaque(true);

        timerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setText("Time Left: " + timeLeft + "s");

        textPanel.setLayout(new GridLayout(2, 1)); // Add grid layout for score and timer
        textPanel.add(textLabel);
        textPanel.add(timerLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        frame.add(boardPanel, BorderLayout.CENTER);

        // Icons
        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);

            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currMoleTile) {
                        score += 10;
                        updateScore();
                    } else if (plantTiles.contains(tile)) {
                        endGame();
                    }
                }
            });
        }

        // Restart Button
        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 30));
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(restartButton, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Mole Timer (Faster and randomize as difficulty)
        setMoleTimer = new Timer(moleDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currMoleTile != null) {
                    currMoleTile.setIcon(null);
                    currMoleTile = null;
                }

                int num = random.nextInt(9);
                JButton tile = board[num];

                if (plantTiles.contains(tile)) return;

                currMoleTile = tile;
                currMoleTile.setIcon(moleIcon);

                // Decrease mole delay as score increases
                int newMoleDelay = Math.max(100, moleDelay - score / 5);
                setMoleTimer.setDelay(newMoleDelay);
            }
        });

        // Plant Timer (Increased plant count as score rises)
        setPlantTimer = new Timer(plantDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JButton plantTile : plantTiles) {
                    plantTile.setIcon(null);
                }
                plantTiles.clear();

                int numOfPlants = random.nextInt(3) + 1 + (score / 50); // More plants with higher score
                while (plantTiles.size() < numOfPlants) {
                    int num = random.nextInt(9);
                    JButton tile = board[num];

                    if (tile == currMoleTile || plantTiles.contains(tile)) continue;

                    plantTiles.add(tile);
                    tile.setIcon(plantIcon);
                }

                // Decrease plant delay as score increases
                int newPlantDelay = Math.max(300, plantDelay - score / 5);
                setPlantTimer.setDelay(newPlantDelay);
            }
        });

        // Game Timer (Countdown)
        gameTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time Left: " + timeLeft + "s");
                if (timeLeft <= 0) {
                    endGame();
                }
            }
        });

        setMoleTimer.start();
        setPlantTimer.start();
        gameTimer.start();
        frame.setVisible(true);
    }

    // Update score and high score
    private void updateScore() {
        if (score > highScore) {
            highScore = score;
        }
        textLabel.setText("Score: " + score + " | High Score: " + highScore);
    }

    // End the game
    private void endGame() {
        textLabel.setText("Game Over! Final Score: " + score + " | High Score: " + highScore);
        setMoleTimer.stop();
        setPlantTimer.stop();
        gameTimer.stop();
        disableBoard();
    }

    // Disable board after game over
    private void disableBoard() {
        for (int i = 0; i < 9; i++) {
            board[i].setEnabled(false);
        }
    }

    // Restart the game
    private void restartGame() {
        score = 0;
        timeLeft = 60; // Reset timer
        textLabel.setText("Score: " + score + " | High Score: " + highScore);
        timerLabel.setText("Time Left: " + timeLeft + "s");
        for (int i = 0; i < 9; i++) {
            board[i].setEnabled(true);
            board[i].setIcon(null);
        }
        plantTiles.clear();
        setMoleTimer.start();
        setPlantTimer.start();
        gameTimer.start();
    }

   
}
