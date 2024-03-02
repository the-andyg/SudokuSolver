package com.model;

import com.Data.Feedback;
import com.controller.SudokuController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SudokuSolver {
    // logic
    private Grid grid;
    private final HashMap<Integer, List<Cell>> mapOfCells;
    private final List<Cell> solvedCells;
    private boolean hasSolucion;
    private int cellsSearching;
    // logic
    // Feedback
    private final SudokuController sudokuController;
    private boolean[][] newCells;
    private boolean[][] removedCells;
    private boolean[][] indexCells;
    private int[][] temporaryCells;
    private int numberOfLines;
    private String feedback;
    // Feedback

    public SudokuSolver(Grid grid, SudokuController sudokuController) {
        this.grid = grid;
        this.sudokuController = sudokuController;
        mapOfCells = new HashMap<>();
        solvedCells = new ArrayList<>();
        hasSolucion = true;
        createMap();
        setupFeedbackElements();
        checkGrid();
    }

    private void update() {
        sudokuController.update(grid.getGridSize(), feedback, grid, grid.isSolveAble());
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
            feedback = Feedback.INIT_TEXT;
        } else {
            feedback = Feedback.SET_TEXT;
        }
        update();
    }

    public void checkNewGrid(int[][] newGrid) {
        grid.clearSudoku();
        setupFeedbackElements();
        Grid grid = new Grid(newGrid, this.grid.getGridSize());
        if (grid.isSolveAble()) {
            setupFeedbackElements();
            feedback = Feedback.SET_TEXT;
            this.grid = grid;
            createMap();
        } else {
            feedback = grid.getFeedback();
            this.grid = grid;
        }
        update();
    }

    public void clearSudoku() {
        grid.clearSudoku();
        createMap();
        setupFeedbackElements();
        feedback = Feedback.CLEAR_SUDOKU;
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
        setupFeedbackElements();
        if (sortMap()) {
            // backtracking was necessary
            if (!isLoop) {
                update();
            }
            return;
        }
        // check if Sudoku is done
        if (!isNotDone()) {
            if (!hasSolucion) {
                feedback = Feedback.SUDOKU_NOT_SOLVABLE;
            } else {
                feedback = Feedback.SUDOKU_SOLVED;
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
        setupFeedbackElements();
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
                        feedback = Feedback.builder(feedback, newGrid.getFeedback());
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
                    Cell cell = mapOfCells.get(x).get(y);
                    if (cell.getColumn() == column && cell.getRow() == row) {
                        mapOfCells.get(x).remove(cell);
                        cell.reducePossibleNumbers(grid.getNewPossibleNumbers(
                                cell.getAllPossibleNumbers(), cell.getColumn(), cell.getRow()));
                        grid.setNumber(cell.removeNumber(newNumber), cell.getColumn(), cell.getRow());
                        solvedCells.add(cell);
                        newCells[cell.getColumn()][cell.getRow()] = true;

                        // feedback
                        numberOfLines++;
                        feedback = Feedback.valideNumberWithMoreOptions(newNumber, column, row,
                                cell.getPossibleNumbers(), feedback);
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
                    Cell cell = mapOfCells.get(x).get(y);
                    // search for distinct Cell in block
                    int check = checkBlockNumber(cell);
                    if (check == 0) {
                        // search for distinct Cell in Row
                        check = checkRowNumber(cell);
                    }
                    if (check == 0) {
                        // search for distinct Cell in block
                        check = checkColumnNumber(cell);
                    }
                    if (check != 0) {
                        // found distinct Cell
                        grid.setNumber(check, cell.getColumn(), cell.getRow());
                        cell.clearPossibleNumbers();
                        mapOfCells.get(x).remove(cell);
                        solvedCells.add(cell);
                        newCells[cell.getColumn()][cell.getRow()] = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int checkBlockNumber(Cell cell) {
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];

        boolean[] blockNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : cell.getPossibleNumbers()) {
            blockNumbers[numberOne] = true;
        }

        int blockSize = (int) Math.sqrt(grid.getGridSize());
        int localRowOne = cell.getColumn() - cell.getColumn() % blockSize;
        int localColumnOne = cell.getRow() - cell.getRow() % blockSize;

        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                for (int j = 0; j < mapOfCells.get(i).size(); j++) {
                    Cell cellTwo = mapOfCells.get(i).get(j);
                    int localRowTwo = cellTwo.getColumn() - cellTwo.getColumn() % blockSize;
                    int localColumnTwo = cellTwo.getRow() - cellTwo.getRow() % blockSize;
                    if (localColumnOne == localColumnTwo && localRowOne == localRowTwo
                            && !(cell.getRow() == cellTwo.getRow() && cell.getColumn() == cellTwo.getColumn())) {
                        // Cell is in the same block
                        indexCells[cellTwo.getColumn()][cellTwo.getRow()] = true;
                        for (int numberTwo : cellTwo.getPossibleNumbers()) {
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
            feedback = Feedback.setBlock(result, cell.getColumn(), cell.getRow());
            return result;
        } else {
            return 0;
        }
    }

    private int checkRowNumber(Cell cell) {
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];

        boolean[] rowNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : cell.getPossibleNumbers()) {
            rowNumbers[numberOne] = true;
        }

        // check all leftover numbers
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                for (int k = 0; k < mapOfCells.get(i).size(); k++) {
                    Cell cellTwo = mapOfCells.get(i).get(k);
                    // check if the cell is in the same row
                    if (cell.getColumn() == cellTwo.getColumn() && cell.getRow() != cellTwo.getRow()) {
                        // if the cell is in the same row set all equal numbers false
                        indexCells[cellTwo.getColumn()][cellTwo.getRow()] = true;
                        for (int numberTwo : cellTwo.getPossibleNumbers()) {
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
            feedback = Feedback.setRow(result, cell.getRow());
            return result;
        } else {
            return 0;
        }
    }

    private int checkColumnNumber(Cell cell) {
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        boolean[] blockNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : cell.getPossibleNumbers()) {
            blockNumbers[numberOne] = true;
        }
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                for (int k = 0; k < mapOfCells.get(i).size(); k++) {
                    Cell cellTwo = mapOfCells.get(i).get(k);
                    if (cell.getRow() == cellTwo.getRow() && cell.getColumn() != cellTwo.getColumn()) {
                        indexCells[cellTwo.getColumn()][cellTwo.getRow()] = true;
                        for (int numberTwo : cellTwo.getPossibleNumbers()) {
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
            feedback = Feedback.setColumn(result, cell.getColumn());
            return result;
        } else {
            return 0;
        }
    }

    private boolean setDistinctNumber() {
        if (!mapOfCells.get(1).isEmpty()) {
            while (!mapOfCells.get(1).isEmpty()) {
                Cell cell = mapOfCells.get(1).get(0);
                mapOfCells.get(1).remove(cell);
                solvedCells.add(cell);
                int newNumber = cell.getNextNumber();
                grid.setNumber(newNumber, cell.getColumn(), cell.getRow());
                newCells[cell.getColumn()][cell.getRow()] = true;
                numberOfLines++;
                feedback = Feedback.setDistinct(newNumber, cell.getColumn(), cell.getRow(), feedback);
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
                Cell cell = mapOfCells.get(i).get(0);
                mapOfCells.get(i).remove(cell);
                grid.setNumber(cell.getNextNumber(), cell.getColumn(), cell.getRow());
                solvedCells.add(cell);
                newCells[cell.getColumn()][cell.getRow()] = true;
                feedback = Feedback.noDistinctNumber(cell.getPossibleNumbers());
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
        Cell cell = solvedCells.get(solvedCells.size() - 1);
        // remove cells until a Cell with a different possible number
        while (cell.possibleNumbersIsEmpty()) {
            grid.setNumber(0, cell.getColumn(), cell.getRow());
            solvedCells.remove(cell);
            cell.resetPossibleNumbers();
            mapOfCells.get(cell.getNumberOfPossibleNumbers()).add(cell);
            removedCells[cell.getColumn()][cell.getRow()] = true;
            if (solvedCells.isEmpty()) {
                // not solvable
                hasSolucion = false;
                cellsSearching = 0;
                return;
            }
            cell = solvedCells.get(solvedCells.size() - 1);
            count = true;
        }
        int newNumber = cell.getNextNumber();
        grid.setNumber(newNumber, cell.getColumn(), cell.getRow());

        // For View
        removedCells[cell.getColumn()][cell.getRow()] = true;
        feedback = Feedback.backTracking(count);
    }

    /**
     * @param newNumber
     * @param column
     * @param row
     */
    private void deleteOrChangeNumber(int newNumber, int column, int row, Grid newGrid) {
        boolean cellFound = false;
        for (Cell cell : solvedCells) {
            if (cell.getColumn() == column && cell.getRow() == row) {
                int oldNumber = grid.getGrid()[cell.getColumn()][cell.getRow()];
                if (newNumber == 0) {
                    // delete old number
                    solvedCells.remove(cell);
                    cell.resetPossibleNumbers();
                    grid.setNumber(newNumber, column, row);
                    mapOfCells.get(cell.getNumberOfPossibleNumbers()).add(cell);
                    numberOfLines++;
                    feedback = Feedback.removeNumber(oldNumber, cell.getColumn(), cell.getRow(), feedback);
                    return;
                } else {
                    if (newGrid.checkNumberIsValid(newNumber, column, row)) {
                        // new number is valid
                        cell.reducePossibleNumbers(newGrid.getNewPossibleNumbers(cell.getAllPossibleNumbers(),
                                cell.getColumn(), cell.getRow()));
                        cell.removeNumber(newNumber);
                        grid.setNumber(newNumber, column, row);
                        numberOfLines++;
                        feedback = Feedback.valideNumberWithMoreOptions(
                                newNumber, cell.getColumn(), cell.getRow(), cell.getPossibleNumbers(), feedback);
                        cellFound = true;
                    } else {
                        // new number is not valid
                        numberOfLines++;
                        feedback = Feedback.newNumberNotAllowed(newNumber, column, row,
                                cell.getPossibleNumbers(), feedback);
                        removedCells[cell.getColumn()][cell.getRow()] = true;
                        return;
                    }
                }
            } else if (cellFound) {
                // reset numbers after changed number
                cell.reducePossibleNumbers(newGrid.getNewPossibleNumbers(cell.getAllPossibleNumbers(),
                        cell.getColumn(), cell.getRow()));
            }
        }
    }

    /**
     *
     */
    private boolean sortMap() {
        Cell cell;
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!mapOfCells.get(i).isEmpty()) {
                for (int j = 0; j < mapOfCells.get(i).size(); j++) {
                    cell = mapOfCells.get(i).get(j);
                    cell.reducePossibleNumbers(grid.getNewPossibleNumbers(cell.getAllPossibleNumbers(),
                            cell.getColumn(), cell.getRow()));
                    if (cell.possibleNumbersIsEmpty()) {
                        // backtracking necessary
                        mapOfCells.get(i).remove(cell);
                        cell.resetPossibleNumbers();
                        mapOfCells.get(cell.getNumberOfPossibleNumbers()).add(cell);
                        backTracking();
                        return true;
                    }
                    if (i != cell.getNumberOfPossibleNumbers()) {
                        // possible numbers reduced
                        mapOfCells.get(i).remove(cell);
                        j--;
                        mapOfCells.get(cell.getNumberOfPossibleNumbers()).add(cell);
                    }
                    if (cell.getNumberOfPossibleNumbers() == 1) {
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
                    Cell cell = new Cell(i, j, possibleNumbers);
                    mapOfCells.get(possibleNumbers.size()).add(cell);
                    cellsSearching++;
                }
            }
        }
    }

    /**
     *
     */
    private void setupFeedbackElements() {
        numberOfLines = 0;
        feedback = "";
        newCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        removedCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        indexCells = new boolean[grid.getGridSize()][grid.getGridSize()];
        temporaryCells = new int[grid.getGridSize()][grid.getGridSize()];
    }
}