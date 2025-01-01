package cleancode.minesweeper.tobe.cell;

public interface Cell {

    String FLAG_SIGN = "⚑";
    String UNCHECKED_SIGN = "□";


    void flag();

    boolean isChecked();

    boolean isLandMine();

    void open();

    boolean isOpened();

    boolean hasLandMineCount();

    String getSigh();
}
