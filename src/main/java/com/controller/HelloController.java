package com.controller;

import com.model.Grid;
import com.model.SudokuSolver;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    public Label output;
    private SudokuSolver sudokuSolver;
    @FXML
    public Button setButton;

    @FXML
    public void nextNumber() {
        sudokuSolver.nextNumber();
        output.setText(sudokuSolver.getOutputText());
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
        Grid grid = new Grid(sudokuExample, 9);
        sudokuSolver = new SudokuSolver(grid);
        setButton.setDisable(true);
    }
}