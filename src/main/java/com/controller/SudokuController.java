package com.controller;

import com.Data.ExampleSudokus;
import com.Data.OutputMessages;
import com.model.Grid;
import com.model.SudokuSolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SudokuController implements Initializable {
    private final int SIZE_NORMAL = 9;
    private final int SIZE_HEX = 16;
    @FXML
    public Label outputDecimal;
    @FXML
    public Label outputHex;
    @FXML
    public Button setButtonHex;
    @FXML
    public Button setButtonDecimal;
    @FXML
    public Button nextNumberHex;
    @FXML
    public Button nextNumberDecimal;
    @FXML
    public GridPane gridHex;
    @FXML
    public GridPane gridNormal;
    @FXML
    public Button setExample;
    @FXML
    public ComboBox<String> comboBoxDecimal;
    private SudokuSolver sudokuSolverDecimal;
    private SudokuSolver sudokuSolverHex;
    public Grid gridSudokuDecimal;
    public Grid gridSudokuHex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGridHex();
        initGridNormal();
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add(OutputMessages.CHOOSE);
        options.add(OutputMessages.EASY);
        options.add(OutputMessages.MEDIUM);
        options.add(OutputMessages.HARD);
        comboBoxDecimal.getItems().addAll(options);
        comboBoxDecimal.setValue(OutputMessages.CHOOSE);
    }

    @FXML
    public void initGridNormal() {
        generateSudokuLayout9x9();
        nextNumberDecimal.setDisable(true);
        outputDecimal.setText(OutputMessages.INIT_TEXT);
    }

    @FXML
    public void initGridHex() {
        generateSudokuLayout16x16();
        nextNumberHex.setDisable(true);
        outputHex.setText(OutputMessages.INIT_TEXT);
    }

    @FXML
    public void nextNumberNormal() {
        sudokuSolverDecimal.nextNumber();
        outputDecimal.setText(sudokuSolverDecimal.getOutputText());
        updateGrid(gridNormal, sudokuSolverDecimal, gridSudokuDecimal, SIZE_NORMAL);
    }

    @FXML
    public void nextNumberHex() {
        sudokuSolverHex.nextNumber();
        outputHex.setText(sudokuSolverHex.getOutputText());
        updateGrid(gridHex, sudokuSolverHex, gridSudokuHex, SIZE_HEX);
    }

    @FXML
    public void setSudokuExampleDecimal() {
        switch (comboBoxDecimal.getValue()) {
            case OutputMessages.EASY ->
                    updateGrid(gridNormal, sudokuSolverDecimal, new Grid(ExampleSudokus.simpleSudokuDecimal(), SIZE_NORMAL), SIZE_NORMAL);
            case OutputMessages.MEDIUM ->
                    updateGrid(gridNormal, sudokuSolverDecimal, new Grid(ExampleSudokus.mediumSudokuDecimal(), SIZE_NORMAL), SIZE_NORMAL);
            case OutputMessages.HARD ->
                    updateGrid(gridNormal, sudokuSolverDecimal, new Grid(ExampleSudokus.hardSudokuDecimal(), SIZE_NORMAL), SIZE_NORMAL);
        }
    }

    @FXML
    public void setGridNormal() {
        int[][] sudoku = getGrid(gridNormal, SIZE_NORMAL);
        if (sudoku == null) {
            outputDecimal.setText(OutputMessages.FAIL_TEXT);
            return;
        }
        gridSudokuDecimal = new Grid(sudoku, SIZE_NORMAL);
        if (!gridSudokuDecimal.isSolveAble()) {
            outputDecimal.setText(OutputMessages.NOT_SOLVABLE);
            return;
        }
        sudokuSolverDecimal = new SudokuSolver(gridSudokuDecimal);
        setButtonDecimal.setDisable(true);
        updateGrid(gridNormal, sudokuSolverDecimal, gridSudokuDecimal, SIZE_NORMAL);
        nextNumberDecimal.setDisable(false);
        outputDecimal.setText(OutputMessages.SET_TEXT);
    }

    @FXML
    public void setGridHex() {
        int[][] sudoku = getGrid(gridHex, SIZE_HEX);
        if (sudoku == null) {
            outputHex.setText(OutputMessages.FAIL_TEXT);
            return;
        }
        gridSudokuHex = new Grid(sudoku, SIZE_HEX);
        if (!gridSudokuHex.isSolveAble()) {
            outputHex.setText(OutputMessages.NOT_SOLVABLE);
            return;
        }
        sudokuSolverHex = new SudokuSolver(gridSudokuHex);
        setButtonHex.setDisable(true);
        updateGrid(gridHex, sudokuSolverHex, gridSudokuHex, SIZE_HEX);
        nextNumberHex.setDisable(false);
        outputHex.setText(OutputMessages.SET_TEXT);
    }

    @FXML
    public void generateSudokuLayout9x9() {
        gridNormal.getChildren().clear();
        generateSudokuLayout(gridNormal, SIZE_NORMAL);
        setButtonDecimal.setDisable(false);
        nextNumberDecimal.setDisable(true);
        outputDecimal.setText(OutputMessages.INIT_TEXT);
    }

    @FXML
    public void generateSudokuLayout16x16() {
        gridHex.getChildren().clear();
        generateSudokuLayout(gridHex, SIZE_HEX);
        setButtonHex.setDisable(false);
        nextNumberHex.setDisable(true);
        outputHex.setText(OutputMessages.INIT_TEXT);
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

    private void updateGrid(GridPane grid, SudokuSolver sudokuSolver, Grid gridSudoku, int size) {
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
                        textField.setStyle(textField.getStyle() + "-fx-font-weight: bold; -fx-font-size: 22px;");
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