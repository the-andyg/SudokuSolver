package com.Data;

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

    public static String numberNotAllowed(int number, int row, int column) {
        return "Die Zahl " + number + " ist an der Stelle " + row + ";" + column + " nicht erlaubt.";
    }
}
