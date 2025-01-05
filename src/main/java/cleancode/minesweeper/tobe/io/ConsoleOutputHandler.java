package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;
import cleancode.minesweeper.tobe.io.sign.*;
import cleancode.minesweeper.tobe.position.CellPosition;

import java.util.List;
import java.util.stream.IntStream;

public class ConsoleOutputHandler implements OutputHandler{

    private final CellSignFinder cellSignFinder = new CellSignFinder();

    @Override
    public void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
    @Override
    public  void showBoard(GameBoard board) {
        String joiningAlphbets = generateColAlphabets(board);


//        System.out.println("   a b c d e f g h i j");
        System.out.println("    "+ joiningAlphbets);
        for (int row = 0; row < board.getRowSize(); row++) {
            System.out.printf("%2d  ", row + 1);
            for (int col = 0; col < board.getColSize(); col++) {
                CellPosition cellPosition = CellPosition.of(row, col);

                CellSnapshot snapshot = board.getSnapshot(cellPosition);
//                String cellSign = cellSignFinder.findCellSignFrom(snapshot);
                String cellSign = CellSignProvider.findCellSignFrom(snapshot);
//

                System.out.println(snapshot + "");


//                System.out.print(board.getSign(cellPosition) + " ");
                System.out.print(cellSign + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

//    private String decideCellSignFrom(CellSnapshot snapshot) {
//        CellSignFinder cellSignFinder = new CellSignFinder();
//        return cellSignFinder.findCellSignFrom(snapshot);
//        CellSnapshotStatus status = snapshot.getStatus();
//        List<CellSignProvidable> cellSignProviders = List.of(
//                new EmptyCellSignProvider(),
//                new FlagCellSignProvider(),
//                new LandMineCellSignProvider(),
//                new NumberCellSignProvider(),
//                new UnCheckedCellSignProvider()
//        );
//        return cellSignProviders.stream()
//                .filter(provider -> provider.supports(snapshot))
//                .findFirst()
//                .map(provider -> provider.provide(snapshot))
//                .orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀 입니다."));


        /*if(status == CellSnapshotStatus.EMPTY){
            EmptyCellSignProvider cellSignProvider = new EmptyCellSignProvider();
            return cellSignProvider.provide(snapshot);
        }
        if(status == CellSnapshotStatus.FLAG){
            FlagCellSignProvider cellSignProvider = new FlagCellSignProvider();
            return cellSignProvider.provide(snapshot);
        }
        if(status == CellSnapshotStatus.LAND_MINE){
            LandMineCellSignProvider cellSignProvider = new LandMineCellSignProvider();
            return cellSignProvider.provide(snapshot);
        }
        if(status == CellSnapshotStatus.NUMBER){
            NumberCellSignProvider cellSignProvider = new NumberCellSignProvider();
            return cellSignProvider.provide(snapshot);
        }
        if(status == CellSnapshotStatus.UNCHECKED){
            UnCheckedCellSignProvider cellSignProvider = new UnCheckedCellSignProvider();
            return cellSignProvider.provide(snapshot);
        }
        throw new IllegalArgumentException("확인할 수 없는 셀 입니다.");
    }*/
//    }

    private String generateColAlphabets(GameBoard board) {
        List<String> alphabets =  IntStream.range(0, board.getColSize())
            .mapToObj(index -> (char)('a' + index))
            .map(Object::toString)
            .toList();
        return String.join(" ", alphabets);
    }
    @Override
    public void showGameWinningComment() {
        System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
    }
    @Override
    public void showGameLosingComment() {
        System.out.println("지뢰를 밟았습니다. GAME OVER!");
    }
    @Override
    public void showCommentForSelectIngCell() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
    }
    @Override
    public void showCommentForUserAction() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
    }
    @Override
    public void showExceptionMessage(Exception e) {
        System.out.println(e.getMessage());
    }
    @Override
    public void showSimpleMessage(String message) {
        System.out.println(message);
    }
}
