package simulator.core.error;

/**
 * Label Too Far Away Error
 * @author LeeThree
 */
public class LabelTooFarAwayError extends CompileError {

    public LabelTooFarAwayError(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String getMessage() {
        return "Label \"" + labelName + "\" is too far away.";
    }
    private String labelName;
}
