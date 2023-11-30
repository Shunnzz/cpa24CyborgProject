package com.example.monkeyfx;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

public class ButtonGame extends Application {

    private ArrayList<Button> buttons;
    private ArrayList<Button> sequence;
    private int currentIndex;
    private int gridSize;
    private Stage primaryStage;
    private long startTime; // To track the start time
    private Queue<Button> sequenceQueue;
    private Stack<Long> attemptTime = new Stack<>();
    private BinaryTree leaderboard = new BinaryTree(); // Binary tree to store leaderboard data

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Button Game");

        // Title Screen
        VBox titleScreen = createTitleScreen();

        Scene titleScene = new Scene(titleScreen, 400, 200);
        primaryStage.setScene(titleScene);
        primaryStage.show();
    }

    private VBox createTitleScreen() {
        VBox titleScreen = new VBox(20);
        titleScreen.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Button Game");
        titleLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #FFFFFF;");

        Label descLabel = new Label("Welcome to Team Cyborg's Monkey Game !");
        descLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #FFFFFF;");

        Button startButton = new Button("Play Game");
        startButton.setOnAction(e -> startGame());

        titleScreen.getChildren().addAll(titleLabel, descLabel, startButton);
        titleScreen.setStyle("-fx-background-color: #333333;"); // Dark background

        return titleScreen;
    }

    private void startGame() {
        // Game Screen
        GridPane gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setHgap(10);
        gameGrid.setVgap(10);
        gameGrid.setStyle("-fx-background-color: #333333;"); // Dark background

        buttons = new ArrayList<>();
        sequence = new ArrayList<>();
        currentIndex = 0;
        gridSize = 1; // Start with a 1x1 grid

        initializeButtons(gameGrid);

        // Initialize the queue with buttons
        sequenceQueue = new LinkedList<>(buttons);
        // Shuffle and display the initial sequence
        generateSequence();
        displaySequence();

        startTime = System.currentTimeMillis(); // Record the start time

        Scene gameScene = new Scene(gameGrid);
        primaryStage.setScene(gameScene);

        // Maximize the window
        primaryStage.setMaximized(true);

        // Switch to the game scene after a delay (you can adjust the delay as needed)
        Platform.runLater(() -> {
            try {
                Thread.sleep(3000); // 3 seconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            primaryStage.show(); // Show the primaryStage after the delay
        });
    }

    private void initializeButtons(GridPane grid) {
        // Create grid of buttons
        for (int i = 1; i <= gridSize; i++) {
            for (int j = 1; j <= gridSize; j++) {
                Button button = new Button();
                button.setMinSize(100, 100);
                button.setStyle("-fx-background-color: #AA00AA; -fx-text-fill: #FFFFFF;"); // Dark Purple color, White text
                button.setOnAction(e -> handleButtonClick(button));
                buttons.add(button);
                grid.add(button, j, i);
            }
        }
    }

    private void generateSequence() {
        buttons.forEach(button -> button.setDisable(true));
        sequence.clear();
        int counter = 1;
        for (Button button : buttons) {
            button.setText("" + counter);
            sequence.add(button);
            counter++;
        }
        Collections.shuffle(sequence);
        for (int i = 0; i < sequence.size(); i++) {
            sequence.get(i).setText("" + (i + 1));
        }

        // Clear and enqueue buttons to the sequenceQueue
        sequenceQueue.clear();
        sequenceQueue.addAll(sequence);
    }

    private void displaySequence() {
        buttons.forEach(button -> button.setDisable(true));

        new Thread(() -> {
            while (!sequenceQueue.isEmpty()) {
                Button button = sequenceQueue.poll();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    button.setStyle("-fx-background-color: #CC00CC;");
                    button.setText(button.getText());
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    button.setStyle("-fx-background-color: #AA00AA; -fx-text-fill: #FFFFFF;");
                    button.setText("X");
                });
            }

            Platform.runLater(() -> buttons.forEach(button -> button.setDisable(false)));
        }).start();
    }

    private void handleButtonClick(Button button) {
        if (button == sequence.get(currentIndex)) {
            currentIndex++;
            button.setDisable(true); // Disable the button temporarily
            button.setStyle("-fx-background-color: #808080; -fx-text-fill: #FFFFFF;"); // Gray out the button
            if (currentIndex == sequence.size()) {
                currentIndex = 0;
                gridSize++;
                if (gridSize <= 5) {
                    buttons.clear();
                    initializeButtons((GridPane) button.getParent());
                    generateSequence();
                    displaySequence();
                } else {
                    // User completed the game
                    long currentTime = System.currentTimeMillis();
                    leaderboard.insert((int) (currentTime - startTime));
                    showGameOverDialog("Congratulations! You have completed a " + (gridSize - 1) + "x" + (gridSize - 1) + " grid.");
                }
            }
        } else {
            // Game over
            long currentTime = System.currentTimeMillis();
            leaderboard.insert((int) (currentTime - startTime));
            showGameOverDialog("Game Over! You completed a " + (gridSize - 1) + "x" + (gridSize - 1) + " grid.");
        }
    }

    private void showGameOverDialog(String message) {
        Platform.runLater(() -> {
            long currentTime = System.currentTimeMillis();
            attemptTime.push(currentTime);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(message);
            alert.setContentText("Your score: " + (gridSize - 1) + "x" + (gridSize - 1) +
                    "\nTime taken: " + (attemptTime.pop() - startTime) / 1000 + " seconds" +
                    "\nLeaderboard:\n" + leaderboard);

            ButtonType playAgainButton = new ButtonType("Play Again");
            ButtonType exitButton = new ButtonType("Exit");
            alert.getButtonTypes().setAll(playAgainButton, exitButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == playAgainButton) {
                primaryStage.setMaximized(false); // Restore normal size
                startGame(); // Start the game again
            } else if (result.isPresent() && result.get() == exitButton) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}