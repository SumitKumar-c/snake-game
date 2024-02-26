import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int SCALE = 20;
    private static final int DELAY = 75;

    private boolean running = false;
    private boolean gameOver = false;
    private int score;
    private int snakeSize;
    private int[] xPos;
    private int[] yPos;
    private int foodX;
    private int foodY;
    private int dx;
    private int dy;

    private Timer timer;
    private JFrame jFrame;
    private JPanel jPanel;

    public SnakeGame() {
        jFrame = new JFrame("Snake Game");
        jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };
        jPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        jPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP && dy != 1) {
                    dx = 0;
                    dy = -1;
                }
                if (key == KeyEvent.VK_DOWN && dy != -1) {
                    dx = 0;
                    dy = 1;
                }
                if (key == KeyEvent.VK_LEFT && dx != 1) {
                    dx = -1;
                    dy = 0;
                }
                if (key == KeyEvent.VK_RIGHT && dx != -1) {
                    dx = 1;
                    dy = 0;
                }
            }
        });

        startGame();
    }

    private void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move();
                checkCollision();
                checkFood();
                repaint();
            }
        });
        timer.start();
    }

    private void move() {
        for (int i = snakeSize - 1; i > 0; i--) {
            xPos[i] = xPos[i - 1];
            yPos[i] = yPos[i - 1];
        }
        xPos[0] += dx;
        yPos[0] += dy;
    }

    private void checkCollision() {
        for (int i = 1; i < snakeSize; i++) {
            if (xPos[0] == xPos[i] && yPos[0] == yPos[i]) {
                gameOver = true;
            }
        }
        if (xPos[0] < 0 || xPos[0] >= WIDTH / SCALE || yPos[0] < 0 || yPos[0] >= HEIGHT / SCALE) {
            gameOver = true;
        }
        if (gameOver) {
            timer.stop();
        }
    }

    private void checkFood() {
        if (xPos[0] == foodX && yPos[0] == foodY) {
            score++;
            snakeSize++;
            newFood();
        }
    }

    private void newFood() {
        foodX = new Random().nextInt((int) (WIDTH / SCALE))
