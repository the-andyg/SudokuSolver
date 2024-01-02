package com.controller;

import com.model.Grid;
import com.model.SudokuSolver;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SudokuController implements Initializable {
    private final int SIZE_NORMAL = 9;
    private final int SIZE_HEX = 16;
    private final String INIT_TEXT = "Gebe das gewünschte Sudoku ein und bestätige es.";
    private final String SET_TEXT = "Klicke auf \"nächste Zahl\" um den Algorithmus zu starten.";
    private final String FAIL_TEXT = "Die Eingabe war nicht korrekt.";
    private final String NOT_SOLVE_ABLE = "Das eingegebene Sudoku ist nicht lösbar";
    @FXML
    public Label outputNormal;
    @FXML
    public Label outputHex;
    @FXML
    public Button setButtonHex;
    @FXML
    public Button setButtonNormal;
    @FXML
    public Button nextNumberHex;
    @FXML
    public Button nextNumberNormal;
    @FXML
    public GridPane gridHex;
    @FXML
    public GridPane gridNormal;
    private SudokuSolver sudokuSolverNormal;
    private SudokuSolver sudokuSolverHex;
    public Grid gridSudokuNormal;
    public Grid gridSudokuHex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGridHex();
        initGridNormal();
    }

    @FXML
    public void initGridNormal() {
        generateSudokuLayout9x9();
        nextNumberNormal.setDisable(true);
        outputNormal.setText(INIT_TEXT);
    }

    @FXML
    public void initGridHex() {
        generateSudokuLayout16x16();
        nextNumberHex.setDisable(true);
        outputHex.setText(INIT_TEXT);
    }

    @FXML
    public void nextNumberNormal() {
        sudokuSolverNormal.nextNumber();
        outputNormal.setText(sudokuSolverNormal.getOutputText());
        updateGrid(gridNormal, sudokuSolverNormal, gridSudokuNormal, SIZE_NORMAL, false);
    }

    @FXML
    public void nextNumberHex() {
        sudokuSolverHex.nextNumber();
        outputHex.setText(sudokuSolverHex.getOutputText());
        updateGrid(gridHex, sudokuSolverHex, gridSudokuHex, SIZE_HEX, false);
    }

    @FXML
    public void setGridNormal() {
        int[][] sudoku = getGrid(gridNormal, SIZE_NORMAL);
        if (sudoku == null) {
            outputNormal.setText(FAIL_TEXT);
            return;
        }
        gridSudokuNormal = new Grid(sudoku, SIZE_NORMAL);
        if (!gridSudokuNormal.isSolveAble()) {
            outputNormal.setText(NOT_SOLVE_ABLE);
            return;
        }
        sudokuSolverNormal = new SudokuSolver(gridSudokuNormal);
        setButtonNormal.setDisable(true);
        updateGrid(gridNormal, sudokuSolverNormal, gridSudokuNormal, SIZE_NORMAL, true);
        nextNumberNormal.setDisable(false);
        outputNormal.setText(SET_TEXT);
    }

    @FXML
    public void setGridHex() {
        int[][] sudoku = getGrid(gridHex, SIZE_HEX);
        if (sudoku == null) {
            outputHex.setText(FAIL_TEXT);
            return;
        }
        gridSudokuHex = new Grid(sudoku, SIZE_HEX);
        if (!gridSudokuHex.isSolveAble()) {
            outputHex.setText(NOT_SOLVE_ABLE);
            return;
        }
        sudokuSolverHex = new SudokuSolver(gridSudokuHex);
        setButtonHex.setDisable(true);
        updateGrid(gridHex, sudokuSolverHex, gridSudokuHex, SIZE_HEX, true);
        nextNumberHex.setDisable(false);
        outputHex.setText(SET_TEXT);
    }

    @FXML
    public void generateSudokuLayout9x9() {
        gridNormal.getChildren().clear();
        generateSudokuLayout(gridNormal, SIZE_NORMAL);
        setButtonNormal.setDisable(false);
        nextNumberNormal.setDisable(true);
        outputNormal.setText(INIT_TEXT);
    }

    @FXML
    public void generateSudokuLayout16x16() {
        gridHex.getChildren().clear();
        generateSudokuLayout(gridHex, SIZE_HEX);
        setButtonHex.setDisable(false);
        nextNumberHex.setDisable(true);
        outputHex.setText(INIT_TEXT);
    }

    private void generateSudokuLayout(GridPane grid, int size) {
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                TextField textField = new TextField();
                grid.add(textField, i, k);
                setTextFieldColor(textField, i, k, size);
            }
        }
    }

    private int[][] getGrid(GridPane grid, int size) {
        int[][] result = new int[size][size];
        int row = 0;
        int column = 0;
        String regex;
        if (size == SIZE_NORMAL) {
            regex = "[1-9]";
        } else {
            regex = "^(1[0-6]|[1-9])$";
            ;
        }
        for (Node node : grid.getChildren()) {
            if (column % size == 0 && column != 0) {
                row++;
            }
            if (node instanceof TextField textField) {
                String number = textField.getText();
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

    private void updateGrid(GridPane grid, SudokuSolver sudokuSolver, Grid gridSudoku, int size, boolean init) {
        boolean[][] newFields = sudokuSolver.getNewFields();
        boolean[][] removedFields = sudokuSolver.getRemovedFieldsFields();
        boolean[][] startingSudoku = gridSudoku.getStartingSudoku();
        int row = 0;
        int column = 0;
        for (Node node : grid.getChildren()) {
            if (column % size == 0 && column != 0) {
                row++;
            }
            if (node instanceof TextField textField) {
                textField.setDisable(true);
                int number = gridSudoku.getGrid()[row][column % size];
                setTextFieldColor(textField, row, column % size, size);
                if (number != 0) {
                    if (startingSudoku[row][column % size]) {
                        textField.setStyle(textField.getStyle() + "-fx-font-weight: bold");
                    }
                    textField.setText(Integer.toString(gridSudoku.getGrid()[row][column % size]));
                    if (newFields[row][column % size]) {
                        textField.setStyle("-fx-background-color: green;");
                    } else if (removedFields[row][column % size]) {
                        textField.setStyle("-fx-background-color: red;");
                    }
                } else {
                    textField.setText("");
                }
            }
            column++;
        }
    }

    private void setTextFieldColor(TextField t, int row, int column, int size) {
        int blockSize = (int) Math.sqrt(size);
        int blockRowIndex = row / blockSize;
        int blockColIndex = (column % size) / blockSize;
        int blockIndex = blockRowIndex + blockColIndex;
        if (blockIndex % 2 == 0) {
            t.setStyle("-fx-background-color: #6B8FA4;");
        } else {
            t.setStyle("-fx-background-color: white;");
        }
    }
}