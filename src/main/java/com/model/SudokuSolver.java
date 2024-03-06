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
    private boolean hasSolution;
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
        hasSolution = true;
        createMap();
        setupFeedbackElements();
        checkGrid();
    }

    private void update() {
        sudokuController.update(grid.getGridSize(), feedback, grid, !grid.isSolvable());
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

    /**
     * Checks the validity of the current Sudoku grid inputs and updates the feedback accordingly.
     * If the grid is not solvable, sets the feedback to indicate initialization text;
     * otherwise, sets it to indicate set text.
     * Finally, updates the user interface based on the new feedback.
     */
    private void checkGrid() {
        grid.checkInputs(true);
        if (!grid.isSolvable()) {
            feedback = Feedback.INIT_TEXT;
        } else {
            feedback = Feedback.SET_TEXT;
        }
        update();
    }

    /**
     * Checks the validity of a new Sudoku grid provided as input.
     * Clears the current Sudoku grid and sets up the feedback elements.
     * Creates a new grid based on the input and checks if it is solvable.
     * If solvable, sets up the feedback elements, updates the feedback to indicate set text,
     * updates the current grid to the new one, and creates a map.
     * If not solvable, updates the feedback based on the grid's feedback and updates the current grid.
     * Finally, updates the user interface based on the new feedback.
     *
     * @param newGrid The new Sudoku grid to be checked.
     */
    public void checkNewGrid(int[][] newGrid) {
        clearSudoku();
        setupFeedbackElements();
        Grid grid = new Grid(newGrid, this.grid.getGridSize());
        if (grid.isSolvable()) {
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

    /**
     * Clears the current Sudoku grid.
     * Clears the Sudoku grid, creates a new map, sets up the feedback elements,
     * updates the feedback to indicate that the Sudoku has been cleared,
     * and updates the user interface.
     */
    public void clearSudoku() {
        grid.clearSudoku();
        hasSolution = true;
        createMap();
        setupFeedbackElements();
        feedback = Feedback.CLEAR_SUDOKU;
        update();
    }

    /**
     * Solves the Sudoku puzzle.
     * Continuously calls the nextNumber method until the puzzle is solved.
     * After solving the puzzle, it updates the user interface.
     */
    public void solveSudoku() {
        final long timeStart = System.currentTimeMillis();
        while (isNotDone()) {
            nextNumber(true);
            if (System.currentTimeMillis() - timeStart > 3000) {
                cellsSearching = solvedCells.size();
                hasSolution = false;
                break;
            }
        }
        nextNumber(true);
        update();
    }

    /**
     * Determines the next number to be placed in the Sudoku grid.
     * If the backtracking algorithm is necessary, it performs backtracking.
     * Checks if the Sudoku puzzle is solved or not solvable.
     * Sets a distinct number if available in a cell.
     * Checks if there is a distinct cell in a row, column, or block.
     * Sets the next number in a cell with multiple possibilities.
     * If the isLoop parameter is false, updates the user interface after each step.
     *
     * @param isLoop a boolean indicating whether the method should be run in a loop
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
            if (!hasSolution) {
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

    /**
     * Checks the validity of new numbers inputted by the user and updates the Sudoku grid accordingly.
     * Sorts the map of available numbers.
     * Sets up feedback elements for the user interface.
     * Creates a new grid with the inputted numbers.
     * Iterates over each cell in the grid to compare the inputted numbers with the existing ones.
     * If a cell had a number previously and it's different from the new input, deletes or changes the number accordingly.
     * If it's a new number, checks if it's valid and updates the grid with the new number.
     * Updates the user interface with the changes.
     *
     * @param numbers a 2D array representing the new numbers inputted by the user
     */
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

    /**
     * Sets a new number in the Sudoku grid and updates the corresponding data structures.
     * Marks the cell containing the new number as a newly filled cell.
     * Iterates through the map of cells and removes the cell containing the new number.
     * Updates the possible numbers for the removed cell based on the newly filled number.
     * Sets the new number in the grid and marks the cell as solved.
     * Adds the solved cell to the list of solved cells.
     * Updates the user interface with the valid number placement and additional options feedback.
     *
     * @param column    the column index of the cell containing the new number
     * @param row       the row index of the cell containing the new number
     * @param newNumber the new number to be set in the Sudoku grid
     */
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

    /**
     * Checks for cells in the Sudoku grid that have unique numbers within their block, row, or column.
     * Iterates through the map of cells to find each cell.
     * For each cell, checks for a distinct number within its block, row, or column.
     * If a distinct number is found, sets the number in the grid, clears the possible numbers for the cell,
     * removes the cell from the map of cells, adds the cell to the list of solved cells, and marks the cell as newly filled.
     *
     * @return true if a distinct number is found and set in the grid, false otherwise
     */
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

    /**
     * Checks for a distinct number within the block of the specified cell.
     * Iterates through the map of cells to find cells within the same block as the specified cell.
     * For each cell within the block, updates the indexCells array to mark the cell's position.
     * Updates the blockNumbers array based on the possible numbers of each cell within the block.
     * Counts the remaining possible numbers within the block.
     * If only one possible number remains, sets the number as the distinct number in the block and returns it.
     *
     * @param cell The cell for which to check the block
     * @return The distinct number found in the block, or 0 if no distinct number is found
     */
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

    /**
     * Checks for a distinct number within the row of the specified cell.
     * Iterates through the map of cells to find cells within the same row as the specified cell.
     * For each cell within the row, updates the indexCells array to mark the cell's position.
     * Updates the rowNumbers array based on the possible numbers of each cell within the row.
     * Counts the remaining possible numbers within the row.
     * If only one possible number remains, sets the number as the distinct number in the row and returns it.
     *
     * @param cell The cell for which to check the row
     * @return The distinct number found in the row, or 0 if no distinct number is found
     */
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

    /**
     * Checks for a distinct number within the column of the specified cell.
     * Iterates through the map of cells to find cells within the same column as the specified cell.
     * For each cell within the column, updates the indexCells array to mark the cell's position.
     * Updates the blockNumbers array based on the possible numbers of each cell within the column.
     * Counts the remaining possible numbers within the column.
     * If only one possible number remains, sets the number as the distinct number in the column and returns it.
     *
     * @param cell The cell for which to check the column
     * @return The distinct number found in the column, or 0 if no distinct number is found
     */
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

    /**
     * Sets a distinct number for cells with only one remaining possible number.
     * Iterates through the map of cells to find cells with only one possible number.
     * For each cell found, sets the cell's remaining possible number as the distinct number.
     * Updates the grid with the distinct number, marks the cell as solved, and adds it to the solvedCells list.
     * Increments the numberOfLines counter and updates the feedback message.
     * Returns true if at least one distinct number is set, otherwise returns false.
     *
     * @return True if a distinct number is set for at least one cell, otherwise false
     */
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
     * Sets the next number for cells with multiple possible numbers.
     * Iterates through the map of cells starting from cells with two possible numbers.
     * For the first cell found, sets its next possible number as the number to be filled in the grid.
     * Marks the cell as solved, updates the grid with the next number, and updates feedback accordingly.
     * Stops iterating after setting the next number for the first cell found.
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
     * Performs backtracking to resolve conflicts when a dead end is reached during Sudoku solving.
     * Removes cells with no possible numbers until a cell with a different possible number is found.
     * Resets the cell's possible numbers, updates the grid with the next number, and marks it as removed.
     * If the solvedCells list becomes empty, indicates that the Sudoku is not solvable.
     * Updates feedback accordingly to indicate backtracking.
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
                hasSolution = false;
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
     * Deletes or changes a number in the Sudoku grid at the specified column and row.
     * If the new number is 0, deletes the old number from the grid.
     * If the new number is valid, changes the old number to the new number.
     * Updates the grid, possible numbers for the cell, and feedback accordingly.
     *
     * @param newNumber The new number to be placed in the grid.
     * @param column    The column index of the cell in the grid.
     * @param row       The row index of the cell in the grid.
     * @param newGrid   The new Sudoku grid used for validation.
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
     * Sorts the map of cells based on the number of possible numbers each cell has.
     * It reduces the possible numbers for each cell and checks if backtracking is necessary.
     * If a cell has no possible numbers, backtracking is initiated.
     * If the number of possible numbers for a cell changes, it is moved to the corresponding list in the map.
     * If a cell becomes a distinct cell with only one possible number, sorting is stopped.
     *
     * @return True if backtracking is necessary, false otherwise.
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
     * Creates a map of cells based on the number of possible numbers each empty cell has.
     * It clears the list of solved cells and initializes the count of cells searching.
     * It initializes a map where the keys represent the number of possible numbers and the values are lists of cells.
     * For each empty cell in the grid, it calculates the possible numbers, creates a cell object
     * and adds it to the corresponding list in the map.
     * It also updates the count of cells searching.
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
     * Initializes or resets various feedback-related elements.
     * It resets the number of lines in the feedback, clears the feedback message,
     * and initializes or resets arrays used for tracking new cells, removed cells, index cells, and temporary cells.
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