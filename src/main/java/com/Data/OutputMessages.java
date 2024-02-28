package com.Data;

import java.util.List;

public class OutputMessages {
    public static final String INIT_TEXT = "Gebe das gewünschte Sudoku ein und bestätige es.";
    public static final String SET_TEXT = "Klicke auf \"nächste Zahl\" um den Algorithmus zu starten" +
            " oder gebe Zahlen in das Sudoku ein und Bestätige diese.";
    public static final String FAIL_TEXT_DECIMAL = "Bitte gebe nur Zahlen von 1 - 9 ein.";
    public static final String FAIL_TEXT_HEX = "Bitte gebe nur Zahlen von 1 - 9 ein.";
    public static final String EASY = "Einfaches Sudoku";
    public static final String MEDIUM = "Schweres Sudoku";
    public static final String HARD = "Sehr schweres Sudoku";
    public static final String OWN_SUDOKU = "Eigenes Sudoku eingeben";
    public static final String CHOOSE_AN_EXAMPLE = "Bitte gebe ein Sudoku ein oder wähle einen Schwierigkeitsgrad.";
    public static final String SUDOKU_SOLVED = "Das Sudoku wurde erfolgreich gelöst";
    public static final String SUDOKU_NOT_SOLVABLE = "Das Sudoku ist nicht lösbar!";

    public static String numberNotAllowedInRow(int number, int column, int row) {
        row++;
        column++;
        return "Die Zahl " + number + " ist an der Stelle " + column + ";" + row + " nicht erlaubt, " +
                "weil in der selben Spalte die Zahl bereits vorkommt";
    }

    public static String numberNotAllowedInBlock(int number, int column, int row) {
        row++;
        column++;
        return "Die Zahl " + number + " ist an der Stelle " + column + ";" + row + " nicht erlaubt, " +
                "weil die Zahl bereits in der selben Box vorkommt.";
    }

    public static String numberNotAllowedInColumn(int number, int column, int row) {
        row++;
        column++;
        return "Die Zahl " + number + " ist an der Stelle " + column + ";" + row + " nicht erlaubt " +
                "weil in der selben Reihe die Zahl bereits vorkommt";
    }

    public static String valideNumberWithMoreOptions(int number, int column, int row, List<Integer> numbers, String output) {
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

    public static String newNumberNotAllowed(int number, int column, int row, List<Integer> numbers, String output) {
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

    public static String setDistinct(int number, int column, int row, String output) {
        row++;
        column++;
        if (!output.isEmpty()) {
            output += "\n";
        }
        return output + "An der Stelle " + column + ";" + row + " ist nur die " + number + " möglich.";
    }

    public static String removeNumber(int number, int column, int row, String output) {
        row++;
        column++;
        if (!output.isEmpty()) {
            output += "\n";
        }
        return output + "Die Zahl " + number + " an der Stelle " + column + ";" + row +
                " wurde entfernt.";
    }

    public static String setBlock(int number, int column, int row) {
        row++;
        column++;
        return "Die Zahl " + number + " an der Stelle " + column + ";" + row +
                " kann nur dort in dem Block gesetzt werden und nicht in den das gelbe/gelben Feldern.";
    }

    public static String setRow(int number, int row) {
        row++;
        return "In der " + row + ". Reihe kann die" + number +
                " nur in das grüne Feld eingesetzt werden und nicht in das gelbe/die gelben";
    }

    public static String setColumn(int number, int column) {
        column++;
        return "In der " + column + ". Spalte kann die" + number +
                " nur in das grüne Feld eingesetzt werden und nicht in das gelbe/die gelben";
    }

    public static String noDistinctNumber(List<Integer> numbers) {
        return "Es wurde keine eindeutige Zahl gefunden." +
                " Somit wird die geringste Zahl eingesetzt. Weitere Möglichkeiten sind: " + numbers;
    }

    public static String backTracking(boolean count) {
        if (count) {
            return "In das grüne Feld konnte keine Zahl eingesetzt werden, bei dem keine Zahl mehr gesetzt werden konnte." +
                    " Die roten Felder wurden zurück gesetzt.";
        } else {
            return "\"In das grüne Feld konnte keine Zahl eingesetzt werden, bei dem keine Zahl mehr gesetzt werden konnte." +
                    " Das rote Feld wurde zurück gesetzt.";
        }
    }
}