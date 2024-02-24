package com.model;

import com.Data.OutputMessages;

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

    public SudokuSolver(Grid grid) {
        this.grid = grid;
        map = new HashMap<>();
        solvedItems = new ArrayList<>();
        numbersSearching = 0;
        createMap();
        outputText = "";
        newFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        removedFields = new boolean[grid.getGridSize()][grid.getGridSize()];
    }

    public String getOutputText() {
        return outputText;
    }

    public boolean[][] getNewFields() {
        return newFields;
    }

    public boolean[][] getRemovedFieldsFields() {
        return removedFields;
    }

    public boolean isNotDone() {
        return !(solvedItems.size() == numbersSearching);
    }

    public void nextNumber() {
        if (!isNotDone()) {
            grid.checkInputs(false);
            if (!grid.isSolveAble()) {
                outputText = OutputMessages.ALGO_FAILED;
            } else {
                outputText = OutputMessages.SUDOKU_SOLVED;
            }
            return;
        }
        newFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        removedFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        if (setDistinctNumber()) {
            outputText = "Das grüne Feld wurde / die grünen Felder wurden gesetzt, weil keine andere Zahl möglich war.";
            return;
        }
        if (checkBoxRowColumn()) {
            return;
        }
        setNextNumber();
    }

    public void checkNewNumbers(int[][] numbers) {
        newFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        removedFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        Grid newGrid = new Grid(numbers, numbers.length);
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers.length; j++) {
                if (grid.getGrid()[i][j] != 0 && grid.getGrid()[i][j] != numbers[i][j]) {
                    deleteOrChangeNumber(numbers[i][j], i, j);
                } else if (grid.getGrid()[i][j] != numbers[i][j]) {
                    newGrid.checkInput(i, j);
                    if (!newGrid.isSolveAble()) {
                        outputText = newGrid.getOutputMessage();
                        removedFields[i][j] = true;
                    } else {
                        newFields[i][j] = true;
                        for (int x = 1; x <= grid.getGridSize(); x++) {
                            if (!map.get(x).isEmpty()) {
                                for (int y = 0; y < map.get(x).size(); y++) {
                                    MapItem item = map.get(x).get(y);
                                    if (item.row == i && item.column == j) {
                                        map.get(x).remove(item);
                                        grid.setNumber(item.getNumber(numbers[i][j]), item.row, item.column);
                                        solvedItems.add(item);
                                        newFields[item.row][item.column] = true;
                                        outputText = OutputMessages.valideNumberWithMoreOptions(numbers[i][j], i, j,
                                                item.getPossibleNumbers());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        sortMap();
    }

    private boolean checkBoxRowColumn() {
        for (int x = 2; x <= grid.getGridSize(); x++) {
            if (!map.get(x).isEmpty()) {
                for (int y = 0; y < map.get(x).size(); y++) {
                    MapItem item = map.get(x).get(y);
                    int check = checkBoxNumber(item);
                    if (check == 0) {
                        check = checkRowNumber(item);
                    }
                    if (check == 0) {
                        check = checkColumnNumber(item);
                    }
                    if (check != 0) {
                        grid.setNumber(check, item.row, item.column);
                        item.clearPossibleNumbers();
                        map.get(x).remove(item);
                        solvedItems.add(item);
                        newFields[item.row][item.column] = true;
                        //System.out.println("[setBoxNumber] setBox: " + item.row + ";" + item.column);
                        sortMap();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int checkBoxNumber(MapItem item) {
        boolean[] boxNumbers = new boolean[grid.getGridSize() + 1];
        int boxSize = (int) Math.sqrt(grid.getGridSize());
        for (int numberOne : item.getPossibleNumbers()) {
            boxNumbers[numberOne] = true;
        }
        int localRowOne = item.row - item.row % boxSize;
        int localColumnOne = item.column - item.column % boxSize;
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int k = 0; k < map.get(i).size(); k++) {
                    MapItem itemTwo = map.get(i).get(k);
                    int localRowTwo = itemTwo.row - itemTwo.row % boxSize;
                    int localColumnTwo = itemTwo.column - itemTwo.column % boxSize;
                    if (localColumnOne == localColumnTwo && localRowOne == localRowTwo
                            && !(item.column == itemTwo.column && item.row == itemTwo.row)) {
                        for (int numberTwo : itemTwo.getPossibleNumbers()) {
                            boxNumbers[numberTwo] = false;
                        }
                    }
                }
            }
        }
        int count = 0;
        int result = 0;
        for (int i = 0; i < boxNumbers.length; i++) {
            if (boxNumbers[i]) {
                count++;
                result = i;
            }
        }
        if (count == 1) {
            return result;
        } else {
            return 0;
        }
    }

    private int checkRowNumber(MapItem item) {
        boolean[] boxNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : item.getPossibleNumbers()) {
            boxNumbers[numberOne] = true;
        }
        // check all leftover numbers
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int k = 0; k < map.get(i).size(); k++) {
                    MapItem itemTwo = map.get(i).get(k);
                    // check if the item is in the same row
                    if (item.row == itemTwo.row && item.column != itemTwo.column) {
                        // if the item is in the same row set all equal numbers false
                        for (int numberTwo : itemTwo.getPossibleNumbers()) {
                            boxNumbers[numberTwo] = false;
                        }
                    }
                }
            }
        }
        int count = 0;
        int result = 0;
        for (int i = 0; i < boxNumbers.length; i++) {
            // check if a number is left
            if (boxNumbers[i]) {
                count++;
                result = i;
            }
        }
        // if more than one number is left return 0 because it's not unique
        if (count == 1) {
            return result;
        } else {
            return 0;
        }
    }

    private int checkColumnNumber(MapItem item) {
        boolean[] boxNumbers = new boolean[grid.getGridSize() + 1];
        for (int numberOne : item.getPossibleNumbers()) {
            boxNumbers[numberOne] = true;
        }
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int k = 0; k < map.get(i).size(); k++) {
                    MapItem itemTwo = map.get(i).get(k);
                    if (item.column == itemTwo.column && item.row != itemTwo.row) {
                        for (int numberTwo : itemTwo.getPossibleNumbers()) {
                            boxNumbers[numberTwo] = false;
                        }
                    }
                }
            }
        }
        int count = 0;
        int result = 0;
        for (int i = 0; i < boxNumbers.length; i++) {
            if (boxNumbers[i]) {
                count++;
                result = i;
            }
        }
        if (count == 1) {
            return result;
        } else {
            return 0;
        }
    }

    private boolean setDistinctNumber() {
        if (!map.get(1).isEmpty()) {
            boolean count = map.get(1).size() > 1;
            while (!map.get(1).isEmpty()) {
                MapItem item = map.get(1).get(0);
                map.get(1).remove(item);
                solvedItems.add(item);
                int newNumber = item.getNextNumber();
                grid.setNumber(newNumber, item.row, item.column);
                //System.out.println("[SetDistinct] Eindeutige Zahl setzen: " + item.row + ";" + item.column + ":" + newNumber);
                newFields[item.row][item.column] = true;
            }
            sortMap();
            return true;
        }
        return false;
    }

    private void setNextNumber() {
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                MapItem item = map.get(i).get(0);
                //System.out.println("[SetNextNumber] Kästchen mit wenigsten Möglichkeiten finden und setzen");
                if (item.possibleNumbersIsEmpty()) {
                    //System.out.println("[SetNextNumber] Das gefundene Kästchen kann keine Zahl setzen. Gefundenes Kästchen zurück setzen");
                    outputText = "Es wurde ein Feld gefunden, bei dem keine Zahl mehr gesetzt werden kann. \n" +
                            "Das rote Feld wurde / die roten Felder wurden zurück gesetzt.";
                    backTracking();
                }
                map.get(i).remove(item);
                grid.setNumber(item.getNextNumber(), item.row, item.column);
                solvedItems.add(item);
                newFields[item.row][item.column] = true;
                sortMap();
                outputText = "Für das grüne Kästchen wurde keine eindeutige Zahl gefunden.\n" +
                        "Somit wird die geringste Zahl eingesetzt. Weitere Möglichkeiten sind: " + item.getPossibleNumbers();
                return;
            }
        }
    }

    private void backTracking() {
        for (MapItem m : solvedItems) {
            //System.out.println(m.row + ";" + m.column + ": " + m.getPossibleNumbers());
        }
        MapItem item = solvedItems.get(solvedItems.size() - 1);
        while (item.possibleNumbersIsEmpty()) {
            //System.out.println("[backTracking] eine zahl zurück gehen, weil keine weitere Möglichkeiten gefunden wurden");
            grid.setNumber(0, item.row, item.column);
            solvedItems.remove(item);
            item.restorePossibleNumbers();
            map.get(item.getNumberOfPossibleNumbers()).add(item);
            removedFields[item.row][item.column] = true;
            if (solvedItems.isEmpty()) {
                System.out.println("Sudoku nicht lösbar");
                outputText = "Sudoku nicht lösbar";
                numbersSearching = 0;
                return;
            }
            item = solvedItems.get(solvedItems.size() - 1);
        }
        //System.out.println("[backTracking] Zahl gefunden mit weiterer Option");
        int newNumber = item.getNextNumber();
        removedFields[item.row][item.column] = true;
        grid.setNumber(newNumber, item.row, item.column);
        sortMap();
    }

    private void deleteOrChangeNumber(int newNumber, int row, int column) {
        boolean found = false;
        MapItem changed = null;
        for (MapItem item : solvedItems) {
            if (item.row == row && item.column == column) {
                found = true;
                if (newNumber == 0) {
                    changed = item;
                } else {
                    if (item.getPossibleNumbers().contains(newNumber)) {
                        item.getPossibleNumbers().add(grid.getGrid()[row][column]);
                        item.getNumber(newNumber);
                        grid.setNumber(newNumber, row, column);
                    } else {
                        outputText = OutputMessages.newNumberNotAllowed(newNumber, row, column, item.getPossibleNumbers());
                        return;
                    }
                }
            }
            if (found) {
                item.restorePossibleNumbers();
            }
        }
        if (changed != null) {
            solvedItems.remove(changed);
            changed.restorePossibleNumbers();
            grid.setNumber(newNumber, row, column);
            map.get(changed.getNumberOfPossibleNumbers()).add(changed);
        }
    }

    private void sortMap() {
        //System.out.println("[sortMap] Neu sortieren");
        MapItem item;
        for (int i = 2; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int j = 0; j < map.get(i).size(); j++) {
                    item = map.get(i).get(j);
                    item.reducePossibleNumbers(grid.getNewPossibleNumbers(item.getAllPossibleNumbers(), item.row, item.column));
                    if (item.possibleNumbersIsEmpty()) {
                        //System.out.println("[sortMap] ein feld ohne Möglichkeiten Backtracking starten" + item.row + ";" + item.column);
                        map.get(i).remove(item);
                        item.restorePossibleNumbers();
                        map.get(item.getNumberOfPossibleNumbers()).add(item);
                        backTracking();
                        return;
                    }
                    if (i != item.getNumberOfPossibleNumbers()) {
                        map.get(i).remove(item);
                        j--;
                        map.get(item.getNumberOfPossibleNumbers()).add(item);
                        //System.out.println("[sortMap] neu sortiert: " + item.row + ";" + item.column);
                    }
                    if (item.getNumberOfPossibleNumbers() == 1) {
                        //System.out.println("[sortMap] neues eindeutiges Feld gefunden");
                        return;
                    }
                }
            }
        }
    }

    private void createMap() {
        for (int i = 1; i <= grid.getGridSize(); i++) {
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
}