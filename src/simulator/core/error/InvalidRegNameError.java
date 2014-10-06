package simulator.core.error;

/**
 * Invalid Register Name Error
 * @author LeeThree
 */
public class InvalidRegNameError extends CompileError {

    public InvalidRegNameError(String errorToken) {
        this.errorToken = errorToken;
    }

    @Override
    public String getMessage() {
        return "\"" + errorToken + "\" is not a valid general register.";
    }
    private String errorToken;
}
