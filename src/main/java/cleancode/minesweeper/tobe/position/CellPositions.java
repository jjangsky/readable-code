package cleancode.minesweeper.tobe.position;

import cleancode.minesweeper.tobe.cell.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CellPositions {
    private final List<CellPosition> positions;

    public CellPositions(List<CellPosition> positions) {
        this.positions = positions;
    }

    public static CellPositions of(List<CellPosition> positions){
        return new CellPositions(positions);
    }

    public static CellPositions form(Cell[][] board) {
        List<CellPosition> cellPositions = new ArrayList<>();

        for (int row = 0; row < board.length; row++){
            for(int col = 0; col < board[0].length; col++){
                CellPosition cellPosition = CellPosition.of(row, col);
                cellPositions.add(cellPosition);
            }
        }
        return of(cellPositions);
    }


    public List<CellPosition> getPositions() {
        // 이렇게 참조된 값을 반환했을 때 문제가 생김
//        return positions;

        return new ArrayList<>(positions);
    }

    public List<CellPosition> extractRandomPositions(int count) {
        ArrayList<CellPosition> cellPositions = new ArrayList<>(positions);
        Collections.shuffle(cellPositions);

        return cellPositions.subList(0, count);
    }

    public List<CellPosition> subtract(List<CellPosition> positionsListToSubtract) {
        ArrayList<CellPosition> cellPositions = new ArrayList<>(this.positions);

        CellPositions positionsToSubtract = CellPositions.of(positionsListToSubtract);
        
        return cellPositions.stream()
                .filter(positionsToSubtract::doesNotContain)
                .toList();
    }

    private boolean doesNotContain(CellPosition cellPosition) {
        return !positions.contains(cellPosition);
    }

}
