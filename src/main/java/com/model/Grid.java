package com.model;

import com.Data.OutputMessages;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int[][] grid;
    private final int gridSize;
    private boolean solveAble;
    private final boolean[][] startingSudoku;
    private String outputMessage;

    public Grid(int[][] grid, int gridSize) {
        this.grid = grid;
        this.gridSize = gridSize;
        startingSudoku = new boolean[gridSize][gridSize];
        outputMessage = "";
        checkInputs(true);
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public boolean isSolveAble() {
        return solveAble;
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

    public List<Integer> getPossibleNumbers(int column, int row) {
        List<Integer> possibleNumbers = new ArrayList<>();
        for (int i = 1; i <= gridSize; i++) {
            if (checkNumberIsValid(i, column, row)) {
                possibleNumbers.add(i);
            }
        }
        return possibleNumbers;
    }

    public List<Integer> getNewPossibleNumbers(List<Integer> numbers, int column, int row) {
        List<Integer> result = new ArrayList<>();
        for (int number : numbers) {
            if (checkNumberIsValid(number, column, row)) {
                result.add(number);
            }
        }
        return result;
    }

    public void checkInputs(boolean newGrid) {
        boolean hasNumber = false;
        outputMessage = "";
        solveAble = false;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                //
                if (!checkColumn(grid[i][j], i, j) && grid[i][j] != 0) {
                    solveAble = false;
                    outputMessage = OutputMessages.numberNotAllowedInColumn(grid[i][j], i, j);
                    return;
                } else if (!checkRow(grid[i][j], i, j) && grid[i][j] != 0) {
                    solveAble = false;
                    outputMessage = OutputMessages.numberNotAllowedInRow(grid[i][j], i, j);
                    return;
                } else if (!checkBlock(grid[i][j], i, j) && grid[i][j] != 0) {
                    solveAble = false;
                    outputMessage = OutputMessages.numberNotAllowedInBlock(grid[i][j], i, j);
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
            outputMessage = OutputMessages.CHOOSE_AN_EXAMPLE;
            solveAble = false;
        } else {
            solveAble = true;
        }
    }

    public void checkInput(int column, int row) {
        solveAble = false;
        if (!checkColumn(grid[column][row], column, row) && grid[column][row] != 0) {
            solveAble = false;
            outputMessage = OutputMessages.numberNotAllowedInColumn(grid[column][row], column, row);
            return;
        } else if (!checkRow(grid[column][row], column, row) && grid[column][row] != 0) {
            solveAble = false;
            outputMessage = OutputMessages.numberNotAllowedInRow(grid[column][row], column, row);
            return;
        } else if (!checkBlock(grid[column][row], column, row) && grid[column][row] != 0) {
            solveAble = false;
            outputMessage = OutputMessages.numberNotAllowedInBlock(grid[column][row], column, row);
            return;
        }
        if (grid[column][row] != 0) {
            startingSudoku[column][row] = true;
            solveAble = true;
        }
        // input has no number
        if (!solveAble) {
            outputMessage = OutputMessages.CHOOSE_AN_EXAMPLE;
        }
    }
}