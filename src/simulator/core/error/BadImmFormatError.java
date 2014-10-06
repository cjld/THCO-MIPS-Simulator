package simulator.core.error;

/**
 * Bad Immediate Format Error
 * @author LeeThree
 */
public class BadImmFormatError extends CompileError {

    public BadImmFormatError(String errorToken) {
        this.errorToken = errorToken;
    }

    @Override
    public String getMessage() {
        return "\"" + errorToken + "\" is not a valid immediate.";
    }
    private String errorToken;
}
