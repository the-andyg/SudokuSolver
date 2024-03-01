package com.model;

import com.Data.OutputMessages;
import com.controller.SudokuController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SudokuSolver {
    private Grid grid;
    private final HashMap<Integer, List<MapItem>> map;
    private final List<MapItem> solvedItems;
    private int numbersSearching;
    private String outputText;
    private boolean[][] newFields;
    private boolean[][] removedFields;
    private boolean[][] indexFields;
    private int [][] temporaryNumbers;
    private int numberOfLines;
    private boolean isSolvable;
    private final SudokuController sudokuController;

    public SudokuSolver(Grid grid, SudokuController sudokuController) {
        this.grid = grid;
        this.sudokuController = sudokuController;
        map = new HashMap<>();
        solvedItems = new ArrayList<>();
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

    public boolean[][] getNewFields() {
        return newFields;
    }

    public boolean[][] getRemovedFieldsFields() {
        return removedFields;
    }

    public boolean[][] getIndexFields() {
        return indexFields;
    }

    public boolean isNotDone() {
        return !(solvedItems.size() == numbersSearching);
    }

    public int[][] getTemporaryNumbers() {
        return temporaryNumbers;
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
        // check if there is a distinct Field
        if (setDistinctNumber()) {
            if (!isLoop) {
                update();
            }
            return;
        }
        // check if there is a distinct Field in Row, Column or Block
        if (checkBlockRowColumn()) {
            if (!isLoop) {
                update();
            }
            return;
        }
        // set a field with multiple opportunities
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
                    // old field had already a number
                    deleteOrChangeNumber(numbers[i][j], i, j, newGrid);
                } else if (grid.getGrid()[i][j] != numbers[i][j]) {
                    // it's a new number
                    if (!newGrid.checkInput(i, j)) {
                        // number temporary saved
                        temporaryNumbers[i][j] = numbers[i][j];
                        removedFields[i][j] = true;
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

    public void solveSudoku() {
        while (isNotDone()) {
            nextNumber(true);
        }
        nextNumber(true);
        update();
    }

    private void setNewNumber(int column, int row, int newNumber) {
        newFields[column][row] = true;
        for (int x = 1; x <= grid.getGridSize(); x++) {
            if (!map.get(x).isEmpty()) {
                for (int y = 0; y < map.get(x).size(); y++) {
                    MapItem item = map.get(x).get(y);
                    if (item.getColumn() == column && item.getRow() == row) {
                        map.get(x).remove(item);
                        item.reducePossibleNumbers(grid.getNewPossibleNumbers(
                                item.getAllPossibleNumbers(), item.getColumn(), item.getRow()));
                        grid.setNumber(item.removeNumber(newNumber), item.getColumn(), item.getRow());
                        solvedItems.add(item);
                        newFields[item.getColumn()][item.getRow()] = true;

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
            if (!map.get(x).isEmpty()) {
                for (int y = 0; y < map.get(x).size(); y++) {
                    MapItem item = map.get(x).get(y);
                    // search for distinct field in block
                    int check = checkBlockNumber(item);
                    if (check == 0) {
                        // search for distinct field in Row
                        check = checkRowNumber(item);
                    }
                    if (check == 0) {
                        // search for distinct field in block
                        check = checkColumnNumber(item);
                    }
                    if (check != 0) {
                        // found distinct field
                        grid.setNumber(check, item.getColumn(), item.getRow());
                        item.clearPossibleNumbers();
                        map.get(x).remove(item);
                        solvedItems.add(item);
                        newFields[item.getColumn()][item.getRow()] = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int checkBlockNumber(MapItem item) {
        indexFields = new boolean[grid.getGridSize()][grid.getGridSize()];

        boolean[] blockNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : item.getPossibleNumbers()) {
            blockNumbers[numberOne] = true;
        }

        int blockSize = (int) Math.sqrt(grid.getGridSize());
        int localRowOne = item.getColumn() - item.getColumn() % blockSize;
        int localColumnOne = item.getRow() - item.getRow() % blockSize;

        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int j = 0; j < map.get(i).size(); j++) {
                    MapItem itemTwo = map.get(i).get(j);
                    int localRowTwo = itemTwo.getColumn() - itemTwo.getColumn() % blockSize;
                    int localColumnTwo = itemTwo.getRow() - itemTwo.getRow() % blockSize;
                    if (localColumnOne == localColumnTwo && localRowOne == localRowTwo
                            && !(item.getRow() == itemTwo.getRow() && item.getColumn() == itemTwo.getColumn())) {
                        // field is in the same block
                        indexFields[itemTwo.getColumn()][itemTwo.getRow()] = true;
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

    private int checkRowNumber(MapItem item) {
        indexFields = new boolean[grid.getGridSize()][grid.getGridSize()];

        boolean[] rowNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : item.getPossibleNumbers()) {
            rowNumbers[numberOne] = true;
        }

        // check all leftover numbers
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int k = 0; k < map.get(i).size(); k++) {
                    MapItem itemTwo = map.get(i).get(k);
                    // check if the item is in the same row
                    if (item.getColumn() == itemTwo.getColumn() && item.getRow() != itemTwo.getRow()) {
                        // if the item is in the same row set all equal numbers false
                        indexFields[itemTwo.getColumn()][itemTwo.getRow()] = true;
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

    private int checkColumnNumber(MapItem item) {
        indexFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        boolean[] blockNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : item.getPossibleNumbers()) {
            blockNumbers[numberOne] = true;
        }
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int k = 0; k < map.get(i).size(); k++) {
                    MapItem itemTwo = map.get(i).get(k);
                    if (item.getRow() == itemTwo.getRow() && item.getColumn() != itemTwo.getColumn()) {
                        indexFields[itemTwo.getColumn()][itemTwo.getRow()] = true;
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
        if (!map.get(1).isEmpty()) {
            while (!map.get(1).isEmpty()) {
                MapItem item = map.get(1).get(0);
                map.get(1).remove(item);
                solvedItems.add(item);
                int newNumber = item.getNextNumber();
                grid.setNumber(newNumber, item.getColumn(), item.getRow());
                newFields[item.getColumn()][item.getRow()] = true;
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
        indexFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        for (int i = 2; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                MapItem item = map.get(i).get(0);
                map.get(i).remove(item);
                grid.setNumber(item.getNextNumber(), item.getColumn(), item.getRow());
                solvedItems.add(item);
                newFields[item.getColumn()][item.getRow()] = true;
                outputText = OutputMessages.noDistinctNumber(item.getPossibleNumbers());
                return;
            }
        }
    }

    /**
     *
     */
    private void backTracking() {
        newFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        boolean count = false;
        MapItem item = solvedItems.get(solvedItems.size() - 1);
        // remove items until a field with a different possible number
        while (item.possibleNumbersIsEmpty()) {
            grid.setNumber(0, item.getColumn(), item.getRow());
            solvedItems.remove(item);
            item.restorePossibleNumbers();
            map.get(item.getNumberOfPossibleNumbers()).add(item);
            removedFields[item.getColumn()][item.getRow()] = true;
            if (solvedItems.isEmpty()) {
                // not solvable
                isSolvable = false;
                numbersSearching = 0;
                return;
            }
            item = solvedItems.get(solvedItems.size() - 1);
            count = true;
        }
        int newNumber = item.getNextNumber();
        grid.setNumber(newNumber, item.getColumn(), item.getRow());

        // For View
        removedFields[item.getColumn()][item.getRow()] = true;
        outputText = OutputMessages.backTracking(count);
    }

    /**
     * @param newNumber
     * @param column
     * @param row
     */
    private void deleteOrChangeNumber(int newNumber, int column, int row, Grid newGrid) {
        boolean itemFound = false;
        for (MapItem item : solvedItems) {
            if (item.getColumn() == column && item.getRow() == row) {
                int oldNumber = grid.getGrid()[item.getColumn()][item.getRow()];
                if (newNumber == 0) {
                    // delete old number
                    solvedItems.remove(item);
                    item.restorePossibleNumbers();
                    grid.setNumber(newNumber, column, row);
                    map.get(item.getNumberOfPossibleNumbers()).add(item);
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
                        removedFields[item.getColumn()][item.getRow()] = true;
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
        MapItem item;
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int j = 0; j < map.get(i).size(); j++) {
                    item = map.get(i).get(j);
                    item.reducePossibleNumbers(grid.getNewPossibleNumbers(item.getAllPossibleNumbers(),
                            item.getColumn(), item.getRow()));
                    if (item.possibleNumbersIsEmpty()) {
                        // backtracking necessary
                        map.get(i).remove(item);
                        item.restorePossibleNumbers();
                        map.get(item.getNumberOfPossibleNumbers()).add(item);
                        backTracking();
                        return true;
                    }
                    if (i != item.getNumberOfPossibleNumbers()) {
                        // possible numbers reduced
                        map.get(i).remove(item);
                        j--;
                        map.get(item.getNumberOfPossibleNumbers()).add(item);
                    }
                    if (item.getNumberOfPossibleNumbers() == 1) {
                        // new distinct field, no need to sort more
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
        solvedItems.clear();
        numbersSearching = 0;
        for (int i = 0; i <= grid.getGridSize(); i++) {
            map.put(i, new ArrayList<>());
        }
        for (int i = 0; i < grid.getGridSize(); i++) {
            for (int j = 0; j < grid.getGridSize(); j++) {
                if (grid.getGrid()[i][j] == 0) {
                    List<Integer> possibleNumbers = grid.getPossibleNumbers(i, j);
                    MapItem item = new MapItem(i, j, possibleNumbers);
                    map.get(possibleNumbers.size()).add(item);
                    numbersSearching++;
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
        newFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        removedFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        indexFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        temporaryNumbers = new int[grid.getGridSize()][grid.getGridSize()];
    }
}