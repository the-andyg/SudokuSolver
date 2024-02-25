import com.model.Grid;
import com.model.SudokuSolver;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSudokuSolver {

    @Test
    public void simpleTest() {
        int[][] unsolvedSudoku = {
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

        Grid grid = new Grid(unsolvedSudoku, 9);
        assertTrue(grid.isSolveAble());
        SudokuSolver solver = new SudokuSolver(grid);
        int count = 0;
        while (solver.isNotDone()) {
            solver.nextNumber();
            count++;
        }
        Grid checkGrid = new Grid(grid.getGrid(), grid.getGridSize());
        assertTrue(checkGrid.isSolveAble());
        System.out.println(count);
    }

    @Test
    public void simpleTestTwo() {
        int[][] unsolvedSudoku = {
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

        Grid grid = new Grid(unsolvedSudoku, 9);
        assertTrue(grid.isSolveAble());
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
        int[][] unsolvedSudoku = {
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
        Grid grid = new Grid(unsolvedSudoku, 9);
        assertTrue(grid.isSolveAble());
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
        int[][] unsolvedSudoku = {
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
        Grid grid = new Grid(unsolvedSudoku, 9);
        assertTrue(grid.isSolveAble());
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
        int[][] unsolvedSudoku = {
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
        Grid grid = new Grid(unsolvedSudoku, 9);
        assertTrue(grid.isSolveAble());
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
        int[][] unsolvedSudoku = {
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
        Grid grid = new Grid(unsolvedSudoku, 9);
        assertTrue(grid.isSolveAble());
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
        int[][] unsolvedSudoku = {
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
        Grid grid = new Grid(unsolvedSudoku, 9);
        assertTrue(grid.isSolveAble());
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
    public void testExpert() {
        int[][] unsolvedSudoku = {
                {0, 0, 0, 0, 0, 0, 0, 0, 5},
                {1, 0, 9, 0, 0, 4, 0, 0, 8},
                {0, 3, 0, 5, 0, 8, 7, 9, 1},
                {0, 0, 0, 2, 0, 0, 9, 0, 0},
                {0, 0, 4, 0, 0, 0, 1, 0, 0},
                {3, 9, 1, 0, 0, 0, 8, 5, 0},
                {0, 0, 5, 0, 0, 0, 0, 4, 0},
                {9, 0, 0, 0, 0, 5, 0, 0, 0},
                {2, 7, 8, 0, 0, 0, 0, 0, 0}
        };
        Grid grid = new Grid(unsolvedSudoku, 9);
        assertTrue(grid.isSolveAble());
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
        int[][] unsolvedSudoku = {
                {0, 0, 0, 0, 0, 9, 0, 10, 0, 0, 3, 0, 0, 0, 0, 0},
                {5, 0, 8, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 13},
                {0, 0, 0, 0, 0, 0, 15, 0, 16, 0, 0, 14, 0, 0, 3, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 15, 0, 0},
                {0, 0, 0, 0, 0, 13, 0, 0, 0, 15, 0, 0, 0, 8, 0, 0},
                {0, 0, 0, 0, 10, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 14},
                {8, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0},
                {14, 0, 0, 11, 0, 0, 6, 0, 0, 0, 5, 0, 0, 0, 0, 0},
                {0, 0, 0, 7, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 6, 0, 0, 0, 12, 0},
                {0, 0, 12, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 8, 0, 0, 0, 0, 7, 0, 0, 0, 0, 10, 0},
                {0, 4, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 2, 0, 13, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 6, 0, 0, 8},
                {0, 13, 0, 0, 3, 0, 0, 0, 0, 0, 8, 0, 0, 0, 11, 0}
        };
        Grid grid = new Grid(unsolvedSudoku, 16);
        assertTrue(grid.isSolveAble());
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
    public void testHexMedium() {
        int[][] unsolvedSudoku = {
                {0, 6, 0, 0, 0, 9, 0, 10, 0, 0, 3, 0, 0, 0, 0, 0},
                {5, 0, 8, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 13},
                {0, 0, 0, 0, 0, 0, 15, 0, 16, 0, 0, 14, 0, 0, 3, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 15, 0, 0},
                {0, 0, 0, 0, 0, 13, 0, 0, 0, 15, 0, 0, 0, 8, 0, 0},
                {0, 0, 0, 0, 10, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 14},
                {8, 0, 0, 0, 0, 15, 0, 0, 5, 0, 0, 4, 0, 0, 0, 0},
                {14, 0, 0, 11, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 7, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 6, 0, 0, 0, 12, 0},
                {0, 0, 12, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 8, 0, 0, 0, 0, 7, 0, 0, 0, 0, 10, 0},
                {0, 4, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 2, 0, 13, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 6, 0, 0, 8},
                {0, 13, 0, 0, 3, 0, 0, 0, 0, 0, 8, 0, 0, 0, 11, 0}
        };
        Grid grid = new Grid(unsolvedSudoku, 16);
        assertTrue(grid.isSolveAble());
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
    public void testHexHard() {
        int[][] unsolvedSudoku = {
                {13, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 3, 0, 0, 0, 11},
                {0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0},
                {0, 0, 0, 4, 0, 0, 16, 0, 0, 12, 0, 0, 15, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 13, 0, 0},
                {0, 9, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 8, 0},
                {0, 0, 0, 0, 2, 0, 0, 6, 0, 0, 4, 0, 0, 0, 0, 0},
                {0, 0, 14, 0, 0, 8, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 10, 0, 9, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0},
                {0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 0},
                {0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0},
                {0, 0, 0, 12, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0},
                {0, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {15, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 8, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 13, 0, 0, 0, 8, 0, 0, 0, 0}
        };
        Grid grid = new Grid(unsolvedSudoku, 16);
        assertTrue(grid.isSolveAble());
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
    public void testHasNoNumber() {
        int[][] unsolvedSudoku = {
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
        Grid grid = new Grid(unsolvedSudoku, 9);
        assertFalse(grid.isSolveAble());
    }

    @Test
    public void testInvalid() {
        int[][] unsolvedSudoku = {
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
        Grid grid = new Grid(unsolvedSudoku, 9);
        assertFalse(grid.isSolveAble());
    }
}