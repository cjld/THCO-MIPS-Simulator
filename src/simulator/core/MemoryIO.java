package simulator.core;

import simulator.ui.Terminal;
import simulator.core.error.MemAccessViolationError;

/**
 *
 * @author LeeThree
 */
public class MemoryIO {

    public MemoryIO() {
        this.memory = new short[MAX_ADDR];
    }

    public short load(short addr) throws MemAccessViolationError {
        if (addr == 0x6000) {
            short s = (short) Terminal.getInput();
            //System.out.println("get:" + s);
            return s;
        } else if (addr < 0x4000 && addr > 0x27FF) {
            throw new MemAccessViolationError(addr);
        } {
            try {
                return memory[addr];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new MemAccessViolationError(addr);
            }
        }
    }

    public void store(short addr, short data) throws MemAccessViolationError {
        if (addr == 0x6002) {
            //System.out.print("print:" + (char) (data & 0xFF));
            Terminal.output((char) (data & 0xFF));
        } else if (addr < 0x4000) {
            throw new MemAccessViolationError(addr);
        } else {
            try {
                memory[addr] = data;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new MemAccessViolationError(addr);
            }
        }
    }

    public short get(short addr) throws ArrayIndexOutOfBoundsException {
        return memory[addr];
    }

    public void set(short addr, short data) throws ArrayIndexOutOfBoundsException {
        memory[addr] = data;
    }

    public void init(short PC) {
        Terminal.reset();
        Terminal.setTermVisible(true);
        Terminal.showMessage("> Program started at " + PC + "...\n");
    }

    public void forceStop() {
        Terminal.forceStop();
        Terminal.showMessage("\n> Program terminated by user.\n");
    }

    public void stop(short PC) {
        Terminal.showMessage("\n> Program stopped at " + PC + ".\n");
    }
    public static final int MAX_ADDR = 0x6004;
    private short memory[];
    //private Terminal term;
}
