package simulator.core.error;

/**
 *
 * @author LeeThree
 */
public class UndefinedLabelError extends CompileError {

    public UndefinedLabelError(String errorToken) {
        this.errorToken = errorToken;
    }

    @Override
    public String getMessage() {
        return "Label \"" + errorToken + "\" is undefined.";
    }
    private String errorToken;
}
