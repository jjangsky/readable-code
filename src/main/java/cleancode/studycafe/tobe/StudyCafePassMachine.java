package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.io.provider.LockerPassFileReader;
import cleancode.studycafe.tobe.io.provider.SeatPassFileReader;
import cleancode.studycafe.tobe.model.order.StudyCafePassOrder;
import cleancode.studycafe.tobe.model.pass.*;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPasses;
import cleancode.studycafe.tobe.provider.LockerPassProvider;
import cleancode.studycafe.tobe.provider.SeatPassProvider;

import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

    private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
    private final SeatPassProvider seatPassProvider;

    private final LockerPassProvider lockerPassProvider;

    public StudyCafePassMachine(SeatPassProvider seatPassProvider, LockerPassProvider lockerPassProvider){
        this.seatPassProvider = seatPassProvider;
        this.lockerPassProvider = lockerPassProvider;
    }

    public void run() {
        try {
            ioHandler.showWelcomeMessage();
            ioHandler.showAnnouncement();

            StudyCafeSeatPass selectedPass = selectPass();

            Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);

            StudyCafePassOrder passOrder = StudyCafePassOrder.of(
                    selectedPass,
                    optionalLockerPass.orElse(null)
            );

            ioHandler.showPassOrderSummary(passOrder);

            /**
             * Optional 객체로 받았지만, 인자로 Optional 객체를 넘겨주는것은 안티 패턴이다.
             * 그렇기 때문에 Optional 객체로 넘겨주는 것이 아닌,
             * 해당 값을 검증 후, 넘겨주는것이 좋다.
             * -> Optional 객체 자체가 null값이 되는 경우도 있음
             */
           /* optionalLockerPass.ifPresentOrElse(
                    // optional 객체의 검증 분기 처리, 첫 번째 인자는 값이 있을 경우, 두 번째 인자는 값이 없을 경우
                    lockerPass -> ioHandler.showPassOrderSummary(selectedPass, lockerPass),
                    () -> ioHandler.showPassOrderSummary(selectedPass)
                    해당 로직은 showPassOrderSummary() 로 전환
            ); */
//            if(optionalLockerPass.isPresent()) {
//                outputHandler.showPassOrderSummary(selectedPass, optionalLockerPass.get());
//            } else {
//                outputHandler.showPassOrderSummary(selectedPass, null);
//            }
        } catch (AppException e) {
            ioHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private StudyCafeSeatPass selectPass() {
        StudyCafePassType passType = ioHandler.askPassTypeSelection();
        List<StudyCafeSeatPass> passCandidates = findPassCandidateBy(passType);

        return ioHandler.askPassSelecting(passCandidates);
    }

    private List<StudyCafeSeatPass> findPassCandidateBy(StudyCafePassType studyCafePassType) {
        /**
         * List<StudyCafePass> 대신 StudyCafePasses라는 일급 컬렉션을 사용하여
         * 컬렉션에 대한 가공로직이 일급 컬렉션이 담당하게 되는 장점이 있음
         */
        StudyCafeSeatPasses allPasses = seatPassProvider.getSeatPasses();

        return allPasses.findPassBy(studyCafePassType);
    }

    private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafeSeatPass selectedPass) {
        /**
         * null 값 자체를 반환 하는것은 안티 패턴이다.
         * Optional 객체를 사용하여 반환을 하지만 Optional 객체를 잘 알고 사용을 해야한다.
         */
        if(selectedPass.cannotUseLocker()) {
            return Optional.empty();
        }

        Optional<StudyCafeLockerPass> lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

        if (lockerPassCandidate.isPresent()) {
            StudyCafeLockerPass lockerPass = lockerPassCandidate.get();

            boolean isLockerSelected = ioHandler.askLockerPass(lockerPass);

            if (isLockerSelected) {
                return Optional.of(lockerPass);
            }
        }

        return Optional.empty();
    }

    private Optional<StudyCafeLockerPass> findLockerPassCandidateBy(StudyCafeSeatPass pass) {
        StudyCafeLockerPasses allLockerPasses = lockerPassProvider.getSeatPasses();

        return allLockerPasses.findLockerPassBy(pass);
    }

}
