package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameInitialize;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;


public class Minesweeper implements GameRunnable, GameInitialize {

    public static final char BASE_CHAR_FOR_COL = 'a';
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final GameBoard gameBoard;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler){
        gameBoard = new GameBoard(gameLevel);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }
    @Override
    public void run() {

        outputHandler.showGameStartComments();

        while (true) {
            try {

                outputHandler.showBoard(gameBoard);

                if (doesUserWinTheGame()) {
                    outputHandler.showGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) {
                    outputHandler.showGameLosingComment();
                    break;
                }

                CellPosition cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            }catch (GameException e){
                // 예상한 오류
                outputHandler.showExceptionMessage(e);
            }catch (Exception e){
                // 예상치 못한 오류
                outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
                e.printStackTrace();
            }
        }
    }

    private  void actOnCell(CellPosition cellInput, String userActionInput) {
        // 각각의 상황으로 만들어 버린다.
        if (doesUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flagAt(cellInput);
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (gameBoard.isLandMineCell(cellInput)) {
                gameBoard.openAt(cellInput);
                changeGameStatusToLose();
                return;
            }
            gameBoard.openSurroundedCells(cellInput);
            checkIfGameIsOver();
            return;
        }

        System.out.println("잘못된 번호를 선택하셨습니다.");

    }

    private  void changeGameStatusToLose() {
        gameStatus = -1;
    }


    private  boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private  boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }


    private  String getUserActionInputFromUser() {
        outputHandler.showCommentForSelectIngCell();
        return inputHandler.getUserInput();
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showCommentForUserAction();
        CellPosition cellPosition = inputHandler.getCellPositionFromUser();
        if(gameBoard.isInvalidCellPosition(cellPosition)){
            throw new GameException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
    }

    private  boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private  boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    /**
     * 중요!
     * 하나의 메소드는 하나의 의미만 가져야 한다.
     * 리팩토링 전에 분리한 메소드는 isAllCellOpend로 해당 메소드가 Cell이 전부
     * 열렸는지 확인하고 열려 있으면 `gameStatus`의 값을 변경 하는 것 이었는데
     * 두 가지 역할을 하여 isAllCellOpened 메소드를 추가하여 분리
     */

    private  void checkIfGameIsOver() {
        if (gameBoard.isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private  void changeGameStatusToWin() {
        gameStatus = 1;
    }


}
