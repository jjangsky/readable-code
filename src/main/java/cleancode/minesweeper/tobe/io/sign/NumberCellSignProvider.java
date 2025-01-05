package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;

public class NumberCellSignProvider implements CellSignProvidable {
    @Override
    public String provide(CellSnapshot cellSnapshot) {
        return String.valueOf(cellSnapshot.getNearbyLandMineCount());
    }

    @Override
    public boolean supports(CellSnapshot cellSnapshot) {
        return cellSnapshot.isSameStatus(CellSnapshotStatus.NUMBER);
    }
}
