package simulator.core.error;

/**
 * Bad Label Format Error
 * @author LeeThree
 */
public class BadLabelFormatError extends CompileError {

    public BadLabelFormatError(String errorToken) {
        this.errorToken = errorToken;
    }

    @Override
    public String getMessage() {
        return "\"" + errorToken + "\" is not a valid label name.";
    }
    private String errorToken;
}
