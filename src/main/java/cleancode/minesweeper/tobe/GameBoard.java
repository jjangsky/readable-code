package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.*;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.CellPositions;
import cleancode.minesweeper.tobe.position.RelativePosition;

import java.util.List;


public class GameBoard {

//    private static final int LAND_MINE_COUNT = 10;
    private Cell[][] board;
    private final int getLandMineCount;

    private GameStatus gameStatus;

    public GameBoard(GameLevel gameLevel){
        initializeGameStatus();
        int rowSize = gameLevel.getRowSize();
        int colSize = gameLevel.getColSize();
        board = new Cell[rowSize][colSize];

        getLandMineCount = gameLevel.getLandMineCount();

    }

    public void initializeGame() {
        initializeGameStatus();
        CellPositions cellPositions = CellPositions.form(board);

        initializeEmptyCells(cellPositions);

        List<CellPosition> landMinePositions =  cellPositions.extractRandomPositions(getLandMineCount);
        initializeLandMineCells(landMinePositions);

        List<CellPosition> numberPositionsCandidates = cellPositions.subtract(landMinePositions);
        initializeNumberCells(numberPositionsCandidates);
    }

    private void initializeGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }

    private void initializeNumberCells(List<CellPosition> numberPositionsCandidates) {
        for(CellPosition candidatePosition : numberPositionsCandidates){

            int count = countNearByLandMines(candidatePosition);
            if(count != 0){
                updateCellAt(candidatePosition, new NumberCell(count));
            }
        }
    }

    private void initializeLandMineCells(List<CellPosition> landMinePositions) {
        for(CellPosition position : landMinePositions){
            updateCellAt(position, new LandMineCell());
        }
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for(CellPosition position : allPositions){
            updateCellAt(position, new EmptyCell());
        }
    }

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
    }

    public int getRowSize(){
        return board.length;
    }

    public int getColSize(){
        return board[0].length;
    }

    private  int countNearByLandMines(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
                .filter(this::isLandMineCell)
                .count();

        /*int count = 0;
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
        }*/
        return (int) count;
    }

    private static List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
        return RelativePosition.SURROUNDED_POSITIONS.stream()
                .filter(cellPosition::canCalculatePositionBy)
                .map(cellPosition::calculatePositionBy)
                .filter(position -> position.isRowIndexLessThan(rowSize))
                .filter(position -> position.isColIndexLessThan(colSize))
                .toList();
    }

    public boolean isLandMineCell(CellPosition cellPosition) {
        return findCell(cellPosition).isLandMine();
    }

    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
                || cellPosition.isColIndexMoreThenOrEqual(colSize);
    }

    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }

    public void flagAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.flag();

        checkIfGameIsOver();
    }

    public void openOneCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.open();
    }

    public   void openSurroundedCells(CellPosition cellPosition) {
//        if (cellPosition.getRowIndex() >= getRowSize() ||  cellPosition.getColIndex() >= getColSize()) {
//            return;
//        }  -> 하단의 스트림에서 검증처리함
        if (isOpenedCell(cellPosition)) {
            return;
        }
        if (isLandMineCell(cellPosition)) {
            return;
        }

        openOneCellAt(cellPosition);

        if (doesCellHaveLandMineCount(cellPosition)) {
            return;
        }

        /**
         * 2차원 배열의 좌표로 받는 것을 Vo 객체로 전환하여
         * CellPosition 으로 받아와서 row 와 col의 값을 대체 하는 과정에서 생긴 이슈
         *
         * 상대 좌표 객체를 만들어서 사전에 검증하는 방식을 도입
         * RelativePosition 객체 사용
         */

        calculateSurroundedPositions(cellPosition, getRowSize(), getColSize())
                .forEach(this::openSurroundedCells);

//        for(RelativePosition relativePosition : RelativePosition.SURROUNDED_POSITIONS){
//            if(canMovePosition(cellPosition, relativePosition)){
//                CellPosition nextCellPosition =  cellPosition.calculatePositionBy(relativePosition);
//                openSurroundedCells(nextCellPosition);
//            }
//        }
    }

    private boolean canMovePosition(CellPosition cellPosition, RelativePosition relativePosition) {
        return cellPosition.canCalculatePositionBy(relativePosition);
    }

    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        return findCell(cellPosition).hasLandMineCount();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        return findCell(cellPosition).isOpened();
    }

    public  boolean isAllCellChecked() {
        Cells cells = Cells.from(board);
        return cells.isAllChecked();
    }

    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSnapshot();
    }

    public boolean isInProgress() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    private  void checkIfGameIsOver() {
        if (isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private  void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN;
    }

    public void openAt(CellPosition cellInput) {
        if (isLandMineCell(cellInput)) {
            openOneCellAt(cellInput);
            changeGameStatusToLose();
            return;
        }
        openSurroundedCells(cellInput);
        checkIfGameIsOver();

    }

    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE;
    }

    public boolean isWinStatus() {
        return gameStatus == GameStatus.WIN;
    }

    public boolean isLoseStatus() {
        return gameStatus == GameStatus.LOSE;
    }
}

