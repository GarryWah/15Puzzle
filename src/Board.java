import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

/**
 * Created by Admin on 4/12/2017.
 */
public class Board {
    private final int[][] blocks;
    private final int[][] goal;
    private final int dimension;


    public Board(int[][] blocks) {
        int[][] copy = copyArray(blocks);
        goal = new int[blocks.length][blocks.length];
        int v = 1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (j != blocks.length - 1) {
                    goal[i][j] = v++;
                } else {
                    if (i == blocks.length - 1)
                        goal[i][j] = 0;
                    else {
                        goal[i][j] = v++;
                    }

                }

            }
        }
        dimension = blocks.length;
        this.blocks = copy;
    }

    // board dimension n
    public int dimension() {
        return dimension;

    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != goal[i][j] && goal[i][j] != 0)
                    hamming++;
            }

        }
        return hamming;

    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int result = 0;
        int val;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                val = blocks[i][j];
                if (val != 0 && val != goal[i][j]) {
                    int correctPosI = 0;
                    int correctPosJ = 0;
                    label:
                    for (int k = 0; k < goal.length; k++) {
                        for (int l = 0; l < goal.length; l++) {
                            if (val == goal[k][l]) {
                                correctPosI = k;
                                correctPosJ = l;
                                break label;
                            }
                        }
                    }
                    if (correctPosI == i && correctPosJ > j)
                        result += correctPosJ - j;
                    if (correctPosI > i && correctPosJ == j)
                        result += correctPosI - i;
                    if (correctPosI == i && correctPosJ < j)
                        result += j - correctPosJ;
                    if (correctPosI < i && correctPosJ == j)
                        result += i - correctPosI;

                    if (correctPosI > i && correctPosJ > j)
                        result += correctPosI - i + correctPosJ - j;
                    if (correctPosI < i && correctPosJ > j)
                        result += i - correctPosI + correctPosJ - j;
                    if (correctPosI > i && correctPosJ < j)
                        result += correctPosI - i + j - correctPosJ;
                    if (correctPosI < i && correctPosJ < j)
                        result += i - correctPosI + j - correctPosJ;

                }
            }
        }
        return result;

    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;

    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] board = copyArray(blocks);
        int[][] old = copyArray(board);
        while (Arrays.deepEquals(old, board)) {
            int firstI = StdRandom.uniform(0, blocks.length);
            int firstJ = StdRandom.uniform(0, blocks.length);
            int secI = StdRandom.uniform(0, blocks.length);
            int secJ = StdRandom.uniform(0, blocks.length);
            while (board[firstI][firstJ] == 0 || board[secI][secJ] == 0) {
                firstI = StdRandom.uniform(0, blocks.length);
                firstJ = StdRandom.uniform(0, blocks.length);
                secI = StdRandom.uniform(0, blocks.length);
                secJ = StdRandom.uniform(0, blocks.length);
            }
            exchange(board, firstI, firstJ, secI, secJ);
        }
        return new Board(board);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return Arrays.deepEquals(blocks, board.blocks);
    }

    public Iterable<Board> neighbors() {
        Board copy = new Board(copyArray(blocks));
        Stack<Board> result = new Stack<>();
        int I0 = 0;
        int J0 = 0;
        for (int i = 0; i < copy.blocks.length; i++) {
            for (int j = 0; j < copy.blocks.length; j++) {
                if (copy.blocks[i][j] == 0) {
                    I0 = i;
                    J0 = j;
                }

            }
        }
        analyzeCorners(copy, result, I0, J0);
        analyzeHorizontals(copy, result, I0, J0);
        analyzeLeftVertical(copy, result, I0, J0);
        analyzeRightVertical(copy, result, I0, J0);
        analyzeGeneralPosition(copy, result, I0, J0);
        return result;
    }

    private void analyzeGeneralPosition(Board copy, Stack<Board> result, int i0, int j0) {
        int[][] newBoard1;
        int[][] newBoard2;
        int[][] newBoard3;
        int[][] newBoard4;
        if (i0 != 0 && i0 != copy.blocks.length - 1 && j0 != 0 && j0 != copy.blocks.length - 1) {
            exchange(copy.blocks, i0, j0, i0 - 1, j0);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, i0 - 1, j0, i0, j0);
            exchange(copy.blocks, i0, j0, i0 + 1, j0);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
            exchange(copy.blocks, i0 + 1, j0, i0, j0);
            exchange(copy.blocks, i0, j0, i0, j0 - 1);
            newBoard3 = copyArray(copy.blocks);
            result.push(new Board(newBoard3));
            exchange(copy.blocks, i0, j0 - 1, i0, j0);
            exchange(copy.blocks, i0, j0, i0, j0 + 1);
            newBoard4 = copyArray(copy.blocks);
            result.push(new Board(newBoard4));
        }
    }

    private void analyzeRightVertical(Board copy, Stack<Board> result, int i0, int j0) {
        int[][] newBoard1;
        int[][] newBoard2;
        int[][] newBoard3;
        if (j0 == copy.blocks.length - 1 && i0 != copy.blocks.length - 1 && i0 != 0) {
            exchange(copy.blocks, i0, j0, i0 - 1, j0);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, i0 - 1, j0, i0, j0);
            exchange(copy.blocks, i0, j0, i0 + 1, j0);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
            exchange(copy.blocks, i0 + 1, j0, i0, j0);
            exchange(copy.blocks, i0, j0, i0, j0 - 1);
            newBoard3 = copyArray(copy.blocks);
            result.push(new Board(newBoard3));
        }
    }

    private void analyzeLeftVertical(Board copy, Stack<Board> result, int i0, int j0) {
        int[][] newBoard1;
        int[][] newBoard2;
        int[][] newBoard3;
        if (j0 == 0 && i0 != copy.blocks.length - 1 && i0 != 0) {
            exchange(copy.blocks, i0, j0, i0 - 1, 0);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, i0 - 1, 0, i0, j0);
            exchange(copy.blocks, i0, j0, i0 + 1, 0);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
            exchange(copy.blocks, i0 + 1, 0, i0, j0);
            exchange(copy.blocks, i0, j0, i0, j0 + 1);
            newBoard3 = copyArray(copy.blocks);
            result.push(new Board(newBoard3));
        }
    }

    private void analyzeHorizontals(Board copy, Stack<Board> result, int i0, int j0) {
        int[][] newBoard1;
        int[][] newBoard2;
        int[][] newBoard3;
        if (i0 == 0 && j0 != 0 && j0 != copy.blocks.length - 1) {
            exchange(copy.blocks, 0, j0, 0, j0 - 1);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, 0, j0 - 1, 0, j0);
            exchange(copy.blocks, 0, j0, 0, j0 + 1);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
            exchange(copy.blocks, 0, j0 + 1, 0, j0);
            exchange(copy.blocks, 0, j0, 1, j0);
            newBoard3 = copyArray(copy.blocks);
            result.push(new Board(newBoard3));
        }
        if (i0 == copy.blocks.length - 1 && j0 != 0 && j0 != copy.blocks.length - 1) {
            exchange(copy.blocks, i0, j0, i0, j0 - 1);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, i0, j0 - 1, i0, j0);
            exchange(copy.blocks, i0, j0, i0, j0 + 1);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
            exchange(copy.blocks, i0, j0 + 1, i0, j0);
            exchange(copy.blocks, i0, j0, i0 - 1, j0);
            newBoard3 = copyArray(copy.blocks);
            result.push(new Board(newBoard3));
        }
    }

    private void analyzeCorners(Board copy, Stack<Board> result, int i0, int j0) {
        int[][] newBoard1;
        int[][] newBoard2;
        if (i0 == 0 && j0 == 0) {
            exchange(copy.blocks, 0, 0, 0, 1);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, 0, 1, 0, 0);
            exchange(copy.blocks, 0, 0, 1, 0);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
        }
        if (i0 == 0 && j0 == copy.blocks.length - 1) {
            exchange(copy.blocks, 0, copy.blocks.length - 1, 0, copy.blocks.length - 2);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, 0, copy.blocks.length - 2, 0, copy.blocks.length - 1);
            exchange(copy.blocks, 0, copy.blocks.length - 1, 1, copy.blocks.length - 1);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
        }
        if (i0 == copy.blocks.length - 1 && j0 == 0) {
            exchange(copy.blocks, copy.blocks.length - 1, 0, copy.blocks.length - 2, 0);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, copy.blocks.length - 2, 0, copy.blocks.length - 1, 0);
            exchange(copy.blocks, copy.blocks.length - 1, 0, copy.blocks.length - 1, 1);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
        }
        if (i0 == copy.blocks.length - 1 && j0 == copy.blocks.length - 1) {
            exchange(copy.blocks, copy.blocks.length - 1, copy.blocks.length - 1, copy.blocks.length - 2, copy.blocks.length - 1);
            newBoard1 = copyArray(copy.blocks);
            result.push(new Board(newBoard1));
            exchange(copy.blocks, copy.blocks.length - 2, copy.blocks.length - 1, copy.blocks.length - 1, copy.blocks.length - 1);
            exchange(copy.blocks, copy.blocks.length - 1, copy.blocks.length - 1, copy.blocks.length - 1, copy.blocks.length - 2);
            newBoard2 = copyArray(copy.blocks);
            result.push(new Board(newBoard2));
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private void exchange(int[][] array, int iStart, int jStart, int iEnd, int jEnd) {
        int tmp = array[iStart][jStart];
        array[iStart][jStart] = array[iEnd][jEnd];
        array[iEnd][jEnd] = tmp;

    }

    private int[][] copyArray(int[][] src) {
        int[][] result = new int[src.length][src.length];
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src.length; j++) {
                result[i][j] = src[i][j];
            }
        }
        return result;
    }

    public static void main(String[] args) {
    }
}
