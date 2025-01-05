package cleancode.minesweeper.tobe.user;

public enum UserAction {

    /**
     * Enum Class를 만들 때, 값에 대한 한글 설명도 같이 첨부하면 좋다.
     */
    OPEN("셀 열기"),
    FLAG("깃발 꽂기"),
    UNKNOWN("알 수 없음");

    private final String description;

    UserAction(String description) {
        this.description = description;
    }
}
