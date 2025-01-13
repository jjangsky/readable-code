package cleancode.studycafe.tobe.io;

import cleancode.studycafe.tobe.model.order.StudyCafePassOrder;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.StudyCafePass;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;

import java.util.List;
import java.util.Optional;

public class OutputHandler {

    public void showWelcomeMessage() {
        System.out.println("*** 프리미엄 스터디카페 ***");
    }

    public void showAnnouncement() {
        System.out.println("* 사물함은 고정석 선택 시 이용 가능합니다. (추가 결제)");
        System.out.println("* !오픈 이벤트! 2주권 이상 결제 시 10% 할인, 12주권 결제 시 15% 할인! (결제 시 적용)");
        System.out.println();
    }

    public void askPassTypeSelection() {
        System.out.println("사용하실 이용권을 선택해 주세요.");
        System.out.println("1. 시간 이용권(자유석) | 2. 주단위 이용권(자유석) | 3. 1인 고정석");
    }

    public void showPassListForSelection(List<StudyCafeSeatPass> passes) {
        System.out.println();
        System.out.println("이용권 목록");
        for (int index = 0; index < passes.size(); index++) {
            StudyCafeSeatPass pass = passes.get(index);
            System.out.println(String.format("%s. ", index + 1) + display(pass));
        }
    }

    public void askLockerPass(StudyCafeLockerPass lockerPass) {
        System.out.println();
        String askMessage = String.format(
            "사물함을 이용하시겠습니까? (%s)",
            display(lockerPass)
        );

        System.out.println(askMessage);
        System.out.println("1. 예 | 2. 아니오");
    }

    /**
     * null 값 넘겨주는거 안이뻐서 오버로딩을 활용
     */
    public void showPassOrderSummary(StudyCafePassOrder passOrder) {
        StudyCafeSeatPass selectedPass = passOrder.getSeatPass();
        // 내부에서는 optional 객체인지 알지만 외부에는 모르기 때문에 옵셔널로 받아옴
        Optional<StudyCafeLockerPass> lockerPass = passOrder.getLockerPass();

        System.out.println();
        System.out.println("이용 내역");
        System.out.println("이용권: " + display(selectedPass));
        lockerPass.ifPresent(pass ->
                System.out.println("사물함: " + display(pass))
        );

        int discountPrice = passOrder.getDiscountPrice();
        if (discountPrice > 0) {
            System.out.println("이벤트 할인 금액: " + discountPrice + "원");
        }

        int totalPrice = passOrder.getTotalPrice();
        System.out.println("총 결제 금액: " + totalPrice + "원");
        System.out.println();
    }

    public void showSimpleMessage(String message) {
        System.out.println(message);
    }

    /**
     * 하단의 두개의 `display` 메소드는 기능이 동일하다.
     * 이러한 경우에 객체의 추상화를 생각해 볼 수 있다.
     * StudyCafePass 와 StudyCafeLockerPass의 좀 더 상위 개념인
     * StudyCafe 이용권에 대한 객체를 추상화 하여
     * 그 하위로 각각 StudyCafeSeatPass라는 좌석 관련 객체와
     * StudyCafeLockerPass 라는 사물함 관련 객체를 구현할 수 있다.
     *
     * 특정 객체가 비슷한 느낌이 있어 묶고 싶을 때 그 객체를 무언가에 대한 구현체로
     * 자리 잡아놓고 그 상위 객체에 대해서 추상화를 생각하보면 생각보다 쉽다.
     */

    /*private String display(StudyCafeSeatPass pass){
        StudyCafePassType passType = pass.getPassType();
        int duration = pass.getDuration();
        int price = pass.getPrice();

        if (passType == StudyCafePassType.HOURLY) {
            return String.format("%s시간권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.WEEKLY) {
            return String.format("%s주권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.FIXED) {
            return String.format("%s주권 - %d원", duration, price);
        }
        return "";
    }

    private String display(StudyCafeLockerPass lockerPass){
        StudyCafePassType passType = lockerPass.getPassType();
        int duration = lockerPass.getDuration();
        int price = lockerPass.getPrice();

        if (passType == StudyCafePassType.HOURLY) {
            return String.format("%s시간권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.WEEKLY) {
            return String.format("%s주권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.FIXED) {
            return String.format("%s주권 - %d원", duration, price);
        }
        return "";
    }*/

    private String display(StudyCafePass pass) {
        StudyCafePassType passType = pass.getPassType();
        int duration = pass.getDuration();
        int price = pass.getPrice();

        if (passType == StudyCafePassType.HOURLY) {
            return String.format("%s시간권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.WEEKLY) {
            return String.format("%s주권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.FIXED) {
            return String.format("%s주권 - %d원", duration, price);
        }
        return "";
    }

}
