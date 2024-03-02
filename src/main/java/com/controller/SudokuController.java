package com.controller;

import com.Data.ExampleSudokus;
import com.Data.Feedback;
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
    public Label labelDecimal;
    @FXML
    public Label labelHex;
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
     * Initializes the decimal grid by generating the Sudoku layout,
     * creating a SudokuSolver instance, and setting up buttons.
     */
    private void initGridDecimal() {
        generateSudokuLayout(gridPaneDecimal, SIZE_DECIMAL);
        sudokuSolverDecimal = new SudokuSolver(
                new Grid(getGrid(gridPaneDecimal, SIZE_DECIMAL), SIZE_DECIMAL), this);
        setUpButtonDecimal(true);
    }

    /**
     * Initializes the hex grid by generating the Sudoku layout,
     * creating a SudokuSolver instance, and setting up buttons.
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
     * Initializes the combo boxes with feedback options.
     * Sets the default value to "Own Sudoku".
     */
    private void initComboBoxes() {
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add(Feedback.OWN_SUDOKU);
        options.add(Feedback.EASY);
        options.add(Feedback.MEDIUM);
        options.add(Feedback.HARD);
        comboBoxDecimal.getItems().addAll(options);
        comboBoxDecimal.setValue(Feedback.OWN_SUDOKU);
        comboBoxHex.getItems().addAll(options);
        comboBoxHex.setValue(Feedback.OWN_SUDOKU);
    }

    /**
     * Sets the decimal grid with an example Sudoku based on the selected feedback option.
     * If "Own Sudoku" is selected, prompts the user to input their own Sudoku.
     */
    @FXML
    public void setSudokuExampleDecimal() {
        switch (comboBoxDecimal.getValue()) {
            case Feedback.EASY -> setGridDecimal(ExampleSudokus.simpleSudokuDecimal());
            case Feedback.MEDIUM -> setGridDecimal(ExampleSudokus.mediumSudokuDecimal());
            case Feedback.HARD -> setGridDecimal(ExampleSudokus.hardSudokuDecimal());
            case Feedback.OWN_SUDOKU -> checkOwnSudokuDecimal();
        }
    }

    /**
     * Sets the hex grid with an example Sudoku based on the selected feedback option.
     * If "Own Sudoku" is selected, prompts the user to input their own Sudoku.
     */
    @FXML
    public void setSudokuExampleHex() {
        switch (comboBoxHex.getValue()) {
            case Feedback.EASY -> setGridHex(ExampleSudokus.simpleSudokuHex());
            case Feedback.MEDIUM -> setGridHex(ExampleSudokus.mediumSudokuHex());
            case Feedback.HARD -> setGridHex(ExampleSudokus.hardSudokuHex());
            case Feedback.OWN_SUDOKU -> checkOwnSudokuHex();
        }
    }

    /**
     * Checks the validity of the user-provided decimal Sudoku grid.
     * If the input grid is invalid, displays a failure message.
     * Otherwise, passes the grid to the Sudoku solver for validation.
     */
    private void checkOwnSudokuDecimal() {
        int[][] newInput = getGrid(gridPaneDecimal, SIZE_DECIMAL);
        if (newInput == null) {
            setLabel(labelDecimal, sudokuSolverDecimal, Feedback.FAIL_TEXT_DECIMAL);
            return;
        }
        sudokuSolverDecimal.checkNewGrid(newInput);
    }

    /**
     * Checks the validity of the user-provided hexadecimal Sudoku grid.
     * If the input grid is invalid, displays a failure message.
     * Otherwise, passes the grid to the Sudoku solver for validation.
     */
    private void checkOwnSudokuHex() {
        int[][] newInput = getGrid(gridPaneHex, SIZE_HEX);
        if (newInput == null) {
            setLabel(labelHex, sudokuSolverHex, Feedback.FAIL_TEXT_HEX);
            return;
        }
        sudokuSolverHex.checkNewGrid(newInput);
    }

    /**
     * Retrieves the decimal Sudoku grid from the UI and checks its validity.
     * If the input grid is invalid, displays a failure message.
     * Otherwise, passes the grid to the Sudoku solver for validation.
     */
    @FXML
    public void getAndCheckGridDecimal() {
        int[][] sudoku = getGrid(gridPaneDecimal, SIZE_DECIMAL);
        if (sudoku == null) {
            setLabel(labelDecimal, sudokuSolverDecimal, Feedback.FAIL_TEXT_DECIMAL);
            return;
        }
        sudokuSolverDecimal.checkNewNumbers(sudoku);
    }

    /**
     * Retrieves the hexadecimal Sudoku grid from the UI and checks its validity.
     * If the input grid is invalid, displays a failure message.
     * Otherwise, passes the grid to the Sudoku solver for validation.
     */
    @FXML
    public void getAndCheckGridHex() {
        int[][] sudoku = getGrid(gridPaneHex, SIZE_HEX);
        if (sudoku == null) {
            setLabel(labelHex, sudokuSolverHex, Feedback.FAIL_TEXT_HEX);
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

    /**
     * Clears the Sudoku grid for the decimal Sudoku variant.
     * Resets buttons and sets the combobox value to "Own Sudoku".
     */
    @FXML
    public void clearSudokuDecimal() {
        sudokuSolverDecimal.clearSudoku();
        setUpButtonDecimal(true);
        comboBoxDecimal.setValue(Feedback.OWN_SUDOKU);
    }

    /**
     * Clears the Sudoku grid for the hex Sudoku variant.
     * Resets buttons and sets the combobox value to "Own Sudoku".
     */
    @FXML
    public void clearSudokuHex() {
        sudokuSolverHex.clearSudoku();
        setUpButtonHex(true);
        comboBoxHex.setValue(Feedback.OWN_SUDOKU);
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
     * Generates the layout of a Sudoku grid within a JavaFX GridPane, consisting of TextFields for input.
     *
     * @param grid The JavaFX GridPane where the Sudoku grid layout will be generated.
     * @param size The size of the Sudoku grid (number of rows/columns).
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
     * Extracts the current state of the Sudoku grid from a JavaFX GridPane and returns it as a 2D integer array.
     *
     * @param grid The JavaFX GridPane containing the Sudoku grid.
     * @param size The size of the Sudoku grid (either SIZE_DECIMAL or SIZE_HEX).
     * @return A 2D integer array representing the current state of the Sudoku grid, where 0 indicates an empty cell.
     *         Returns null if the input contains invalid or incomplete Sudoku grid data.
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
     * Updates the appearance and content of a JavaFX GridPane
     * based on the current state of the Sudoku grid.
     *
     * @param grid         The JavaFX GridPane to be updated.
     * @param sudokuSolver The SudokuSolver object containing the current state of the Sudoku grid.
     * @param gridSudoku   The Grid object representing the Sudoku grid.
     */
    private void updateGrid(GridPane grid, SudokuSolver sudokuSolver, Grid gridSudoku) {
        boolean[][] newCells = sudokuSolver.getNewCells();
        boolean[][] removedCells = sudokuSolver.getRemovedCells();
        boolean[][] indexCells = sudokuSolver.getIndexCells();
        boolean[][] startingSudoku = gridSudoku.getStartingSudoku();
        int[][] temporaryCells = sudokuSolver.getTemporaryCells();
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
                    textField.clear();
                    textField.setText(Integer.toString(gridSudoku.getGrid()[row][column % gridSudoku.getGridSize()]));
                } else if (temporaryCells[row][column % gridSudoku.getGridSize()] != 0) {
                    textField.clear();
                    textField.setText(Integer.toString(temporaryCells[row][column % gridSudoku.getGridSize()]));
                } else {
                    textField.clear();
                }
                if (startingSudoku[row][column % gridSudoku.getGridSize()]) {
                    textField.setStyle(textField.getStyle() + "-fx-font-weight: bold;");
                    textField.setDisable(true);
                }
                if (indexCells[row][column % gridSudoku.getGridSize()]) {
                    textField.setStyle("-fx-background-color: yellow;");
                }
                if (newCells[row][column % gridSudoku.getGridSize()]) {
                    textField.setStyle("-fx-background-color: green;");
                } else if (removedCells[row][column % gridSudoku.getGridSize()]) {
                    textField.setStyle("-fx-background-color: red;");
                }
            }
            column++;
        }
    }

    /**
     * Sets the background color of a JavaFX TextField based on the position
     * of the cell in the Sudoku grid.
     *
     * @param t      The JavaFX TextField to be updated.
     * @param row    The row index of the cell.
     * @param column The column index of the cell.
     * @param size   The size of the Sudoku grid (e.g., 9 for a standard 9x9 grid).
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
     * Sets the text and style of a JavaFX label based on the output message
     * and the number of lines in the Sudoku solver's solution.
     *
     * @param label  The JavaFX label to be updated.
     * @param solver The SudokuSolver object containing the solution data.
     * @param output The output message to be displayed on the label.
     */
    private void setLabel(Label label, SudokuSolver solver, String output) {
        if (solver != null) {
            int height = 30 + (Math.max(solver.getNumberOfLines() - 1, 0)) * 20;
            label.setStyle("-fx-min-height: " + height);
        }
        if (label != null) {
            label.setText(output);
        }
    }

    /**
     * Updates the user interface based on the size of the Sudoku puzzle,
     * the output message, the current state of the grid, and the solvability status.
     *
     * @param size       An integer representing the size of the Sudoku puzzle.
     *                   It can be either SIZE_DECIMAL or SIZE_HEX.
     * @param output     A string containing the output message to display to the user.
     * @param grid       An instance of the Grid class representing the current state of the Sudoku grid.
     * @param isSolvable A boolean indicating whether the Sudoku puzzle is solvable.
     *                   If true, buttons are disabled; otherwise, they remain enabled.
     */
    public void update(int size, String output, Grid grid, boolean isSolvable) {
        switch (size) {
            case SIZE_DECIMAL: {
                setLabel(labelDecimal, sudokuSolverDecimal, output);
                setUpButtonDecimal(isSolvable);
                if (sudokuSolverDecimal != null) {
                    updateGrid(gridPaneDecimal, sudokuSolverDecimal, grid);
                }
                return;
            }
            case SIZE_HEX: {
                setLabel(labelHex, sudokuSolverHex, output);
                setUpButtonHex(isSolvable);
                if (sudokuSolverHex != null) {
                    updateGrid(gridPaneHex, sudokuSolverHex, grid);
                }
            }
        }
    }

    /**
     * Sets up the buttons related to hexadecimal Sudoku solving based on the solvability status.
     * If the Sudoku is solvable, the "Next Number", "Solve", and "Set" buttons are disabled;
     * otherwise, they remain enabled to allow user interaction.
     *
     * @param isSolvable A boolean indicating whether the Sudoku puzzle is solvable.
     *                   If true, buttons are disabled; otherwise, they remain enabled.
     */
    private void setUpButtonDecimal(boolean isSolvable) {
        if (nextNumberDecimal != null) {
            nextNumberDecimal.setDisable(isSolvable);
        }
        if (solveDecimal != null) {
            solveDecimal.setDisable(isSolvable);
        }
        if (setButtonDecimal != null) {
            setButtonDecimal.setDisable(isSolvable);
        }
    }

    /**
     * Sets up the buttons related to hexadecimal Sudoku solving based on the solvability status.
     * If the Sudoku is solvable, the "Next Number", "Solve", and "Set" buttons are disabled;
     * otherwise, they remain enabled to allow user interaction.
     *
     * @param isSolvable A boolean indicating whether the Sudoku puzzle is solvable.
     *                   If true, buttons are disabled; otherwise, they remain enabled.
     */
    private void setUpButtonHex(boolean isSolvable) {
        if (nextNumberHex != null) {
            nextNumberHex.setDisable(isSolvable);
        }
        if (solveHex != null) {
            solveHex.setDisable(isSolvable);
        }
        if (setButtonHex != null) {
            setButtonHex.setDisable(isSolvable);
        }
    }
}