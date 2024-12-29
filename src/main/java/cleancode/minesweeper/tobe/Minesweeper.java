package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

import java.util.Arrays;
import java.util.Random;

public class Minesweeper {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    private final GameBoard gameBoard = new GameBoard(BOARD_ROW_SIZE, BOARD_COL_SIZE);
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
    private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배
    public void run() {

        consoleOutputHandler.showGameStartComments();
        gameBoard.initializeGame();

        while (true) {
            try {

                consoleOutputHandler.showBoard(gameBoard);

                if (doesUserWinTheGame()) {
                    consoleOutputHandler.printGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) {
                    consoleOutputHandler.printGameLosingComment();
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            }catch (GameException e){
                // 예상한 오류
                consoleOutputHandler.printExceptionMessage(e);
            }catch (Exception e){
                // 예상치 못한 오류
                consoleOutputHandler.printSimpleMessage("프로그램에 문제가 생겼습니다.");
                e.printStackTrace();
            }
        }
    }

    private  void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);
        // 각각의 상황으로 만들어 버린다.
        if (doesUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flag(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (gameBoard.isLandMineCell(selectedRowIndex, selectedColIndex)) {
                gameBoard.open(selectedRowIndex, selectedColIndex);
                changeGameStatusToLose();
                return;
            }
            gameBoard.openSurroundedCells(selectedRowIndex, selectedColIndex);
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

    private  int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private  int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private  String getUserActionInputFromUser() {
        consoleOutputHandler.printCommentForSelectIngCell();
        return consoleInputHandler.getUserInput();
    }

    private  String getCellInputFromUser() {
        consoleOutputHandler.printCommentForUserAction();
        return consoleInputHandler.getUserInput();
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


    private  int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) -1;
        if(rowIndex > BOARD_ROW_SIZE){
            throw new GameException("잘못된 입력입니다");
        }
        return rowIndex;
    }

    private  int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                throw new GameException("잘못된 입력입니다.");
        }
    }






}
