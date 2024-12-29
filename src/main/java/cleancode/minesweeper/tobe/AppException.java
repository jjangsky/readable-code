package cleancode.minesweeper.tobe;

public class AppException extends RuntimeException{
    // 프로그램에서 의도한 에러
    public AppException(String message){
        super(message);
    }
}
