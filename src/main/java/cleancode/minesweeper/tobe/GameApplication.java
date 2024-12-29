package cleancode.minesweeper.tobe;


public class GameApplication {
    public static void main(String[] args){
        // 첫번째, 책임 분리
        // 게임의 실행부와 게임의 진행부를 분리
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.run();

    }

}
