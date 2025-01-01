package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.*;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;

import java.util.Arrays;
import java.util.Random;


public class GameBoard {

//    private static final int LAND_MINE_COUNT = 10;
    private Cell[][] board;
    private final int getLandMineCount;

    public GameBoard(GameLevel gameLevel){
        int rowSize = gameLevel.getRowSize();
        int colSize = gameLevel.getColSize();
        board = new Cell[rowSize][colSize];

        getLandMineCount = gameLevel.getLandMineCount();

    }

    public void initializeGame() {
        int rowSize = getRowSize();
        int colSize = getColSize();
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                board[row][col] = new EmptyCell();
            }
        }
        for (int i = 0; i < getLandMineCount; i++) {
            int landMineCol = new Random().nextInt(colSize);
            int landMineRow = new Random().nextInt(rowSize);
            LandMineCell landMineCell = new LandMineCell();
            board[landMineRow][landMineCol] = landMineCell;
        }
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (!isLandMineCell(row, col)) {
                    continue;
                }
                int count = countNearByLandMines(row, col);
                if(count == 0){
                    continue;
                }
                // 셀의 count update가 아닌 생성자에서 바로 받기
                NumberCell numberCell = new NumberCell(count);
                board[row][col] = numberCell;
            }
        }
    }

    public int getRowSize(){
        return board.length;
    }

    public int getColSize(){
        return board[0].length;
    }

    private  int countNearByLandMines(int row, int col) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        int count = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < colSize && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < colSize && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < rowSize && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < rowSize && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < rowSize && col + 1 < colSize && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    public boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return findCell(selectedRowIndex, selectedColIndex).isLandMine();
    }

    public String getSign(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        return cell.getSigh();
    }

    private Cell findCell(int row, int col) {
        return board[row][col];
    }

    public void flag(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.flag();
    }

    public void open(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.open();
    }

    public   void openSurroundedCells(int row, int col) {
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) {
            return;
        }
        if (isOpenedCell(row, col)) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }

        open(row, col);

        if (doesCellHaveLandMineCount(row, col)) {
            return;
        }
        openSurroundedCells(row - 1, col - 1);
        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }

    private boolean doesCellHaveLandMineCount(int row, int col) {
        return findCell(row, col).hasLandMineCount();
    }

    private boolean isOpenedCell(int row, int col) {
        return findCell(row, col).isOpened();
    }

    public  boolean isAllCellChecked() {
        return Arrays.stream(board)
                .flatMap(stringArr -> Arrays.stream(stringArr))
                .allMatch(Cell::isChecked); // -> 모든 Cell이 다 체크 되었는지 확인해

        // `.equals` 에 대해서는 npe가 발생할 수 있음 -> stream 객체가 검증되지 않은 상태
        // 그렇기 때문에 .equals 메소드를 호출하는 대상은 항상 null이 아닌 상수에서 호출하는게 좋음
    }
}
