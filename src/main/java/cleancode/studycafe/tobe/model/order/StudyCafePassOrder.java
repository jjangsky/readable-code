package cleancode.studycafe.tobe.model.order;

import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;

import java.util.Optional;

public class StudyCafePassOrder {

    private final StudyCafeSeatPass seatPass;
    private final StudyCafeLockerPass lockerPass;

    public StudyCafePassOrder(StudyCafeSeatPass seatPass, StudyCafeLockerPass lockerPass) {
        this.seatPass = seatPass;
        this.lockerPass = lockerPass;
    }

    public static StudyCafePassOrder of(StudyCafeSeatPass seatPass, StudyCafeLockerPass lockerPass) {
        return new StudyCafePassOrder(seatPass, lockerPass);
    }

    public StudyCafeSeatPass getSeatPass() {
        return this.seatPass;
    }

    public Optional<StudyCafeLockerPass> getLockerPass() {
        // null 값일 수 도 있음
        return Optional.ofNullable(lockerPass);
    }

    public int getDiscountPrice() {
        return seatPass.getDiscountPrice();

    }

    public int getTotalPrice() {
        int lockerPassPrice = lockerPass != null ? lockerPass.getPrice() : 0;
        int totalPassPrice = seatPass.getPrice() + lockerPassPrice;

        return  totalPassPrice - getDiscountPrice();
    }
}
