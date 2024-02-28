package com.model;

import java.util.ArrayList;
import java.util.List;

public class MapItem {
    private int getColumn;
    private int row;
    private final List<Integer> possibleNumbers;
    private final List<Integer> allPossibleNumbers;

    public MapItem(int column, int row, List<Integer> allPossibleNumbers) {
        this.possibleNumbers = new ArrayList<>();
        this.allPossibleNumbers = new ArrayList<>();
        this.getColumn = column;
        this.row = row;
        this.possibleNumbers.addAll(allPossibleNumbers);
        this.allPossibleNumbers.addAll(allPossibleNumbers);
    }

    public int getNextNumber() {
        int result = 0;
        if (!possibleNumbers.isEmpty()) {
            result = possibleNumbers.get(0);
            possibleNumbers.remove(0);
        }
        return result;
    }

    public int removeNumber(int number) {
        int index = 0;
        for (int i : this.possibleNumbers) {
            if(i == number) {
                possibleNumbers.remove(index);
                return i;
            }
            index++;
        }
        return 0;
    }

    public int getColumn() {
        return getColumn;
    }

    public int getRow() {
        return row;
    }

    public void addNumber(int number) {
        possibleNumbers.add(number);
    }

    public boolean possibleNumbersIsEmpty() {
        return possibleNumbers.isEmpty();
    }

    public void reducePossibleNumbers(List<Integer> newPossibleNumbers) {
        this.possibleNumbers.clear();
        this.possibleNumbers.addAll(newPossibleNumbers);
    }

    public List<Integer> getPossibleNumbers() {
        return possibleNumbers;
    }

    public List<Integer> getAllPossibleNumbers() {
        return allPossibleNumbers;
    }

    public int getNumberOfPossibleNumbers() {
        return possibleNumbers.size();
    }

    public void restorePossibleNumbers() {
        possibleNumbers.clear();
        possibleNumbers.addAll(allPossibleNumbers);
    }

    public void clearPossibleNumbers() {
        possibleNumbers.clear();
    }
}