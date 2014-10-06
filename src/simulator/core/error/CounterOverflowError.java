package simulator.core.error;

/**
 * Program Counter Overflow Error
 * @author LeeThree
 */
public class CounterOverflowError extends RuntimeError {

    public CounterOverflowError(short PC) {
        this.PC = PC;
    }

    @Override
    public String getMessage() {
        return "Program counter overflow: PC = " + PC+" .";
    }
    private short PC;
}
