package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SudokuSolver {
    private final Grid grid;
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

    public void nextNumber() {
        if (!isNotDone()) {
            outputText = "Das Sudoku wurde erfolgreich gelöst";
            return;
        }
        newFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        removedFields = new boolean[grid.getGridSize()][grid.getGridSize()];
        if (setDistinctNumber()) {
            outputText = "Das grüne Feld wurde / die grünen Felder wurden gesetzt, weil keine andere Zahl möglich war.";
            return;
        }
        if (setBoxNumber()) {
            outputText = "Das grüne Feld wurde / die grünen Felder wurden gesetzt, weil in dem 3x3/4x4 Block die gesetzte Zahl nur dort möglich war.";
            return;
        }
        setNextNumber();
    }

    public boolean isNotDone() {
        return !(solvedItems.size() == numbersSearching);
    }

    private boolean setBoxNumber() {
        boolean result = false;
        for (int x = 2; x <= grid.getGridSize(); x++) {
            if (!map.get(x).isEmpty()) {
                for (int y = 0; y < map.get(x).size(); y++) {
                    MapItem item = map.get(x).get(y);
                    int check = checkBoxNumber(item);
                    if (check != 0) {
                        grid.setNumber(check, item.row, item.column);
                        item.clearPossibleNumbers();
                        map.get(x).remove(item);
                        solvedItems.add(item);
                        newFields[item.row][item.column] = true;
                        System.out.println("[setBoxNumber] setBox: " + item.row + ";" + item.column);
                        result = true;
                    }
                }
            }
        }
        if (result) {
            sortMap();
        }
        return result;
    }

    private int checkBoxNumber(MapItem itemOne) {
        boolean[] boxNumbers = new boolean[grid.getGridSize() + 1];
        int boxSize = (int) Math.sqrt(grid.getGridSize());
        for (int numberOne : itemOne.getPossibleNumbers()) {
            boxNumbers[numberOne] = true;
        }
        int localRowOne = itemOne.row - itemOne.row % boxSize;
        int localColumnOne = itemOne.column - itemOne.column % boxSize;
        for (int i = 1; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int k = 0; k < map.get(i).size(); k++) {
                    MapItem itemTwo = map.get(i).get(k);
                    int localRowTwo = itemTwo.row - itemTwo.row % boxSize;
                    int localColumnTwo = itemTwo.column - itemTwo.column % boxSize;
                    if (localColumnOne == localColumnTwo && localRowOne == localRowTwo
                            && !(itemOne.column == itemTwo.column && itemOne.row == itemTwo.row)) {
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
            while (!map.get(1).isEmpty()) {
                MapItem item = map.get(1).get(0);
                map.get(1).remove(item);
                solvedItems.add(item);
                int newNumber = item.getNextNumber();
                grid.setNumber(newNumber, item.row, item.column);
                System.out.println("[SetDistinct] Eindeutige Zahl setzen: " + item.row + ";" + item.column + ":" + newNumber);
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
                System.out.println("[SetNextNumber] Kästchen mit wenigsten Möglichkeiten finden und setzen");
                if (item.possibleNumbersIsEmpty()) {
                    System.out.println("[SetNextNumber] Das gefundene Kästchen kann keine Zahl setzen. Gefundenes Kästchen zurück setzen");
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
            System.out.println(m.row + ";" + m.column + ": " + m.getPossibleNumbers());
        }
        MapItem item = solvedItems.get(solvedItems.size() - 1);
        while (item.possibleNumbersIsEmpty()) {
            System.out.println("[backTracking] eine zahl zurück gehen, weil keine weitere Möglichkeiten gefunden wurden");
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
        System.out.println("[backTracking] Zahl gefunden mit weiterer Option");
        int newNumber = item.getNextNumber();
        removedFields[item.row][item.column] = true;
        grid.setNumber(newNumber, item.row, item.column);
        sortMap();
    }

    private void sortMap() {
        System.out.println("[sortMap] Neu sortieren");
        MapItem item;
        for (int i = 2; i <= grid.getGridSize(); i++) {
            if (!map.get(i).isEmpty()) {
                for (int j = 0; j < map.get(i).size(); j++) {
                    item = map.get(i).get(j);
                    item.reducePossibleNumbers(grid.getNewPossibleNumbers(item.getAllPossibleNumbers(), item.row, item.column));
                    if (item.possibleNumbersIsEmpty()) {
                        System.out.println("[sortMap] ein feld ohne Möglichkeiten Backtracking starten" + item.row + ";" + item.column);
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
                        System.out.println("[sortMap] neu sortiert: " + item.row + ";" + item.column);
                    }
                    if (item.getNumberOfPossibleNumbers() == 1) {
                        System.out.println("[sortMap] neues eindeutiges Feld gefunden");
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
//        for (int i = 1; i <= grid.getGridSize(); i++) {
//            if (!map.get(i).isEmpty()) {
//                for (int j = 0; j < map.get(i).size(); j++) {
//                    MapItem m = map.get(i).get(j);
//                    System.out.println(m.row + ";" + m.column + ":" + m.getPossibleNumbers());
//                }
//            }
//        }
    }
}