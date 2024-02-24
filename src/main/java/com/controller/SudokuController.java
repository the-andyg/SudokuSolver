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
    private final int SIZE_DECIMAL = 9;
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
    public GridPane gridPaneHex;
    @FXML
    public GridPane gridPaneDecimal;
    @FXML
    public ComboBox<String> comboBoxDecimal;
    @FXML
    public ComboBox<String> comboBoxHex;
    @FXML
    public Button solveDecimal;
    @FXML
    public Button solveHex;
    private SudokuSolver sudokuSolverDecimal;
    private SudokuSolver sudokuSolverHex;
    public Grid gridSudokuDecimal;
    public Grid gridSudokuHex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGridHex();
        initGridDecimal();
        initComboBoxes();
    }

    /**
     *
     */
    private void initGridDecimal() {
        generateSudokuLayoutDecimal();
        nextNumberDecimal.setDisable(true);
        solveDecimal.setDisable(true);
        setButtonDecimal.setDisable(true);
        outputDecimal.setText(OutputMessages.INIT_TEXT);
        sudokuSolverDecimal = new SudokuSolver(new Grid(getGrid(gridPaneDecimal, SIZE_DECIMAL), SIZE_DECIMAL));
    }

    /**
     *
     */
    private void initGridHex() {
        generateSudokuLayoutHex();
        nextNumberHex.setDisable(true);
        solveHex.setDisable(true);
        setButtonHex.setDisable(true);
        outputHex.setText(OutputMessages.INIT_TEXT);
        sudokuSolverHex = new SudokuSolver(new Grid(getGrid(gridPaneHex, SIZE_HEX), SIZE_HEX));
    }

    /**
     *
     */
    private void initComboBoxes() {
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add(OutputMessages.OWN_SUDOKU);
        options.add(OutputMessages.EASY);
        options.add(OutputMessages.MEDIUM);
        options.add(OutputMessages.HARD);
        comboBoxDecimal.getItems().addAll(options);
        comboBoxDecimal.setValue(OutputMessages.OWN_SUDOKU);

        comboBoxHex.getItems().addAll(options);
        comboBoxHex.setValue(OutputMessages.OWN_SUDOKU);
    }

    @FXML
    public void nextNumberDecimal() {
        sudokuSolverDecimal.nextNumber();
        outputDecimal.setText(sudokuSolverDecimal.getOutputText());
        updateGrid(gridPaneDecimal, sudokuSolverDecimal, gridSudokuDecimal);
    }

    @FXML
    public void nextNumberHex() {
        sudokuSolverHex.nextNumber();
        outputHex.setText(sudokuSolverHex.getOutputText());
        updateGrid(gridPaneHex, sudokuSolverHex, gridSudokuHex);
    }

    @FXML
    public void setSudokuExampleDecimal() {
        switch (comboBoxDecimal.getValue()) {
            case OutputMessages.EASY -> setGridDecimal(new Grid(ExampleSudokus.simpleSudokuDecimal(), SIZE_DECIMAL));
            case OutputMessages.MEDIUM -> setGridDecimal(new Grid(ExampleSudokus.mediumSudokuDecimal(), SIZE_DECIMAL));
            case OutputMessages.HARD -> setGridDecimal(new Grid(ExampleSudokus.hardSudokuDecimal(), SIZE_DECIMAL));
            case OutputMessages.OWN_SUDOKU -> checkOwnSudokuDecimal();
        }
    }

    private void checkOwnSudokuDecimal() {
        int[][] newInput = getGrid(gridPaneDecimal, SIZE_DECIMAL);
        if (newInput == null) {
            outputDecimal.setText(OutputMessages.FAIL_TEXT_DECIMAL);
            return;
        }
        Grid newGrid = new Grid(newInput, SIZE_DECIMAL);
        if (newGrid.isSolveAble()) {
            setGridDecimal(newGrid);
            outputDecimal.setText(OutputMessages.SET_TEXT);
        } else {
            outputDecimal.setText(newGrid.getOutputMessage());
        }
    }

    @FXML
    public void setSudokuExampleHex() {
        switch (comboBoxHex.getValue()) {
            case OutputMessages.EASY -> setGridHex(new Grid(ExampleSudokus.simpleSudokuHex(), SIZE_HEX));
            case OutputMessages.MEDIUM -> setGridHex(new Grid(ExampleSudokus.mediumSudokuHex(), SIZE_HEX));
            case OutputMessages.HARD -> setGridHex(new Grid(ExampleSudokus.hardSudokuHex(), SIZE_HEX));
            case OutputMessages.OWN_SUDOKU -> checkOwnSudokuHex();
        }
    }

    private void checkOwnSudokuHex() {
        int[][] newInput = getGrid(gridPaneHex, SIZE_HEX);
        if (newInput == null) {
            outputHex.setText(OutputMessages.FAIL_TEXT_HEX);
            return;
        }
        Grid newGrid = new Grid(newInput, SIZE_HEX);
        if (newGrid.isSolveAble()) {
            setGridHex(newGrid);
            outputHex.setText(OutputMessages.SET_TEXT);
        } else {
            outputHex.setText(newGrid.getOutputMessage());
        }
    }

    @FXML
    public void getAndCheckGridDecimal() {
        int[][] sudoku = getGrid(gridPaneDecimal, SIZE_DECIMAL);
        if (sudoku == null) {
            outputDecimal.setText(OutputMessages.FAIL_TEXT_DECIMAL);
            return;
        }
        sudokuSolverDecimal.checkNewNumbers(sudoku);
        updateGrid(gridPaneDecimal, sudokuSolverDecimal, gridSudokuDecimal);
        outputDecimal.setText(sudokuSolverDecimal.getOutputText());
    }

    private void setGridDecimal(Grid grid) {
        gridSudokuDecimal = grid;
        sudokuSolverDecimal = new SudokuSolver(gridSudokuDecimal);
        updateGrid(gridPaneDecimal, sudokuSolverDecimal, gridSudokuDecimal);
        nextNumberDecimal.setDisable(false);
        solveDecimal.setDisable(false);
        setButtonDecimal.setDisable(false);
        outputDecimal.setText(OutputMessages.SET_TEXT);
    }

    @FXML
    public void getAndCheckGridHex() {
        int[][] sudoku = getGrid(gridPaneHex, SIZE_HEX);
        if (sudoku == null) {
            outputHex.setText(OutputMessages.FAIL_TEXT_HEX);
            return;
        }
        sudokuSolverHex.checkNewNumbers(sudoku);
        updateGrid(gridPaneHex, sudokuSolverHex, gridSudokuHex);
        outputHex.setText(sudokuSolverHex.getOutputText());
    }

    private void setGridHex(Grid grid) {
        gridSudokuHex = grid;
        sudokuSolverHex = new SudokuSolver(gridSudokuHex);
        updateGrid(gridPaneHex, sudokuSolverHex, gridSudokuHex);
        nextNumberHex.setDisable(false);
        solveHex.setDisable(false);
        setButtonHex.setDisable(false);
        outputHex.setText(OutputMessages.SET_TEXT);
    }

    @FXML
    public void generateSudokuLayoutDecimal() {
        gridPaneDecimal.getChildren().clear();
        generateSudokuLayout(gridPaneDecimal, SIZE_DECIMAL);
        setButtonDecimal.setDisable(true);
        nextNumberDecimal.setDisable(true);
        solveDecimal.setDisable(true);
        outputDecimal.setText(OutputMessages.INIT_TEXT);
    }

    @FXML
    public void generateSudokuLayoutHex() {
        gridPaneHex.getChildren().clear();
        generateSudokuLayout(gridPaneHex, SIZE_HEX);
        setButtonHex.setDisable(true);
        nextNumberHex.setDisable(true);
        solveHex.setDisable(true);
        outputHex.setText(OutputMessages.INIT_TEXT);
    }

    @FXML
    public void solveDecimal() {
        while (sudokuSolverDecimal.isNotDone()) {
            sudokuSolverDecimal.nextNumber();
        }
        sudokuSolverDecimal.nextNumber();
        updateGrid(gridPaneDecimal, sudokuSolverDecimal, gridSudokuDecimal);
        outputDecimal.setText(sudokuSolverDecimal.getOutputText());
    }

    @FXML
    public void solveHex() {
        while (sudokuSolverHex.isNotDone()) {
            sudokuSolverHex.nextNumber();
        }
        sudokuSolverHex.nextNumber();
        updateGrid(gridPaneHex, sudokuSolverHex, gridSudokuHex);
        outputHex.setText(sudokuSolverHex.getOutputText());
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
        if (size == SIZE_DECIMAL) {
            regex = "[1-9]";
        } else {
            regex = "^(1[0-6]|[1-9])$";
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

    private void updateGrid(GridPane grid, SudokuSolver sudokuSolver, Grid gridSudoku) {
        boolean[][] newFields = sudokuSolver.getNewFields();
        boolean[][] removedFields = sudokuSolver.getRemovedFieldsFields();
        boolean[][] startingSudoku = gridSudoku.getStartingSudoku();
        int row = 0;
        int column = 0;
        for (Node node : grid.getChildren()) {
            if (column % gridSudoku.getGridSize() == 0 && column != 0) {
                row++;
            }
            if (node instanceof TextField textField) {
                textField.setDisable(false);
                int number = gridSudoku.getGrid()[row][column % gridSudoku.getGridSize()];
                setTextFieldColor(textField, row, column % gridSudoku.getGridSize(), gridSudoku.getGridSize());
                if (number != 0) {
                    textField.setText(Integer.toString(gridSudoku.getGrid()[row][column % gridSudoku.getGridSize()]));
                    if (startingSudoku[row][column % gridSudoku.getGridSize()]) {
                        textField.setStyle(textField.getStyle() + "-fx-font-weight: bold; -fx-font-size: 20px;");
                        textField.setDisable(true);
                    }
                    if (newFields[row][column % gridSudoku.getGridSize()]) {
                        textField.setStyle("-fx-background-color: green;");
                    } else if (removedFields[row][column % gridSudoku.getGridSize()]) {
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