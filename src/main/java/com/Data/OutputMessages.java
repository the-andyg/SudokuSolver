package com.Data;

public class OutputMessages {

    public static final String INIT_TEXT = "Gebe das gewünschte Sudoku ein und bestätige es.";
    public static final String SET_TEXT = "Klicke auf \"nächste Zahl\" um den Algorithmus zu starten.";
    public static final String FAIL_TEXT = "Die Eingabe war nicht korrekt.";
    public static final String NOT_SOLVABLE = "Das eingegebene Sudoku ist nicht lösbar.";
    public static final String EASY = "Einfach";
    public static final String MEDIUM = "Mittel";
    public static final String HARD = "Schwer";
    public static final String CHOOSE = "Wähle einen Schwierigkeitsgrad";
    public static final String CHOOSE_AN_EXAMPLE = "Wähle einen Schwierigkeitsgrad.";

    public static String numberNotAllowed(int number, int row, int column) {
        return "Die Zahl " + number + " ist an der Stelle " + row + ";" + column + " nicht erlaubt.";
    }
}
