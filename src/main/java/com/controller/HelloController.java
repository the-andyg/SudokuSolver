package com.controller;

import com.model.Grid;
import com.model.SudokuSolver;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class HelloController {
    @FXML
    public Label output;
    private SudokuSolver sudokuSolver;
    @FXML
    public Button setButton;
    @FXML
    public GridPane gridPane;
    public Grid grid;

    private int size;

    @FXML
    public void nextNumber() {
        sudokuSolver.nextNumber();
        output.setText(sudokuSolver.getOutputText());
        updateGrid();
    }

    @FXML
    public void setGrid() {
        int[][] sudokuExample = {
                {3, 9, 0, 4, 0, 0, 0, 7, 0},
                {0, 0, 0, 6, 0, 0, 9, 1, 0},
                {4, 0, 0, 0, 0, 0, 0, 0, 5},
                {0, 8, 0, 0, 0, 0, 0, 0, 3},
                {0, 0, 0, 1, 0, 0, 0, 4, 0},
                {9, 2, 0, 0, 0, 0, 0, 0, 6},
                {7, 0, 0, 0, 0, 0, 0, 3, 9},
                {1, 0, 0, 9, 3, 8, 0, 0, 0},
                {0, 3, 0, 0, 4, 0, 0, 2, 0}
        };
        sudokuExample = getGrid();
        if (sudokuExample == null) {
            output.setText("Die Eingabe war nicht korrekt.");
            return;
        }
        grid = new Grid(sudokuExample, size);
        sudokuSolver = new SudokuSolver(grid);
        setButton.setDisable(true);
        updateGrid();
    }

    @FXML
    public void generateSudokuLayout9x9() {
        size = 9;
        generateSudokuLayout();
    }

    @FXML
    public void generateSudokuLayout16x16() {
        size = 16;
        generateSudokuLayout();
    }

    private void generateSudokuLayout() {
        gridPane.getChildren().clear();
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                TextField textField = new TextField();
                gridPane.add(textField, i, k);
            }
        }
    }

    private int[][] getGrid() {
        int[][] result = new int[size][size];
        int row = 0;
        int column = 0;
        for (Node node : gridPane.getChildren()) {
            if (column % size == 0 && column != 0) {
                row++;
            }
            if (node instanceof TextField textField) {
                String number = textField.getText();
                String regex = "[1-9]";
                if (number.matches(regex)) {
                    result[row][column % size] = Integer.parseInt(number);
                } else if (number.isEmpty()) {
                    result[row][column % size] = 0;
                } else {
                    return null;
                }
            }
            column++;
        }
        return result;
    }

    private void updateGrid() {
        boolean[][] newFields = sudokuSolver.getNewFields();
        boolean[][] removedFields = sudokuSolver.getRemovedFieldsFields();
        int row = 0;
        int column = 0;
        for (Node node : gridPane.getChildren()) {
            if (column % size == 0 && column != 0) {
                row++;
            }
            if (node instanceof TextField textField) {
                textField.setDisable(true);
                int number = grid.getGrid()[row][column % size];
                if (number != 0) {
                    textField.setText(Integer.toString(grid.getGrid()[row][column % size]));
                    if (newFields[row][column % size]) {
                        textField.setStyle("-fx-background-color: green;");
                    } else if (removedFields[row][column % size]) {
                        textField.setStyle("-fx-background-color: red;");
                    } else {
                        textField.setStyle("-fx-background-color: white;");
                    }
                } else {
                    textField.setText("");
                }
            }
            column++;
        }
    }


}