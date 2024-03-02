package com.model;

import com.Data.OutputMessages;
import com.controller.SudokuController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SudokuSolver {
    private Grid grid;
    private final HashMap<Integer, List<Cell>> mapOfCells;
    private final List<Cell> solvedCells;
    private int cellsSearching;
    private String outputText;
    private boolean[][] newCells;
    private boolean[][] removedCells;
    private boolean[][] indexCells;
    private int [][] temporaryCells;
    private int numberOfLines;
    private boolean isSolvable;
    private final SudokuController sudokuController;

    public SudokuSolver(Grid grid, SudokuController sudokuController) {
        this.grid = grid;
        this.sudokuController = sudokuController;
        mapOfCells = new HashMap<>();
        solvedCells = new ArrayList<>();
        isSolvable = true;
        createMap();
        setupOutput();
        checkGrid();
    }

    private void update() {
        sudokuController.update(grid.getGridSize(), outputText, grid, !grid.isSolveAble());
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public boolean[][] getNewCells() {
        return newCells;
    }

    public boolean[][] getRemovedCells() {
        return removedCells;
    }

    public boolean[][] getIndexCells() {
        return indexCells;
    }

    public boolean isNotDone() {
        return !(solvedCells.size() == cellsSearching);
    }

    public int[][] getTemporaryCells() {
        return temporaryCells;
    }

    private void checkGrid() {
        grid.checkInputs(true);
        if (!grid.isSolveAble()) {
            outputText = OutputMessages.INIT_TEXT;
        } else {
            outputText = OutputMessages.SET_TEXT;
        }
        update();
    }

    public void checkNewGrid(int[][] newGrid) {
        grid.clearSudoku();
        setupOutput();
        Grid grid = new Grid(newGrid, this.grid.getGridSize());
        if (grid.isSolveAble()) {
            setupOutput();
            outputText = OutputMessages.SET_TEXT;
            this.grid = grid;
            createMap();
        } else {
            outputText = grid.getOutputMessage();
            this.grid = grid;
        }
        update();
    }

    public void clearSudoku() {
        grid.clearSudoku();
        createMap();
        setupOutput();
        outputText = OutputMessages.CLEAR_SUDOKU;
        update();
    }

    public void solveSudoku() {
        while (isNotDone()) {
            nextNumber(true);
        }
        nextNumber(true);
        update();
    }

    /**
     *
     */
    public void nextNumber(boolean isLoop) {
        if (sortMap()) {
            // backtracking was necessary
            if (!isLoop) {
                update();
            }
            return;
        }
        setupOutput();
        // check if Sudoku is done
        if (!isNotDone()) {
            if (!isSolvable) {
                outputText = OutputMessages.SUDOKU_NOT_SOLVABLE;
            } else {
                outputText = OutputMessages.SUDOKU_SOLVED;
            }
            if (!isLoop) {
                update();
            }
            return;
        }
        // check if there is a distinct Cell
        if (setDistinctNumber()) {
            if (!isLoop) {
                update();
            }
            return;
        }
        // check if there is a distinct Cell in Row, Column or Block
        if (checkBlockRowColumn()) {
            if (!isLoop) {
                update();
            }
            return;
        }
        // set a Cell with multiple opportunities
        setNextNumber();
        if (!isLoop) {
            update();
        }
    }

    public void checkNewNumbers(int[][] numbers) {
        sortMap();
        setupOutput();
        Grid newGrid = new Grid(numbers, numbers.length);
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers.length; j++) {
                if (grid.getGrid()[i][j] != 0 && grid.getGrid()[i][j] != numbers[i][j]) {
                    // old Cell had already a number
                    deleteOrChangeNumber(numbers[i][j], i, j, newGrid);
                } else if (grid.getGrid()[i][j] != numbers[i][j]) {
                    // it's a new number
                    if (!newGrid.checkInput(i, j)) {
                        // number temporary saved
                        temporaryCells[i][j] = numbers[i][j];
                        removedCells[i][j] = true;
                        //feedback
                        numberOfLines++;
                        outputText = OutputMessages.builder(outputText, newGrid.getOutputMessage());
                    } else {
                        setNewNumber(i, j, numbers[i][j]);
                    }
                }
            }
        }
        update();
    }

    private void setNewNumber(int column, int row, int newNumber) {
        newCells[column][row] = true;
        for (int x = 1; x <= grid.getGridSize(); x++) {
            if (!mapOfCells.get(x).isEmpty()) {
                for (int y = 0; y < mapOfCells.get(x).size(); y++) {
                    Cell item = mapOfCells.get(x).get(y);
                    if (item.getColumn() == column && item.getRow() == row) {
                        mapOfCells.get(x).remove(item);
                        item.reducePossibleNumbers(grid.getNewPossibleNumbers(
                                item.getAllPossibleNumbers(), item.getColumn(), item.getRow()));
                        grid.setNumber(item.removeNumber(newNumber), item.getColumn(), item.getRow());
                        solvedCells.add(item);
                        newCells[item.getColumn()][item.getRow()] = true;

                        // feedback
                        numberOfLines++;
                        outputText = OutputMessages.valideNumberWithMoreOptions(newNumber, column, row,
                                item.getPossibleNumbers(), outputText);
                        return;
                    }
                }
            }
        }
    }

    private boolean checkBlockRowColumn() {
        for (int x = 1; x <= grid.getGridSize(); x++) {
            if (!mapOfCells.get(x).isEmpty()) {
                for (int y = 0; y < mapOfCells.get(x).size(); y++) {
                    Cell item = mapOfCells.get(x).get(y);
                    // search for distinct Cell in block
                    int check = checkBlockNumber(item);
                    if (check == 0) {
                        // search for distinct Cell in Row
                        check = checkRowNumber(item);
                    }
                    if (check == 0) {
                        // search for distinct Cell in block
                        check = checkColumnNumber(item);
                    }
                    if (check != 0) {
                        // found distinct Cell
                        grid.setNumber(check, item.getColumn(), item.getRow());
                        item.clearPossibleNumbers();
                        mapOfCells.get(x).remove(item);
                        solvedCells.add(item);
                        newCells[item.getColumn()][item.getRow()] = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int checkBlockNumber(Cell item) {
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];

        boolean[] blockNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : item.getPossibleNumbers()) {
            blockNumbers[numberOne] = true;
        }

        int blockSize = (int) Math.sqrt(grid.getGridSize());
        int localRowOne = item.getColumn() - item.getColumn() % blockSize;
        int localColumnOne = item.getRow() - item.getRow() % blockSize;

        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                for (int j = 0; j < mapOfCells.get(i).size(); j++) {
                    Cell itemTwo = mapOfCells.get(i).get(j);
                    int localRowTwo = itemTwo.getColumn() - itemTwo.getColumn() % blockSize;
                    int localColumnTwo = itemTwo.getRow() - itemTwo.getRow() % blockSize;
                    if (localColumnOne == localColumnTwo && localRowOne == localRowTwo
                            && !(item.getRow() == itemTwo.getRow() && item.getColumn() == itemTwo.getColumn())) {
                        // Cell is in the same block
                        indexCells[itemTwo.getColumn()][itemTwo.getRow()] = true;
                        for (int numberTwo : itemTwo.getPossibleNumbers()) {
                            blockNumbers[numberTwo] = false;
                        }
                    }
                }
            }
        }
        int count = 0;
        int result = 0;
        // count possible numbers
        for (int i = 0; i < blockNumbers.length; i++) {
            if (blockNumbers[i]) {
                count++;
                result = i;
            }
        }
        // if more than one number is left return 0
        if (count == 1) {
            outputText = OutputMessages.setBlock(result, item.getColumn(), item.getRow());
            return result;
        } else {
            return 0;
        }
    }

    private int checkRowNumber(Cell item) {
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];

        boolean[] rowNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : item.getPossibleNumbers()) {
            rowNumbers[numberOne] = true;
        }

        // check all leftover numbers
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                for (int k = 0; k < mapOfCells.get(i).size(); k++) {
                    Cell itemTwo = mapOfCells.get(i).get(k);
                    // check if the item is in the same row
                    if (item.getColumn() == itemTwo.getColumn() && item.getRow() != itemTwo.getRow()) {
                        // if the item is in the same row set all equal numbers false
                        indexCells[itemTwo.getColumn()][itemTwo.getRow()] = true;
                        for (int numberTwo : itemTwo.getPossibleNumbers()) {
                            rowNumbers[numberTwo] = false;
                        }
                    }
                }
            }
        }
        int count = 0;
        int result = 0;
        // count possible numbers
        for (int i = 0; i < rowNumbers.length; i++) {
            if (rowNumbers[i]) {
                count++;
                result = i;
            }
        }
        // if more than one number is left return 0
        if (count == 1) {
            outputText = OutputMessages.setRow(result, item.getRow());
            return result;
        } else {
            return 0;
        }
    }

    private int checkColumnNumber(Cell item) {
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        boolean[] blockNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : item.getPossibleNumbers()) {
            blockNumbers[numberOne] = true;
        }
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                for (int k = 0; k < mapOfCells.get(i).size(); k++) {
                    Cell itemTwo = mapOfCells.get(i).get(k);
                    if (item.getRow() == itemTwo.getRow() && item.getColumn() != itemTwo.getColumn()) {
                        indexCells[itemTwo.getColumn()][itemTwo.getRow()] = true;
                        for (int numberTwo : itemTwo.getPossibleNumbers()) {
                            blockNumbers[numberTwo] = false;
                        }
                    }
                }
            }
        }
        int count = 0;
        int result = 0;
        for (int i = 0; i < blockNumbers.length; i++) {
            if (blockNumbers[i]) {
                count++;
                result = i;
            }
        }
        if (count == 1) {
            outputText = OutputMessages.setColumn(result, item.getColumn());
            return result;
        } else {
            return 0;
        }
    }

    private boolean setDistinctNumber() {
        if (!mapOfCells.get(1).isEmpty()) {
            while (!mapOfCells.get(1).isEmpty()) {
                Cell item = mapOfCells.get(1).get(0);
                mapOfCells.get(1).remove(item);
                solvedCells.add(item);
                int newNumber = item.getNextNumber();
                grid.setNumber(newNumber, item.getColumn(), item.getRow());
                newCells[item.getColumn()][item.getRow()] = true;
                numberOfLines++;
                outputText = OutputMessages.setDistinct(newNumber, item.getColumn(), item.getRow(), outputText);
            }
            return true;
        }
        return false;
    }

    /**
     *
     */
    private void setNextNumber() {
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        for (int i = 2; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                Cell item = mapOfCells.get(i).get(0);
                mapOfCells.get(i).remove(item);
                grid.setNumber(item.getNextNumber(), item.getColumn(), item.getRow());
                solvedCells.add(item);
                newCells[item.getColumn()][item.getRow()] = true;
                outputText = OutputMessages.noDistinctNumber(item.getPossibleNumbers());
                return;
            }
        }
    }

    /**
     *
     */
    private void backTracking() {
        newCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        boolean count = false;
        Cell item = solvedCells.get(solvedCells.size() - 1);
        // remove items until a Cell with a different possible number
        while (item.possibleNumbersIsEmpty()) {
            grid.setNumber(0, item.getColumn(), item.getRow());
            solvedCells.remove(item);
            item.resetPossibleNumbers();
            mapOfCells.get(item.getNumberOfPossibleNumbers()).add(item);
            removedCells[item.getColumn()][item.getRow()] = true;
            if (solvedCells.isEmpty()) {
                // not solvable
                isSolvable = false;
                cellsSearching = 0;
                return;
            }
            item = solvedCells.get(solvedCells.size() - 1);
            count = true;
        }
        int newNumber = item.getNextNumber();
        grid.setNumber(newNumber, item.getColumn(), item.getRow());

        // For View
        removedCells[item.getColumn()][item.getRow()] = true;
        outputText = OutputMessages.backTracking(count);
    }

    /**
     * @param newNumber
     * @param column
     * @param row
     */
    private void deleteOrChangeNumber(int newNumber, int column, int row, Grid newGrid) {
        boolean itemFound = false;
        for (Cell item : solvedCells) {
            if (item.getColumn() == column && item.getRow() == row) {
                int oldNumber = grid.getGrid()[item.getColumn()][item.getRow()];
                if (newNumber == 0) {
                    // delete old number
                    solvedCells.remove(item);
                    item.resetPossibleNumbers();
                    grid.setNumber(newNumber, column, row);
                    mapOfCells.get(item.getNumberOfPossibleNumbers()).add(item);
                    numberOfLines++;
                    outputText = OutputMessages.removeNumber(oldNumber, item.getColumn(), item.getRow(), outputText);
                    return;
                } else {
                    if (newGrid.checkNumberIsValid(newNumber, column, row)) {
                        // new number is valid
                        item.reducePossibleNumbers(newGrid.getNewPossibleNumbers(item.getAllPossibleNumbers(),
                                item.getColumn(), item.getRow()));
                        item.removeNumber(newNumber);
                        grid.setNumber(newNumber, column, row);
                        numberOfLines++;
                        outputText = OutputMessages.valideNumberWithMoreOptions(
                                newNumber, item.getColumn(), item.getRow(), item.getPossibleNumbers(), outputText);
                        itemFound = true;
                    } else {
                        // new number is not valid
                        numberOfLines++;
                        outputText = OutputMessages.newNumberNotAllowed(newNumber, column, row,
                                item.getPossibleNumbers(), outputText);
                        removedCells[item.getColumn()][item.getRow()] = true;
                        return;
                    }
                }
            } else if (itemFound) {
                // reset numbers after changed number
                item.reducePossibleNumbers(newGrid.getNewPossibleNumbers(item.getAllPossibleNumbers(),
                        item.getColumn(), item.getRow()));
            }
        }
    }

    /**
     *
     */
    private boolean sortMap() {
        Cell item;
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                for (int j = 0; j < mapOfCells.get(i).size(); j++) {
                    item = mapOfCells.get(i).get(j);
                    item.reducePossibleNumbers(grid.getNewPossibleNumbers(item.getAllPossibleNumbers(),
                            item.getColumn(), item.getRow()));
                    if (item.possibleNumbersIsEmpty()) {
                        // backtracking necessary
                        mapOfCells.get(i).remove(item);
                        item.resetPossibleNumbers();
                        mapOfCells.get(item.getNumberOfPossibleNumbers()).add(item);
                        backTracking();
                        return true;
                    }
                    if (i != item.getNumberOfPossibleNumbers()) {
                        // possible numbers reduced
                        mapOfCells.get(i).remove(item);
                        j--;
                        mapOfCells.get(item.getNumberOfPossibleNumbers()).add(item);
                    }
                    if (item.getNumberOfPossibleNumbers() == 1) {
                        // new distinct Cell, no need to sort more
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     */
    private void createMap() {
        solvedCells.clear();
        cellsSearching = 0;
        for (int i = 0; i <= grid.getGridSize(); i++) {
            mapOfCells.put(i, new ArrayList<>());
        }
        for (int i = 0; i < grid.getGridSize(); i++) {
            for (int j = 0; j < grid.getGridSize(); j++) {
                if (grid.getGrid()[i][j] == 0) {
                    List<Integer> possibleNumbers = grid.getPossibleNumbers(i, j);
                    Cell item = new Cell(i, j, possibleNumbers);
                    mapOfCells.get(possibleNumbers.size()).add(item);
                    cellsSearching++;
                }
            }
        }
    }

    /**
     *
     */
    private void setupOutput() {
        numberOfLines = 0;
        outputText = "";
        newCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        removedCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        temporaryCells = new int[grid.getGridSize()][grid.getGridSize()];
    }
}