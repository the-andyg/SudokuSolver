package com.model;

import com.Data.Feedback;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int[][] grid;
    private final int gridSize;
    private boolean solvable;
    private final boolean[][] startingSudoku;
    private String feedback;

    public Grid(int[][] grid, int gridSize) {
        this.grid = grid;
        this.gridSize = gridSize;
        startingSudoku = new boolean[gridSize][gridSize];
        feedback = "";
        checkInputs(true);
    }

    public String getFeedback() {
        return feedback;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public boolean[][] getStartingSudoku() {
        return startingSudoku;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public void setNumber(int number, int row, int column) {
        this.grid[row][column] = number;
    }

    public int getGridSize() {
        return gridSize;
    }

    /**
     * Clears the sudoku grid by setting all cell values to 0 and marking all cells as non-starting cells.
     * It iterates through each cell in the grid and sets its value to 0, indicating an empty cell,
     * and marks it as a non-starting cell by setting the corresponding flag in the startingSudoku array to false.
     */
    public void clearSudoku() {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                grid[x][y] = 0;
                startingSudoku[x][y] = false;
            }
        }
    }

    public boolean checkNumberIsValid(int number, int column, int row) {
        return checkRow(number, column, row) && checkColumn(number, column, row) && checkBlock(number, column, row);
    }

    private boolean checkRow(int number, int column, int row) {
        for (int i = 0; i < gridSize; i++) {
            if (grid[i][row] == number && i != column) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int number, int column, int row) {
        for (int i = 0; i < gridSize; i++) {
            if (grid[column][i] == number && i != row) {
                return false;
            }
        }
        return true;
    }

    private boolean checkBlock(int number, int column, int row) {
        int blockSize = (int) Math.sqrt(gridSize);
        int localRow = row - row % blockSize;
        int localColumn = column - column % blockSize;
        for (int i = localColumn; i < localColumn + blockSize; i++) {
            for (int j = localRow; j < localRow + blockSize; j++) {
                if (grid[i][j] == number && !(i == column && j == row)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Retrieves the list of possible numbers that can be placed in the specified cell without violating Sudoku rules.
     *
     * @param column Column index of the cell.
     * @param row    Row index of the cell.
     * @return List of valid numbers for the cell.
     */
    public List<Integer> getPossibleNumbers(int column, int row) {
        List<Integer> possibleNumbers = new ArrayList<>();
        for (int i = 1; i <= gridSize; i++) {
            if (checkNumberIsValid(i, column, row)) {
                possibleNumbers.add(i);
            }
        }
        return possibleNumbers;
    }

    /**
     * Retrieves the list of possible numbers that can be placed in the specified cell based on the current state
     * of the Sudoku grid, excluding numbers that would violate Sudoku rules for the given column and row.
     *
     * @param numbers List of numbers to filter for validity.
     * @param column  Column index of the cell.
     * @param row     Row index of the cell.
     * @return List of valid numbers for the cell.
     */
    public List<Integer> getNewPossibleNumbers(List<Integer> numbers, int column, int row) {
        List<Integer> result = new ArrayList<>();
        for (int number : numbers) {
            if (checkNumberIsValid(number, column, row)) {
                result.add(number);
            }
        }
        return result;
    }

    /**
     * Checks if the current state of the Sudoku grid satisfies the Sudoku rules for columns, rows, and blocks.
     * If a number violates any constraint, it updates feedback accordingly and sets solveAble to false.
     * If the grid has no numbers and is new, it prompts the user to choose an example Sudoku.
     *
     * @param newGrid Flag indicating whether the grid is new.
     */
    public void checkInputs(boolean newGrid) {
        boolean hasNumber = false;
        feedback = "";
        solvable = false;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                //
                if (!checkColumn(grid[i][j], i, j) && grid[i][j] != 0) {
                    solvable = false;
                    feedback = Feedback.numberNotAllowedInColumn(grid[i][j], i, j);
                    return;
                } else if (!checkRow(grid[i][j], i, j) && grid[i][j] != 0) {
                    solvable = false;
                    feedback = Feedback.numberNotAllowedInRow(grid[i][j], i, j);
                    return;
                } else if (!checkBlock(grid[i][j], i, j) && grid[i][j] != 0) {
                    solvable = false;
                    feedback = Feedback.numberNotAllowedInBlock(grid[i][j], i, j);
                    return;
                }
                if (grid[i][j] != 0 && newGrid) {
                    startingSudoku[i][j] = true;
                    hasNumber = true;
                }
            }
        }
        // input has no number
        if (!hasNumber && newGrid) {
            feedback = Feedback.CHOOSE_AN_EXAMPLE;
            solvable = false;
        } else {
            solvable = true;
        }
    }

    /**
     * Checks if placing a number at the specified column and row in the grid is valid according to Sudoku rules.
     * It verifies column, row, and block constraints. If the number violates any constraint, it updates feedback
     * and returns false. If the cell is empty, it prompts the user to choose an example Sudoku.
     *
     * @param column The column index.
     * @param row    The row index.
     * @return true if the placement is valid, false otherwise.
     */
    public boolean checkInput(int column, int row) {
        solvable = false;
        if (!checkColumn(grid[column][row], column, row) && grid[column][row] != 0) {
            solvable = false;
            feedback = Feedback.numberNotAllowedInColumn(grid[column][row], column, row);
            return false;
        } else if (!checkRow(grid[column][row], column, row) && grid[column][row] != 0) {
            solvable = false;
            feedback = Feedback.numberNotAllowedInRow(grid[column][row], column, row);
            return false;
        } else if (!checkBlock(grid[column][row], column, row) && grid[column][row] != 0) {
            solvable = false;
            feedback = Feedback.numberNotAllowedInBlock(grid[column][row], column, row);
            return false;
        }
        if (grid[column][row] != 0) {
            startingSudoku[column][row] = true;
            solvable = true;
        }
        // input has no number
        if (!solvable) {
            feedback = Feedback.CHOOSE_AN_EXAMPLE;
            return false;
        }
        return true;
    }
}