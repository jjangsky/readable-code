package cleancode.minesweeper.tobe;

public class GameException extends RuntimeException{
    // 프로그램에서 의도한 에러
    public GameException(String message){
        super(message);
    }


}
