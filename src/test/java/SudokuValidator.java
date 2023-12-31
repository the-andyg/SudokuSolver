public class SudokuValidator {
    public static boolean isValidSudoku(int[][] sudoku, int size) {
        // Überprüfe jede Zeile und Spalte
        for (int i = 0; i < size; i++) {
            if (!isValidRow(sudoku, i, size) || !isValidColumn(sudoku, i, size)) {
                return false;
            }
        }

        // Überprüfe jeden 3x3-Block
        int blockSize = (int) Math.sqrt(size);
        for (int i = 0; i < size; i += blockSize) {
            for (int j = 0; j < size; j += blockSize) {
                if (!isValidBlock(sudoku, i, j, size)) {
                    return false;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(sudoku[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValidRow(int[][] sudoku, int row, int size) {
        boolean[] seen = new boolean[size];
        for (int i = 0; i < size; i++) {
            int num = sudoku[row][i];
            if (num != 0) {
                if (seen[num - 1]) {
                    return false; // Zahl wurde bereits gesehen
                }
                seen[num - 1] = true;
            }
        }
        return true;
    }

    private static boolean isValidColumn(int[][] sudoku, int col, int size) {
        boolean[] seen = new boolean[size];
        for (int i = 0; i < size; i++) {
            int num = sudoku[i][col];
            if (num != 0) {
                if (seen[num - 1]) {
                    return false; // Zahl wurde bereits gesehen
                }
                seen[num - 1] = true;
            }
        }
        return true;
    }

    private static boolean isValidBlock(int[][] sudoku, int startRow, int startCol, int size) {
        boolean[] seen = new boolean[size];
        int blocksize = (int) Math.sqrt(size);
        for (int i = 0; i < blocksize; i++) {
            for (int j = 0; j < blocksize; j++) {
                int num = sudoku[startRow + i][startCol + j];
                if (num != 0) {
                    if (seen[num - 1]) {
                        return false; // Zahl wurde bereits gesehen
                    }
                    seen[num - 1] = true;
                }
            }
        }
        return true;
    }

    public static void printSudoku(int[][] sudoku, int size) {
        int blockSize = (int) Math.sqrt(size);
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                if (sudoku[i][j] < 10) {
                    System.out.print(sudoku[i][j] + "  ");
                } else {
                    System.out.print(sudoku[i][j] + " ");
                }
                if ((j + 1) % blockSize == 0 && j < sudoku[i].length - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if (blockSize == 3) {
                if ((i + 1) % 3 == 0 && i < sudoku.length - 1) {
                    System.out.println("------+-------+------");
                }
            }
            if (blockSize == 4) {
                if ((i + 1) % 4 == 0 && i < sudoku.length - 1) {
                    System.out.println("------------+-------------+-------------+-------------");
                }
            }
        }
    }
}
