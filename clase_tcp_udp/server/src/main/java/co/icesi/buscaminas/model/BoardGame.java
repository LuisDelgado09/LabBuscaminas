package co.icesi.buscaminas.model;

import java.util.Random;

public class BoardGame {

    private Cell[][] board;

    private int mines;

    public synchronized int getMines() {
        return mines;
    }

    public synchronized int initGame(int n, int m, int mines){
        if (mines < 0 || mines >= n * m) {
            throw new IllegalArgumentException("Cantidad de minas inválida");
        }

        this.mines = mines;
        board = new Cell[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j] = new Cell(false, 0);
            }
        }
        Random rd = new Random();
        int placed = 0;

        while (placed < mines) {
            int i = rd.nextInt(n);
            int j = rd.nextInt(m);

            if (!board[i][j].isLandMine()) {
                board[i][j].setLandMine(true);
                placed++;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!board[i][j].isLandMine()) {
                    board[i][j].setValue(getMinesAround(i, j));
                }
            }
        }
        return placed;
    }

    public synchronized void showAll(boolean show){
        for (int i = 0; i <board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j].setShowAll(show);
            }
        }
    }

    private synchronized int getMinesAround(int i, int j) {
        int mines = 0;
        mines += i > 0 && board[i-1][j].isLandMine()?1:0;
        mines += i < board.length-1 && board[i+1][j].isLandMine()?1:0;
        mines += j > 0 && board[i][j-1].isLandMine()?1:0;
        mines += j < board[0].length-1 && board[i][j+1].isLandMine()?1:0;
        mines += i > 0 && j > 0 && board[i-1][j-1].isLandMine()?1:0;
        mines += i > 0 && j < board[0].length-1 && board[i-1][j+1].isLandMine()?1:0;
        mines += i < board.length-1 && j > 0 && board[i+1][j-1].isLandMine()?1:0;
        mines += i < board.length-1 && j < board[0].length-1 && board[i+1][j+1].isLandMine()?1:0;
        return mines;
    }

    public synchronized void printBoard(){
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < board[0].length; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int i = 0; i <board.length; i++) {
            System.out.print(i+" [");
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(" "+board[i][j]);
            }
            System.out.println(" ]");
        }
    }
    public synchronized boolean selectCell(int i, int j){
        if(i<0 || i>= board.length || j<0 || j >= board[0].length ){
            throw new RuntimeException("Cell no valid");
        }
        Cell cell = board[i][j];
        if(cell.isLandMine()){
            showAll(true);
            throw new RuntimeException("Game over");
        }else {
            if (cell.isHide()) {
                showCells(i,j,true);
            }
            return validWin();
        }
    }

    private synchronized boolean validWin(){
        boolean win = true;
        for (int i = 0; i <board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                win &= !board[i][j].isHide() || board[i][j].isLandMine();
            }
        }
        return win;
    }

    private synchronized void showCells(int i, int j, boolean deep) {
        if (i < 0 || i >= board.length ||
            j < 0 || j >= board[0].length ||
            !board[i][j].isHide() ||
            board[i][j].isLandMine()) {
            return;
        }
        board[i][j].setHide(false);
        if (board[i][j].getValue() != 0) {
            return;
        }
        showCells(i, j - 1, true);
        showCells(i, j + 1, true);
        showCells(i - 1, j, true);
        showCells(i + 1, j, true);
        showCells(i - 1, j - 1, true);
        showCells(i - 1, j + 1, true);
        showCells(i + 1, j - 1, true);
        showCells(i + 1, j + 1, true);
    }

    public synchronized Cell[][] getBoard() {
        return board;
    }

    public synchronized String getBoardString() {
    StringBuilder result = new StringBuilder();

    result.append("\n       ");
    for (int j = 0; j < board[0].length; j++) {
        result.append(String.format("%-4d", j + 1));
    }
    result.append("\n");

    result.append("     +");
    for (int j = 0; j < board[0].length; j++) {
        result.append("----");
    }
    result.append("\n");

    for (int i = 0; i < board.length; i++) {
        result.append(String.format("%-3d  | ", i + 1));

        for (int j = 0; j < board[i].length; j++) {
            result.append(String.format("[%s] ", board[i][j]));
        }

        result.append("\n");
    }

    return result.toString();
}

    @Override
    public synchronized String toString() {
        return getBoardString();
    }

    public synchronized void markCell(int i, int j) {
        if(i<0 || i>= board.length || j<0 || j >= board[0].length ){
            throw new RuntimeException("Cell no valid");
        }
        Cell cell = board[i][j];
        cell.setMarked(!cell.isMarked());
    }
}
