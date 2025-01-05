package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;

public interface CellSignProvidable {
    /**
     * 어떤 CellSnapshot을 넘겨주었을 때 sign을 반환
     */

    boolean supports(CellSnapshot cellSnapshot);
    String provide(CellSnapshot cellSnapshot);
}
