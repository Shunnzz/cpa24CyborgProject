package com.example.lbycpa2_monkeygame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonkeySequenceGame extends Application {
    private int gridSize = 1; // Starting with a 1x1 grid
    private static final int DISPLAY_TIME_SECONDS = 5;

    private List<Integer> sequence;
    private int currentIndex = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Monkey Sequence Game");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        sequence = generateRandomSequence();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Button button = createButton(i * gridSize + j + 1);
                grid.add(button, j, i);
            }
        }

        Scene scene = new Scene(grid, 300, 300);
        primaryStage.setScene(scene);

        primaryStage.show();

        displayAnswerSequence(); // Display the answer sequence before allowing player interaction
        displaySequence();
    }

    private void displayAnswerSequence() {
        GridPane displayGrid = new GridPane();
        displayGrid.setAlignment(Pos.CENTER);
        displayGrid.setHgap(10);
        displayGrid.setVgap(10);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                String label = Integer.toString(sequence.get(i * gridSize + j));
                Button button = new Button(label);
                button.setMinSize(80, 80);
                button.setDisable(true); // Disable the button to make it non-interactive
                displayGrid.add(button, j, i);
            }
        }

        Scene displayScene = new Scene(displayGrid, 300, 300);
        Stage displayStage = new Stage();
        displayStage.setScene(displayScene);
        displayStage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(DISPLAY_TIME_SECONDS));
        pause.setOnFinished(e -> {
            displayStage.close();
            allowPlayerInteraction();
        });
        pause.play();
    }

    private void allowPlayerInteraction() {
        // Now that the answer sequence has been displayed, allow player interaction
        // You can add any additional logic here if needed
    }

    private Button createButton(int number) {
        Button button = new Button(Integer.toString(number));
        button.setMinSize(80, 80);
        button.setOnAction(e -> handleButtonClick(number));

        return button;
    }

    private void handleButtonClick(int buttonNumber) {
        if (buttonNumber == sequence.get(currentIndex)) {
            currentIndex++;

            if (currentIndex == sequence.size()) {
                System.out.println("Congratulations! You completed the sequence!");
                currentIndex = 0;
                gridSize++; // Increase the grid size for the next round
                displaySequence(); // Display the new sequence for the next round
            }
        } else {
            System.out.println("Game Over! Wrong sequence. Try again.");
            currentIndex = 0;
            gridSize = 1; // Reset the grid size
            sequence = generateRandomSequence();
            displaySequence(); // Restart with a new random sequence
        }
    }

    private List<Integer> generateRandomSequence() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= gridSize * gridSize; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }

    private void displaySequence() {
        GridPane displayGrid = new GridPane();
        displayGrid.setAlignment(Pos.CENTER);
        displayGrid.setHgap(10);
        displayGrid.setVgap(10);

        List<Integer> displaySequence = generateRandomSequence(); // Generate a new sequence for display

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                String label = Integer.toString(displaySequence.get(i * gridSize + j));
                Button button = new Button(label);
                button.setMinSize(80, 80);
                button.setDisable(true); // Disable the button to make it non-interactive
                displayGrid.add(button, j, i);
            }
        }

        Scene displayScene = new Scene(displayGrid, 300, 300);
        Stage displayStage = new Stage();
        displayStage.setScene(displayScene);
        displayStage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(DISPLAY_TIME_SECONDS));
        pause.setOnFinished(e -> {
            displayStage.close();
            allowPlayerInteraction();
        });
        pause.play();
    }
}
