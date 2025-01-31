package cleancode.minesweeper.tobe.cell;

public class CellSnapshot {
    private  final CellSnapshotStatus status;
    private final int nearbyLandMineCount;

    public int getNearbyLandMineCount() {
        return nearbyLandMineCount;
    }

    public CellSnapshot(CellSnapshotStatus status, int nearbyLandMineCount) {

        this.status = status;
        this.nearbyLandMineCount = nearbyLandMineCount;
    }

    public static CellSnapshot of(CellSnapshotStatus status, int nearbyLandMineCount){
        return new CellSnapshot(status, nearbyLandMineCount);
    }

    public static CellSnapshot ofEmpty(){
        return of(CellSnapshotStatus.EMPTY, 0);
    }
    public static CellSnapshot ofFlag(){
        return of(CellSnapshotStatus.FLAG, 0);
    }
    public static CellSnapshot ofLandMine(){
        return of(CellSnapshotStatus.LAND_MINE, 0);
    }
    public static CellSnapshot ofNumber(int nearbyLandMineCount){
        return of(CellSnapshotStatus.NUMBER, nearbyLandMineCount);
    }
    public static CellSnapshot ofUnchecked(){
        return of(CellSnapshotStatus.EMPTY, 0);
    }

    public CellSnapshotStatus getStatus() {
        return status;
    }

    public boolean isSameStatus(CellSnapshotStatus cellSnapshotStatus) {
        return this.status == cellSnapshotStatus;
    }
}
