package com.Data;

import java.util.List;

public class OutputMessages {

    public static final String INIT_TEXT = "Gebe das gewünschte Sudoku ein und bestätige es.";
    public static final String SET_TEXT = "Klicke auf \"nächste Zahl\" um den Algorithmus zu starten.";
    public static final String FAIL_TEXT_DECIMAL = "Bitte gebe nur Zahlen von 1 - 9 ein.";
    public static final String FAIL_TEXT_HEX = "Bitte gebe nur Zahlen von 1 - 9 ein.";
    public static final String NOT_SOLVABLE = "Das eingegebene Sudoku ist nicht lösbar.";
    public static final String EASY = "Einfaches Sudoku";
    public static final String MEDIUM = "Schweres Sudoku";
    public static final String HARD = "Sehr schweres Sudoku";
    public static final String OWN_SUDOKU = "Eigenes Sudoku eingeben";
    public static final String CHOOSE_AN_EXAMPLE = "Bitte gebe ein Sudoku ein oder wähle einen Schwierigkeitsgrad.";

    public static String numberNotAllowedInRow(int number, int row, int column) {
        return "Die Zahl " + number + " ist an der Stelle " + row + ";" + column + " nicht erlaubt, " +
                "weil in der selben Spalte die Zahl bereits vorkommt";
    }

    public static String numberNotAllowedInBox(int number, int row, int column) {
        return "Die Zahl " + number + " ist an der Stelle " + row + ";" + column + " nicht erlaubt, " +
                "weil die Zahl bereits in der selben Box vorkommt.";
    }

    public static String numberNotAllowedInColumn(int number, int row, int column) {
        return "Die Zahl " + number + " ist an der Stelle " + row + ";" + column + " nicht erlaubt " +
                "weil in der selben Reihe die Zahl bereits vorkommt";
    }

    public static String valideNumberWithMoreOptions(int number, int row, int column, List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return "Die Zahl " + number + " ist an der Stelle " + row + ";" + column +
                    " ist richtig und hatte keine weiteren Möglichkeiten.";
        } else {
            return "Die Zahl " + number + " an der Stelle " + row + ";" + column +
                    " ist richtig und hatte noch folgende weitere Möglichkeiten: " + numbers + ".";
        }
    }

    public static String newNumberNotAllowed(int number, int row, int column, List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return "Die Zahl " + number + " ist an der Stelle " + column + ";" + row
                    + " nicht erlaubt " +
                    "und aktuell gibt es keine anderen möglichen Zahlen an dieser Stelle.\n" +
                    "Die alte Zahl wurde wieder eingesetzt.";
        } else {
            return "Die Zahl " + number + " an der Stelle " + row + ";" + column + " nicht erlaubt " +
                    "und aktuelle möglichen Zahlen an dieser Stelle sind: " + numbers;
        }
    }
}
