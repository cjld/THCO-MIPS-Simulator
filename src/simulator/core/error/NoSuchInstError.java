package simulator.core.error;

/**
 *
 * @author LeeThree
 */
public class NoSuchInstError extends CompileError {

    public NoSuchInstError(String errorToken) {
        this.errorToken = errorToken;
    }

    @Override
    public String getMessage() {
        return "\"" + errorToken + "\" is not a valid instruction.";
    }
    private String errorToken;
}
