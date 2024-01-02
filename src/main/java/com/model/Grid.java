package com.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int[][] grid;
    private final int gridSize;

    public Grid(int[][] grid, int gridSize) {
        this.grid = grid;
        this.gridSize = gridSize;
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
        return checkRow(number, row) && checkColumn(number, column) && checkBox(number, row, column);
    }

    private boolean checkRow(int number, int row) {
        for (int i = 0; i < gridSize; i++) {
            if (grid[row][i] == number) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int number, int column) {
        for (int i = 0; i < gridSize; i++) {
            if (grid[i][column] == number) {
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
                if (grid[i][j] == number) {
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
}
