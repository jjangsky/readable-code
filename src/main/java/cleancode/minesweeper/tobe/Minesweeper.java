package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.game.GameInitialize;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.user.UserAction;


public class Minesweeper implements GameRunnable, GameInitialize {

    public static final char BASE_CHAR_FOR_COL = 'a';
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final GameBoard gameBoard;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    public Minesweeper(GameConfig gameConfig){
        gameBoard = new GameBoard(gameConfig.getGameLevel());
        this.inputHandler = gameConfig.getInputHandler();
        this.outputHandler = gameConfig.getOutputHandler();
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }
    @Override
    public void run() {

        outputHandler.showGameStartComments();

        while (gameBoard.isInProgress()) {
            try {
                outputHandler.showBoard(gameBoard);
                CellPosition cellInput = getCellInputFromUser();
                UserAction userActionInput = getUserActionInputFromUser();
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

        outputHandler.showBoard(gameBoard);
        if (gameBoard.isWinStatus()) {
            outputHandler.showGameWinningComment();
        }
        if (gameBoard.isLoseStatus()) {
            outputHandler.showGameLosingComment();
        }
    }

    private  void actOnCell(CellPosition cellInput, UserAction userActionInput) {
        // 각각의 상황으로 만들어 버린다.
        if (doesUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flagAt(cellInput);
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            gameBoard.openAt(cellInput);
            return;
        }

        System.out.println("잘못된 번호를 선택하셨습니다.");

    }


    private  boolean doesUserChooseToOpenCell(UserAction userActionInput) {
        return userActionInput == UserAction.OPEN;
    }

    private  boolean doesUserChooseToPlantFlag(UserAction userActionInput) {
        return userActionInput == UserAction.FLAG;
    }


    private UserAction getUserActionInputFromUser() {
        outputHandler.showCommentForSelectIngCell();
        return inputHandler.getUserActionFromUser();
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showCommentForUserAction();
        CellPosition cellPosition = inputHandler.getCellPositionFromUser();
        if(gameBoard.isInvalidCellPosition(cellPosition)){
            throw new GameException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
    }


}
