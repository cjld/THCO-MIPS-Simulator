package simulator.core.error;

import simulator.core.Misc;

/**
 * Memory Access Violation Runtime Error
 * @author LeeThree
 */
public class MemAccessViolationError extends RuntimeError {

    public MemAccessViolationError(short requestAddr) {
        this.requestAddr = requestAddr;
    }

    @Override
    public String getMessage() {
        return "Memory address " + requestAddr + " (0x" + Misc.toHex(requestAddr) + ") is not accessible.";
    }
    private short requestAddr;
}
