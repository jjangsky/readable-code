package cleancode.studycafe.tobe.model.pass;

import java.util.Set;

public enum StudyCafePassType {

    HOURLY("시간 단위 이용권"),
    WEEKLY("주 단위 이용권"),
    FIXED("1인 고정석");

    /**
     * 고정석인 경우에만 사물함을 사용할 수 있다.
     * 즉, 고정석인 것을 물어볼 수 있냐 보다 조금 더 높은 추상화 레벨인
     * 사물함을 사용할 수 있냐 로 바꿔 도메인에 접근한다.
     *
     * 하단의 Set으로 관리하는 필드는 사물함을 사용할 수 있는 이용권의 종류를 관리
     */
    private static final Set<StudyCafePassType> LOCKER_TYPES = Set.of(StudyCafePassType.FIXED);
    private final String description;

    StudyCafePassType(String description) {
        this.description = description;
    }

    public boolean isLockerType() {
        return LOCKER_TYPES.contains(this);
    }
    public boolean isNotLockerType() {
        return !isLockerType();
    }
}
