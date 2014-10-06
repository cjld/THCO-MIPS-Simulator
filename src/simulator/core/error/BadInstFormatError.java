package simulator.core.error;

/**
 * Bad Instruction Format Error
 * @author LeeThree
 */
public class BadInstFormatError extends CompileError {

    public BadInstFormatError(int givenArguNum, int expectedArguNum) {
        this.givenArguNum = givenArguNum;
        this.expectedArguNum = expectedArguNum;
    }

    @Override
    public String getMessage() {
        return "Instruction format incompatible: " + givenArguNum + " arguments found, " + expectedArguNum + " expected.";
    }
    private int givenArguNum;
    private int expectedArguNum;
}
