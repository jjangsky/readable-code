package cleancode.minesweeper.tobe.cell;

public class EmptyCell implements Cell {
    protected static final String EMPTY_SIGN = "■";
    private final CellState cellState = CellState.initialize();

    @Override
    public void flag() {
        cellState.flag();

    }

    @Override
    public boolean isChecked() {
        return cellState.isChecked();
    }

    @Override
    public boolean isLandMine() {
        return false;
    }

    @Override
    public void open() {
        cellState.open();

    }

    @Override
    public boolean isOpened() {
        return cellState.isOpened();
    }

    @Override
    public boolean hasLandMineCount() {
        return false;
    }

    @Override
    public CellSnapshot getSnapshot() {
        if(cellState.isOpened()){
            return  CellSnapshot.ofEmpty();
        }
        if(cellState.isFlagged()){
            return CellSnapshot.ofFlag();
        }
        return CellSnapshot.ofUnchecked();
    }
}
