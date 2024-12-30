package cleancode.minesweeper.tobe.cell;

public abstract class Cell {

    protected static final String FLAG_SIGN = "⚑";
    protected static final String UNCHECKED_SIGN = "□";

    protected boolean isFlagged;
    protected boolean isOpened;

    /**
     * 공통적인 기능은 추상클래스에서 정의하고
     * 상속 받아서 각각 정의할 메서드에 대해서는
     * `abstract` 즉, 하위 클래스에서 재정의 한다.
     */


    public void flag() {
        this.isFlagged = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpened;
    }

    public abstract boolean isLandMine() ;
    public void open() {
        this.isOpened = true;
    }
    public boolean isOpened() {
        return isOpened;
    }

    public abstract boolean hasLandMineCount();

    public  abstract String getSigh();
}
