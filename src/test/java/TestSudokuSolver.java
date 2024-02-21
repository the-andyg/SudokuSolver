import com.model.Grid;
import com.model.SudokuSolver;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSudokuSolver {

    @Test
    public void simpleTest() {
        int[][] sudokuExample = {
                {0, 0, 2, 0, 0, 4, 9, 0, 0},
                {0, 4, 5, 9, 0, 0, 0, 0, 1},
                {0, 0, 1, 6, 0, 0, 0, 2, 0},
                {0, 9, 4, 0, 6, 8, 0, 0, 7},
                {0, 0, 6, 5, 0, 7, 8, 4, 9},
                {0, 0, 0, 4, 9, 0, 0, 0, 5},
                {0, 0, 0, 0, 0, 0, 7, 3, 4},
                {0, 1, 0, 0, 0, 9, 0, 0, 0},
                {0, 3, 0, 0, 4, 5, 1, 0, 2}
        };

        Grid grid = new Grid(sudokuExample, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }
        assertTrue(SudokuValidator.isValidSudoku(grid.getGrid(), 9));
        System.out.println(count);
    }

    @Test
    public void simpleTestTwo() {
        int[][] sudokuExample = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        Grid grid = new Grid(sudokuExample, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }
        assertTrue(SudokuValidator.isValidSudoku(grid.getGrid(), 9));
        System.out.println(count);
    }

    @Test
    public void testBacktracking() {
        int[][] sudokuForBacktracking = {
                {6, 0, 0, 0, 1, 9, 7, 0, 0},
                {0, 0, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 3, 1, 9},
                {0, 0, 0, 4, 0, 0, 0, 0, 1},
                {3, 0, 0, 0, 0, 2, 9, 0, 0},
                {0, 0, 0, 0, 8, 5, 0, 2, 0},
                {9, 0, 0, 0, 0, 0, 0, 6, 5},
                {0, 0, 5, 3, 4, 8, 0, 0, 0},
                {8, 0, 7, 0, 0, 0, 0, 0, 0}
        };

        Grid grid = new Grid(sudokuForBacktracking, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }

        SudokuValidator.printSudoku(grid.getGrid(), 9);
        System.out.println(count);
        assertTrue(SudokuValidator.isValidSudoku(grid.getGrid(), 9));
    }

    @Test
    public void testKiller() {
        int[][] sudokuForBacktracking = {
                {0, 4, 0, 2, 0, 0, 0, 3, 0},
                {1, 0, 0, 0, 0, 5, 0, 0, 4},
                {0, 0, 0, 7, 6, 0, 0, 0, 9},
                {0, 0, 0, 0, 0, 3, 0, 2, 6},
                {0, 0, 0, 0, 5, 0, 7, 9, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 9, 0, 0, 0, 7, 0},
                {7, 0, 9, 0, 4, 1, 0, 0, 0},
                {0, 0, 6, 0, 0, 0, 0, 0, 8}
        };

        Grid grid = new Grid(sudokuForBacktracking, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            SudokuValidator.printSudoku(grid.getGrid(), 9);
            count++;
        }

        int[][] solvedSudoku = grid.getGrid();
        SudokuValidator.printSudoku(grid.getGrid(), 9);
        System.out.println(count);
        assertTrue(SudokuValidator.isValidSudoku(solvedSudoku, 9));
    }

    @Test
    public void testHard() {
        int[][] sudokuForBacktracking = {
                {3, 9, 0, 4, 0, 0, 0, 7, 0},
                {0, 0, 0, 6, 0, 0, 9, 1, 0},
                {4, 0, 0, 0, 0, 0, 0, 0, 5},
                {0, 8, 0, 0, 0, 0, 0, 0, 3},
                {0, 0, 0, 1, 0, 0, 0, 4, 0},
                {9, 2, 0, 0, 0, 0, 0, 0, 6},
                {7, 0, 0, 0, 0, 0, 0, 3, 9},
                {1, 0, 0, 9, 3, 8, 0, 0, 0},
                {0, 3, 0, 0, 4, 0, 0, 2, 0}
        };

        Grid grid = new Grid(sudokuForBacktracking, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }

        int[][] solvedSudoku = grid.getGrid();
        SudokuValidator.printSudoku(grid.getGrid(), 9);
        System.out.println(count);
        assertTrue(SudokuValidator.isValidSudoku(solvedSudoku, 9));
    }

    @Test
    public void testSimpleThree() {
        int[][] sudokuForBacktracking = {
                {8, 0, 0, 9, 0, 4, 0, 0, 7},
                {0, 0, 0, 0, 0, 7, 8, 2, 1},
                {6, 7, 0, 0, 0, 0, 4, 0, 0},
                {0, 5, 0, 4, 3, 1, 0, 0, 0},
                {7, 0, 0, 6, 2, 0, 5, 0, 0},
                {0, 6, 3, 0, 0, 0, 0, 9, 0},
                {9, 0, 7, 0, 4, 0, 0, 0, 2},
                {0, 0, 5, 0, 0, 0, 3, 7, 0},
                {0, 1, 0, 0, 8, 0, 9, 4, 5}
        };

        Grid grid = new Grid(sudokuForBacktracking, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        final long timeStart = System.currentTimeMillis();
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }
        final long timeEnd = System.currentTimeMillis();
        int[][] solvedSudoku = grid.getGrid();
        SudokuValidator.printSudoku(grid.getGrid(), 9);
        System.out.println(count);
        System.out.println("Verlaufszeit der Schleife: " + (timeEnd - timeStart) + " Millisek.");
        assertTrue(SudokuValidator.isValidSudoku(solvedSudoku, 9));
    }

    @Test
    public void testKillerTwo() {
        int[][] sudokuForBacktracking = {
                {0, 0, 3, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 5, 6, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 6, 0, 7},
                {0, 0, 0, 2, 0, 0, 0, 9, 4},
                {0, 0, 0, 0, 3, 9, 0, 0, 0},
                {4, 6, 0, 0, 0, 8, 0, 0, 3},
                {0, 9, 0, 0, 0, 4, 1, 0, 0},
                {0, 0, 8, 6, 0, 0, 0, 4, 0},
                {0, 0, 0, 0, 2, 0, 8, 0, 0}
        };

        Grid grid = new Grid(sudokuForBacktracking, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        final long timeStart = System.currentTimeMillis();
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }
        final long timeEnd = System.currentTimeMillis();
        int[][] solvedSudoku = grid.getGrid();
        SudokuValidator.printSudoku(grid.getGrid(), 9);
        System.out.println(count);
        System.out.println("Verlaufszeit der Schleife: " + (timeEnd - timeStart) + " Millisek.");
        assertTrue(SudokuValidator.isValidSudoku(solvedSudoku, 9));
    }

    @Test
    public void testHexSimple() {
        int[][] hexSudoku = {
                {0, 2, 0, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16},
                {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4},
                {9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8},
                {13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1},
                {6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 5},
                {10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                {14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13},
                {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1, 2},
                {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 5, 6},
                {11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {15, 16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
                {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3},
                {8, 9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7},
                {12, 13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
                {16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
        };

        Grid grid = new Grid(hexSudoku, 16);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }

        int[][] solvedSudoku = grid.getGrid();
        SudokuValidator.printSudoku(grid.getGrid(), 16);
        System.out.println(count);
        assertTrue(SudokuValidator.isValidSudoku(solvedSudoku, 16));
    }

    @Test
    public void sample() {
        int[][] sudokuForBacktracking = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };


        Grid grid = new Grid(sudokuForBacktracking, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }

        int[][] solvedSudoku = grid.getGrid();
        SudokuValidator.printSudoku(grid.getGrid(), 9);
        System.out.println(count);
        assertTrue(SudokuValidator.isValidSudoku(solvedSudoku, 9));
    }

    @Test
    public void testInvalid() {
        int[][] sudokuForBacktracking = {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Grid grid = new Grid(sudokuForBacktracking, 9);
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            SudokuValidator.printSudoku(grid.getGrid(), 9);
            count++;
        }

        int[][] solvedSudoku = grid.getGrid();
        SudokuValidator.printSudoku(grid.getGrid(), 9);
        System.out.println(count);
        assertTrue(SudokuValidator.isValidSudoku(solvedSudoku, 9));
    }
}