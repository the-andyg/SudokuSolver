package com.Data;

import java.util.List;

public class OutputMessages {

    public static final String INIT_TEXT = "Gebe das gewünschte Sudoku ein und bestätige es.";
    public static final String SET_TEXT = "Klicke auf \"nächste Zahl\" um den Algorithmus zu starten.";
    public static final String FAIL_TEXT_DECIMAL = "Bitte gebe nur Zahlen von 1 - 9 ein.";
    public static final String FAIL_TEXT_HEX = "Bitte gebe nur Zahlen von 1 - 9 ein.";
    public static final String EASY = "Einfaches Sudoku";
    public static final String MEDIUM = "Schweres Sudoku";
    public static final String HARD = "Sehr schweres Sudoku";
    public static final String OWN_SUDOKU = "Eigenes Sudoku eingeben";
    public static final String CHOOSE_AN_EXAMPLE = "Bitte gebe ein Sudoku ein oder wähle einen Schwierigkeitsgrad.";
    public static final String ALGO_FAILED = "Fehler! Das Sudoku wurde nicht richtig gelöst!";
    public static final String SUDOKU_SOLVED = "Das Sudoku wurde erfolgreich gelöst";
    public static final String SUDOKU_NOT_SOLVABLE = "Das Sudoku ist nicht lösbar!";

    public static String numberNotAllowedInRow(int number, int row, int column) {
        row++;
        column++;
        return "Die Zahl " + number + " ist an der Stelle " + column + ";" + row + " nicht erlaubt, " +
                "weil in der selben Spalte die Zahl bereits vorkommt";
    }

    public static String numberNotAllowedInBox(int number, int row, int column) {
        row++;
        column++;
        return "Die Zahl " + number + " ist an der Stelle " + column + ";" + row + " nicht erlaubt, " +
                "weil die Zahl bereits in der selben Box vorkommt.";
    }

    public static String numberNotAllowedInColumn(int number, int row, int column) {
        row++;
        column++;
        return "Die Zahl " + number + " ist an der Stelle " + column + ";" + row + " nicht erlaubt " +
                "weil in der selben Reihe die Zahl bereits vorkommt";
    }

    public static String valideNumberWithMoreOptions(int number, int row, int column, List<Integer> numbers, String output) {
        row++;
        column++;
        if (!output.isEmpty()) {
            output += "\n";
        }
        if (numbers.isEmpty()) {
            return output + "Die Zahl " + number + " ist an der Stelle " + column + ";" + row +
                    " ist richtig und hatte keine weiteren Möglichkeiten.";
        } else {
            return output + "Die Zahl " + number + " an der Stelle " + column + ";" + row +
                    " ist richtig und hatte noch folgende weitere Möglichkeiten: " + numbers + ".";
        }
    }

    public static String newNumberNotAllowed(int number, int row, int column, List<Integer> numbers, String output) {
        row++;
        column++;
        if (!output.isEmpty()) {
            output += "\n";
        }
        if (numbers.isEmpty()) {
            return output + "Die Zahl " + number + " ist an der Stelle " + column + ";" + row
                    + " nicht erlaubt " +
                    "und aktuell gibt es keine anderen möglichen Zahlen an dieser Stelle." +
                    " Die alte Zahl wurde wieder eingesetzt.";
        } else {
            return output + "Die Zahl " + number + " an der Stelle " + column + ";" + row + " nicht erlaubt " +
                    "und aktuelle möglichen Zahlen an dieser Stelle sind: " + numbers;
        }
    }

    public static String setDistinct(int number, int row, int column, String output) {
        row++;
        column++;
        if (!output.isEmpty()) {
            output += "\n";
        }
        return output + "An der Stelle " + column + ";" + row + " ist nur die " + number + " möglich.";
    }

    public static String removeNumber(int number, int row, int column, String output) {
        row++;
        column++;
        if (!output.isEmpty()) {
            output += "\n";
        }
        return output + "Die Zahl " + number + " an der Stelle " + column + ";" + row +
                " wurde entfernt.";
    }

    public static String setBox(int number, int row, int column) {
        row++;
        column++;
        return "Die Zahl " + number + " an der Stelle " + column + ";" + row +
                " kann nur dort in der Box gesetzt werden.";
    }

    public static String setRow(int number, int row, int column) {
        row++;
        column++;
        return "Die Zahl " + number + " an der Stelle " + column + ";" + row +
                " kann nur dort in der Spalte gesetzt werden.";
    }

    public static String setColumn(int number, int row, int column) {
        row++;
        column++;
        return "Die Zahl " + number + " an der Stelle " + column + ";" + row +
                " kann nur dort in der Reihe gesetzt werden.";
    }

    public static String noDistinctNumber(List<Integer> numbers) {
        return "Es wurde keine eindeutige Zahl gefunden." +
                " Somit wird die geringste Zahl eingesetzt. Weitere Möglichkeiten sind: " + numbers;
    }

    public static String backTracking(boolean count) {
        if (count) {
            return "Es wurde ein Feld gefunden, bei dem keine Zahl mehr gesetzt werden konnte." +
                    " Die roten Felder wurden zurück gesetzt.";
        } else {
            return "Es wurde ein Feld gefunden, bei dem keine Zahl mehr gesetzt werden konnte." +
                    " Das rote Feld wurde zurück gesetzt.";
        }
    }
}