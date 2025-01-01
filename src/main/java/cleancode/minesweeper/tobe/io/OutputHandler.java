package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;


public interface OutputHandler {

     void showGameStartComments();

      void showBoard(GameBoard board);

     void showGameWinningComment() ;

     void showGameLosingComment() ;

     void showCommentForSelectIngCell();

     void showCommentForUserAction() ;

     void showExceptionMessage(Exception e) ;

     void showSimpleMessage(String message);
}
