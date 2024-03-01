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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGridDecimal();
        initGridHex();
        initComboBoxes();
    }

    /**
     *
     */
    private void initGridDecimal() {
        generateSudokuLayout(gridPaneDecimal, SIZE_DECIMAL);
        sudokuSolverDecimal = new SudokuSolver(
                new Grid(getGrid(gridPaneDecimal, SIZE_DECIMAL), SIZE_DECIMAL), this);
        setUpButtonDecimal(true);
    }

    /**
     *
     */
    private void initGridHex() {
        generateSudokuLayout(gridPaneHex, SIZE_HEX);
        sudokuSolverHex = new SudokuSolver(new Grid(getGrid(gridPaneHex, SIZE_HEX), SIZE_HEX), this);
        setUpButtonHex(true);
    }

    @FXML
    public void nextNumberDecimal() {
        sudokuSolverDecimal.nextNumber(false);
    }

    @FXML
    public void nextNumberHex() {
        sudokuSolverHex.nextNumber(false);
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
    public void setSudokuExampleDecimal() {
        switch (comboBoxDecimal.getValue()) {
            case OutputMessages.EASY -> setGridDecimal(ExampleSudokus.simpleSudokuDecimal());
            case OutputMessages.MEDIUM -> setGridDecimal(ExampleSudokus.mediumSudokuDecimal());
            case OutputMessages.HARD -> setGridDecimal(ExampleSudokus.hardSudokuDecimal());
            case OutputMessages.OWN_SUDOKU -> checkOwnSudokuDecimal();
        }
    }

    @FXML
    public void setSudokuExampleHex() {
        switch (comboBoxHex.getValue()) {
            case OutputMessages.EASY -> setGridHex(ExampleSudokus.simpleSudokuHex());
            case OutputMessages.MEDIUM -> setGridHex(ExampleSudokus.mediumSudokuHex());
            case OutputMessages.HARD -> setGridHex(ExampleSudokus.hardSudokuHex());
            case OutputMessages.OWN_SUDOKU -> checkOwnSudokuHex();
        }
    }

    private void checkOwnSudokuDecimal() {
        int[][] newInput = getGrid(gridPaneDecimal, SIZE_DECIMAL);
        if (newInput == null) {
            setLabel(outputDecimal, sudokuSolverDecimal, OutputMessages.FAIL_TEXT_DECIMAL);
            return;
        }
        sudokuSolverDecimal.checkNewGrid(newInput);
    }

    private void checkOwnSudokuHex() {
        int[][] newInput = getGrid(gridPaneHex, SIZE_HEX);
        if (newInput == null) {
            setLabel(outputHex, sudokuSolverHex, OutputMessages.FAIL_TEXT_HEX);
            return;
        }
        sudokuSolverHex.checkNewGrid(newInput);
    }

    @FXML
    public void getAndCheckGridDecimal() {
        int[][] sudoku = getGrid(gridPaneDecimal, SIZE_DECIMAL);
        if (sudoku == null) {
            setLabel(outputDecimal, sudokuSolverDecimal, OutputMessages.FAIL_TEXT_DECIMAL);
            return;
        }
        sudokuSolverDecimal.checkNewNumbers(sudoku);
    }

    @FXML
    public void getAndCheckGridHex() {
        int[][] sudoku = getGrid(gridPaneHex, SIZE_HEX);
        if (sudoku == null) {
            setLabel(outputHex, sudokuSolverHex, OutputMessages.FAIL_TEXT_HEX);
            return;
        }
        sudokuSolverHex.checkNewNumbers(sudoku);
    }

    private void setGridDecimal(int[][] grid) {
        sudokuSolverDecimal.clearSudoku();
        sudokuSolverDecimal.checkNewGrid(grid);
    }

    private void setGridHex(int[][] grid) {
        sudokuSolverHex.clearSudoku();
        sudokuSolverHex.checkNewGrid(grid);
    }

    @FXML
    public void clearSudokuDecimal() {
        sudokuSolverDecimal.clearSudoku();
        setUpButtonDecimal(true);
        comboBoxDecimal.setValue(OutputMessages.OWN_SUDOKU);
    }

    @FXML
    public void clearSudokuHex() {
        sudokuSolverHex.clearSudoku();
        setUpButtonHex(true);
        comboBoxHex.setValue(OutputMessages.OWN_SUDOKU);
    }

    @FXML
    public void solveDecimal() {
        sudokuSolverDecimal.solveSudoku();
    }

    @FXML
    public void solveHex() {
        sudokuSolverHex.solveSudoku();
    }

    /**
     * @param grid
     * @param size
     */
    private void generateSudokuLayout(GridPane grid, int size) {
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                TextField textField = new TextField();
                grid.add(textField, k, i);
                setTextFieldColor(textField, k, i, size);
            }
        }
    }

    /**
     * @param grid
     * @param size
     * @return
     */
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

    /**
     * @param grid
     * @param sudokuSolver
     * @param gridSudoku
     */
    private void updateGrid(GridPane grid, SudokuSolver sudokuSolver, Grid gridSudoku) {
        boolean[][] newFields = sudokuSolver.getNewFields();
        boolean[][] removedFields = sudokuSolver.getRemovedFieldsFields();
        boolean[][] indexFields = sudokuSolver.getIndexFields();
        boolean[][] startingSudoku = gridSudoku.getStartingSudoku();
        int[][] temporaryNumbers = sudokuSolver.getTemporaryNumbers();
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
                } else if (temporaryNumbers[row][column % gridSudoku.getGridSize()] != 0) {
                    textField.setText(Integer.toString(temporaryNumbers[row][column % gridSudoku.getGridSize()]));
                } else {
                    textField.setText("");
                }
                if (startingSudoku[row][column % gridSudoku.getGridSize()]) {
                    textField.setStyle(textField.getStyle() + "-fx-font-weight: bold; -fx-font-size: 20px;");
                    textField.setDisable(true);
                }
                if (indexFields[row][column % gridSudoku.getGridSize()]) {
                    textField.setStyle("-fx-background-color: yellow;");
                }
                if (newFields[row][column % gridSudoku.getGridSize()]) {
                    textField.setStyle("-fx-background-color: green;");
                } else if (removedFields[row][column % gridSudoku.getGridSize()]) {
                    textField.setStyle("-fx-background-color: red;");
                }
            }
            column++;
        }
    }

    /**
     * @param t
     * @param row
     * @param column
     * @param size
     */
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

    /**
     * @param label
     * @param solver
     * @param output
     */
    private void setLabel(Label label, SudokuSolver solver, String output) {
        if (solver != null) {
            int height = 30 + (Math.max(solver.getNumberOfLines() - 1, 0)) * 20;
            label.setStyle("-fx-min-height: " + height);
        }
        label.setText(output);
    }

    /**
     * @param size
     * @param output
     * @param grid
     */
    public void update(int size, String output, Grid grid, boolean isSolvable) {
        switch (size) {
            case SIZE_DECIMAL: {
                setLabel(outputDecimal, sudokuSolverDecimal, output);
                setUpButtonDecimal(isSolvable);
                if (sudokuSolverDecimal != null) {
                    updateGrid(gridPaneDecimal, sudokuSolverDecimal, grid);
                }
                return;
            }
            case SIZE_HEX: {
                setLabel(outputHex, sudokuSolverHex, output);
                setUpButtonHex(isSolvable);
                if (sudokuSolverHex != null) {
                    updateGrid(gridPaneHex, sudokuSolverHex, grid);
                }
            }
        }
    }

    private void setUpButtonDecimal(boolean value) {
        nextNumberDecimal.setDisable(value);
        solveDecimal.setDisable(value);
        setButtonDecimal.setDisable(value);
    }

    private void setUpButtonHex(boolean value) {
        nextNumberHex.setDisable(value);
        solveHex.setDisable(value);
        setButtonHex.setDisable(value);
    }
}