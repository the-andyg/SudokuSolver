package com.model;

public enum HexEnum {
    A,
    B,
    C,
    D,
    E,
    F;

    public HexEnum toHexEnum(String s) {
        switch (s) {
            case "A" -> {
                return A;
            }
            case "B" -> {
                return B;
            }
            case "C" -> {
                return C;
            }
            case "D" -> {
                return D;
            }
            case "E" -> {
                return E;
            }
            case "F" -> {
                return F;
            }
            default -> {
                return null;
            }
        }
    }

    public int toNumber(HexEnum e) {
        switch (e) {
            case A -> {
                return 10;
            }
            case B -> {
                return 11;
            }
            case C -> {
                return 12;
            }
            case D -> {
                return 13;
            }
            case E -> {
                return 14;
            }
            case F -> {
                return 15;
            }
            default -> {
                return 0;
            }

        }
    }
}
