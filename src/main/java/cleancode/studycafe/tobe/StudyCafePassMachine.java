package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();
    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

    public void run() {
        try {
            outputHandler.showWelcomeMessage();
            outputHandler.showAnnouncement();

            StudyCafePass selectedPass = selectPass();

            Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);

            /**
             * Optional 객체로 받았지만, 인자로 Optional 객체를 넘겨주는것은 안티 패턴이다.
             * 그렇기 때문에 Optional 객체로 넘겨주는 것이 아닌,
             * 해당 값을 검증 후, 넘겨주는것이 좋다.
             * -> Optional 객체 자체가 null값이 되는 경우도 있음
             */
            optionalLockerPass.ifPresentOrElse(
                    // optional 객체의 검증 분기 처리, 첫 번째 인자는 값이 있을 경우, 두 번째 인자는 값이 없을 경우
                    lockerPass -> outputHandler.showPassOrderSummary(selectedPass, lockerPass),
                    () -> outputHandler.showPassOrderSummary(selectedPass)
            );
//            if(optionalLockerPass.isPresent()) {
//                outputHandler.showPassOrderSummary(selectedPass, optionalLockerPass.get());
//            } else {
//                outputHandler.showPassOrderSummary(selectedPass, null);
//            }
        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private StudyCafePass selectPass() {
        outputHandler.askPassTypeSelection();
        StudyCafePassType studyCafePassType = inputHandler.getPassTypeSelectingUserAction();

        List<StudyCafePass> passCandidates = findPassCandidateBy(studyCafePassType);

        outputHandler.showPassListForSelection(passCandidates);
        return inputHandler.getSelectPass(passCandidates);
    }

    private List<StudyCafePass> findPassCandidateBy(StudyCafePassType studyCafePassType) {
        List<StudyCafePass> allPasses = studyCafeFileHandler.readStudyCafePasses();
        return allPasses.stream()
                .filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
                .toList();
    }

    private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafePass selectedPass) {
        /**
         * null 값 자체를 반환 하는것은 안티 패턴이다.
         * Optional 객체를 사용하여 반환을 하지만 Optional 객체를 잘 알고 사용을 해야한다.
         */
        if(selectedPass.getPassType() != StudyCafePassType.FIXED) {
            return Optional.empty();
        }

        StudyCafeLockerPass lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

        if (lockerPassCandidate != null) {
            outputHandler.askLockerPass(lockerPassCandidate);
            boolean isLockerSelected = inputHandler.getLockerSelection();

            if (isLockerSelected) {
                return Optional.of(lockerPassCandidate);
            }
        }

        return Optional.empty();
    }

    private StudyCafeLockerPass findLockerPassCandidateBy(StudyCafePass pass) {
        List<StudyCafeLockerPass> allLockerPasses = studyCafeFileHandler.readLockerPasses();

        return allLockerPasses.stream()
                .filter(lockerPass ->
                        lockerPass.getPassType() == pass.getPassType()
                                && lockerPass.getDuration() == pass.getDuration()
                )
                .findFirst()
                .orElse(null);
    }

}
