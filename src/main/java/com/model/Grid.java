package com.model;

import com.Data.OutputMessages;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int[][] grid;
    private final int gridSize;
    private boolean solveAble;
    private final boolean[][] startingSudoku;
    private String outputMessage;

    public Grid(int[][] grid, int gridSize) {
        this.grid = grid;
        this.gridSize = gridSize;
        startingSudoku = new boolean[gridSize][gridSize];
        outputMessage = "";
        checkInput();
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

    public boolean checkNumberIsValid(int number, int row, int column) {
        return checkRow(number, row, column) && checkColumn(number, row, column) && checkBox(number, row, column);
    }

    private boolean checkRow(int number, int row, int column) {
        for (int i = 0; i < gridSize; i++) {
            if (grid[row][i] == number && i != column) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int number, int row, int column) {
        for (int i = 0; i < gridSize; i++) {
            if (grid[i][column] == number && i != row) {
                return false;
            }
        }
        return true;
    }

    private boolean checkBox(int number, int row, int column) {
        int boxSize = (int) Math.sqrt(gridSize);
        int localRow = row - row % boxSize;
        int localColumn = column - column % boxSize;
        for (int i = localRow; i < localRow + boxSize; i++) {
            for (int j = localColumn; j < localColumn + boxSize; j++) {
                if (grid[i][j] == number && !(i == row && j == column)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Integer> getPossibleNumbers(int row, int column) {
        List<Integer> possibleNumbers = new ArrayList<>();
        for (int i = 1; i <= gridSize; i++) {
            if (checkNumberIsValid(i, row, column)) {
                possibleNumbers.add(i);
            }
        }
        return possibleNumbers;
    }

    public List<Integer> getNewPossibleNumbers(List<Integer> numbers, int row, int column) {
        List<Integer> result = new ArrayList<>();
        for (int number : numbers) {
            if (checkNumberIsValid(number, row, column)) {
                result.add(number);
            }
        }
        return result;
    }

    public void checkInput() {
        solveAble = false;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                //
                if (!checkNumberIsValid(grid[i][j], i, j) && grid[i][j] != 0) {
                    solveAble = false;
                    outputMessage = OutputMessages.numberNotAllowed(grid[i][j], i, j);
                    return;
                }
                if (grid[i][j] != 0) {
                    startingSudoku[i][j] = true;
                    solveAble = true;
                }
            }
        }
        // input has no number
        if (!solveAble) {
            outputMessage = OutputMessages.CHOOSE_AN_EXAMPLE;
        }
    }
}