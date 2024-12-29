package cleancode.minesweeper.tobe;

public class Cell {

    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□";
    private static final String EMPTY_SIGN = "■";

    private int nearbyLandMineCount;
    private boolean isLandMine;
    private boolean isFlagged;
    private boolean isOpened;

    // Cell이 가진 속성 : 근처 지뢰 숫자, 지뢰 여부
    // Cell의 상태 : 깃발 유무, 열렸다/닫혔다, 사용자가 확인함



    private Cell( int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {

        this.nearbyLandMineCount = nearbyLandMineCount;
        this.isLandMine = isLandMine;
        this.isFlagged = isFlagged;
        this.isOpened = isOpened;
    }

    // 객체를 생성할 때 정적 팩토리 메소드를 사용하면 좋다.
    // 생성자 메소드에 대해서 이름을 부여할 수 있어서 장점이 있음
    public static Cell of(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened){
        return new Cell( nearbyLandMineCount, isLandMine, isFlagged, isOpened);
    }

    /**
     * Cell 객체가 생성된 후, Flag표시에 대한 부분도 객체 내부에서 이동하여
     * 외부에 값을 부여할 수 있다.
     * 이전에는 FLAG_SIGN이라는 값을 외부에서 받아와서 객체를 생성하였다면
     * 객체 내부에 FLAG 값이 존재하니까 객체 내부 메소드에서 FLAG 값을 설정하여
     * 외부로 반환이 가능함
     */

    public static Cell create() {
        return of( 0 , false, false, false);
    }


    public void turnOnLandMine() {
        this.isLandMine = true;
    }

    public void updateNearbyLandMineCount(int count) {
        this.nearbyLandMineCount = count;
    }

    public void flag() {
        this.isFlagged = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpened;
    }

    public boolean isLandMine() {
        return isLandMine;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean hasLandMineCount() {
        return this.nearbyLandMineCount != 0;
    }

    public String getSigh(){
        if(isOpened){
            if(isLandMine){
                return LAND_MINE_SIGN;
            }
            if(hasLandMineCount()){
                return String.valueOf(nearbyLandMineCount);
            }
            return EMPTY_SIGN;
        }

        if(isFlagged){
            return FLAG_SIGN;
        }

        return UNCHECKED_SIGN;
    }
}
