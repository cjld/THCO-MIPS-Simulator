package simulator.core.error;

/**
 * Immediate Too Large or Too Small Error
 * @author LeeThree
 */
public class ImmTooLargeError extends CompileError {

    public ImmTooLargeError(int givenImmediate, int expectedMax, int expectedMin) {
        this.givenImmediate = givenImmediate;
        this.expectedMax = expectedMax;
        this.expectedMin = expectedMin;
    }

    @Override
    public String getMessage() {
        return "Immediate " + givenImmediate + " is too large or too small, number in [" + expectedMax + ", " + expectedMin + "] expected.";
    }
    private int givenImmediate;
    private int expectedMax;
    private int expectedMin;
}
