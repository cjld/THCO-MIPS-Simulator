package simulator.core;

import java.util.HashMap;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import simulator.ui.Console;
import simulator.core.error.BadLabelFormatError;
import simulator.core.error.RuntimeError;
import simulator.core.error.CompileError;
import simulator.core.error.CounterOverflowError;
import simulator.ui.UIConfig;

/**
 *
 * @author LeeThree
 */
public class Simulator {

    public Simulator() {
        this.register = new short[8];
        this.memory = new MemoryIO();
        compiled = false;
        running = false;
        lineNumMap = new HashMap();
        uiConfig = new UIConfig();
        uiConfig.memory = this.memory;
    }

//    public static void main(String[] args) {
//        String line = new String(" a bc : abcd");
//        int colonPos = line.indexOf(':');
//        if (colonPos != -1) {
//            String label = line.substring(0, colonPos).trim();
//            System.out.println(label);
//        }
//    }
    public void stop() {
        cancelled = true;
        memory.forceStop();
        if (running) {
            running = false;
            Console.log("Program terminated.", "emphasis");
        }
        updateUI();

    }

    public UIConfig getUiConfig() {
        return uiConfig;
    }

    public void compile(String sourceCode) {
        short memoryPointer = 0;
        int errorCount = 0;
        PC = 0;
        Console.clear();
        Console.log("Start compiling...", "info");
        lineNumMap.clear();
        try {
            Compiler compiler = new Compiler();
            lines = compiler.preprocess(sourceCode);
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                //Console.log("line " + (i + 1) + ":\t" + line);
                if (!(line.matches("\\s*"))) {
                    try {
                        memory.set(memoryPointer, Misc.encode(compiler.translate(line, memoryPointer)));
                        lineNumMap.put(memoryPointer, i);
                        //Console.log(memoryPointer + ":\t" + Misc.decode(memory.get(memoryPointer)), "info");
                        memoryPointer++;
                    } catch (CompileError error) {
                        errorCount++;
                        Console.log("Compile Error in line " + (i + 1) + ": \t" + line, "error");
                        Console.log(error.getMessage(), "error");
                    }
                }
                memory.set(memoryPointer, (short) 0);
            }
        } catch (BadLabelFormatError error) {
            errorCount++;
            Console.log(error.getMessage(), "error");
        }
        if (errorCount != 0) {
            Console.log("Compiling failed with " + errorCount + " error(s).", "error");
            compiled = false;
        } else if (memoryPointer == 0) {
            Console.log("Compiling failed: nothing compiled.", "error");
            compiled = false;
        } else {
            Console.log("Compiling finished successfully.", "emphasis");
            compiled = true;
        }
        updateUI();
    }

    public void step() {
        if (compiled == false) {
            Console.log("Please compile before running this program.", "error");
            return;
        }
        cancelled = false;
        short currentInst = 0x0800;
        if (!running) {
            PC = 0;
            running = true;
            Console.clear();
            Console.log("Running program...", "info");
            memory.init(PC);
        }
        try {
            currentInst = memory.get(PC);
            if (currentInst != 0) {
                PC++;
                if (PC < 0 || PC > Short.MAX_VALUE) {
                    throw new CounterOverflowError(PC);
                }
                execute(currentInst);
                Console.log("Program paused at " + PC, "info");
            } else {
                Console.log("Program exits.", "emphasis");
                memory.stop(PC);
                running = false;
            }
        } catch (RuntimeError error) {
            Console.log("Runtime Error in instrucion:\t" + Misc.decode(currentInst), "error");
            Console.log(error.getMessage(), "error");
            Console.log("Program exits.", "emphasis");
            memory.stop(PC);
            running = false;
        }
        updateUI();
    }

    public void run() {
        if (compiled == false) {
            Console.log("Please compile before running this program.", "error");
            return;
        }
        cancelled = false;
        short currentInst = 0x0800;
        //int counter = 0;
        try {
            if (!running) {
                PC = 0;
                running = true;
                Console.clear();
                Console.log("Running program...", "info");
                memory.init(PC);
            }
            while (!cancelled) {
                //System.out.println(PC);
                currentInst = memory.get(PC);
                if (currentInst == 0) {
                    break;
                }
                PC++;
                //counter++;
                if (PC < 0 || PC > Short.MAX_VALUE) {
                    throw new CounterOverflowError(PC);
                }
                execute(currentInst);
                if (uiConfig.breakpoints.contains((int) PC)) {
                    Console.log("Program paused on breakpoint at " + PC, "emphasis");
                    updateUI();
                    return;
                }
            }
        //memory.stop(PC);
        } catch (RuntimeError error) {
            Console.log("Runtime Error in instrucion:\t" + Misc.decode(currentInst), "error");
            Console.log(error.getMessage(), "error");
        }
        memory.stop(PC);
        Console.log("Program exits.", "emphasis");
        running = false;
        updateUI();
    }

    public String getBinaryCode() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < lineNumMap.size(); i++) {
            buffer.append(Misc.decode(memory.get((short) i))+"\n");
        }
        return buffer.toString();
    }

    public TableModel getInstTabel() {
        return new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return lineNumMap.size() + 1;
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return getValueAt(0, columnIndex).getClass();
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Line No.";
                    case 1:
                        return "Assembly";
                    case 2:
                        return "Code";
                    case 3:
                        return "Address";
                    default:
                        return null;
                }
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (rowIndex == getRowCount() - 1) {
                    switch (columnIndex) {
                        case 2:
                            return Misc.decode(memory.get((short) rowIndex));
                        case 3:
                            return rowIndex;
                        default:
                            return null;
                    }
                }
                switch (columnIndex) {
                    case 0:
                        return lineNumMap.get((short) rowIndex) + 1;
                    case 1:
                        return lines[lineNumMap.get((short) rowIndex)].replace('\t', ' ');
                    case 2:
                        return Misc.decode(memory.get((short) rowIndex));
                    case 3:
                        return rowIndex;
                    default:
                        return null;
                }
            }
        };
    }

    private void execute(short instruction) throws RuntimeError {
        execute(Misc.decode(instruction));
    }

    private void execute(String inst) throws RuntimeError {
        //System.out.println(inst);
        if (inst.startsWith("01001")) {              // ADDIU
            register[getRegIndex(inst.substring(5, 8))] += Misc.signExtend(inst.substring(8));
        } else if (inst.startsWith("01000")) {       // ADDIU3
            register[getRegIndex(inst.substring(8, 11))] = (short) (register[getRegIndex(inst.substring(5, 8))] + Misc.signExtend(inst.substring(12)));
        } else if (inst.startsWith("00000")) {       // ADDSP3
            register[getRegIndex(inst.substring(5, 8))] = (short) (SP + Misc.signExtend(inst.substring(8)));
        } else if (inst.startsWith("01100011")) {       // ADDSP
            SP += Misc.signExtend(inst.substring(8));
        } else if (inst.startsWith("11100") && inst.endsWith("01")) {       // ADDU
            register[getRegIndex(inst.substring(11, 14))] = (short) (register[getRegIndex(inst.substring(5, 8))] + register[getRegIndex(inst.substring(8, 11))]);
        } else if (inst.startsWith("11101") && inst.endsWith("01100")) {       // AND
            register[getRegIndex(inst.substring(5, 8))] &= register[getRegIndex(inst.substring(8, 11))];
        } else if (inst.startsWith("00010")) {       // B
            PC += Misc.signExtend(inst.substring(5));
        } else if (inst.startsWith("00100")) {       // BEQZ
            if (register[getRegIndex(inst.substring(5, 8))] == 0) {
                PC += Misc.signExtend(inst.substring(8));
            }
        } else if (inst.startsWith("00101")) {       // BNEZ
            if (register[getRegIndex(inst.substring(5, 8))] != 0) {
                PC += Misc.signExtend(inst.substring(8));
            }
        } else if (inst.startsWith("01100000")) {       // BTEQZ
            if (T == 0) {
                PC += Misc.signExtend(inst.substring(8));
            }
        } else if (inst.startsWith("01100001")) {       // BTNEZ
            if (T != 0) {
                PC += Misc.signExtend(inst.substring(8));
            }
        } else if (inst.startsWith("11101") && inst.endsWith("01010")) {       // CMP
            if (register[getRegIndex(inst.substring(5, 8))] == register[getRegIndex(inst.substring(8, 11))]) {
                T = 0;
            } else {
                T = 1;
            }
        } else if (inst.startsWith("01110")) {       // CMPI
            if (register[getRegIndex(inst.substring(5, 8))] == Misc.signExtend(inst.substring(8))) { // TODO not sure
                T = 0;
            } else {
                T = 1;
            }
        } else if (inst.startsWith("11111")) {       // INT
            // TODO
            Console.log("INT not supported yet.", "warning");
        } else if (inst.startsWith("11101") && inst.endsWith("11000000")) {       // JALR
            PC = register[getRegIndex(inst.substring(5, 8))];
            RA = PC;
        } else if (inst.startsWith("11101") && inst.endsWith("00000000")) {       // JR
            PC = register[getRegIndex(inst.substring(5, 8))];
        } else if (inst.startsWith("11101") && inst.endsWith("00100000")) {       // JRRA
            PC = RA;
        } else if (inst.startsWith("01101")) {       // LI
            register[getRegIndex(inst.substring(5, 8))] = Misc.signExtend(inst.substring(8));
        } else if (inst.startsWith("10011")) {       // LW
            register[getRegIndex(inst.substring(8, 11))] = memory.load((short) (register[getRegIndex(inst.substring(5, 8))] + Misc.zeroExtend(inst.substring(11))));
        } else if (inst.startsWith("10010")) {       // LW-SP
            register[getRegIndex(inst.substring(5, 8))] = memory.load((short) (SP + Misc.zeroExtend(inst.substring(8))));
        } else if (inst.startsWith("11110") && inst.endsWith("00000000")) {       // MFIH
            register[getRegIndex(inst.substring(5, 8))] = IH;
        } else if (inst.startsWith("11101") && inst.endsWith("01000000")) {       // MFPC
            register[getRegIndex(inst.substring(5, 8))] = PC;
        } else if (inst.startsWith("01100111")) {       // MOVE
            register[getRegIndex(inst.substring(8, 11))] = register[getRegIndex(inst.substring(13, 16))];
        } else if (inst.startsWith("11110") && inst.endsWith("00000001")) {       // MTIH
            IH = register[getRegIndex(inst.substring(5, 8))];
        } else if (inst.startsWith("01100100")) {       // MTSP
            SP = register[getRegIndex(inst.substring(8, 11))];
        } else if (inst.startsWith("11101") && inst.endsWith("01011")) {       // NEG
            register[getRegIndex(inst.substring(5, 8))] = (short) -register[getRegIndex(inst.substring(8, 11))];
        } else if (inst.startsWith("11101") && inst.endsWith("01111")) {       // NOT
            register[getRegIndex(inst.substring(5, 8))] = (short) ~register[getRegIndex(inst.substring(8, 11))];
        } else if (inst.startsWith("11101") && inst.endsWith("01101")) {       // OR
            register[getRegIndex(inst.substring(5, 8))] |= register[getRegIndex(inst.substring(8, 11))];
        } else if (inst.startsWith("00110") && inst.endsWith("00")) {       // SLL
            int imm = Misc.zeroExtend(inst.substring(11, 14));
            register[getRegIndex(inst.substring(5, 8))] = (short) (register[getRegIndex(inst.substring(8, 11))] << (imm == 0 ? 8 : imm));
        } else if (inst.startsWith("11101") && inst.endsWith("00100")) {       // SLLV
            register[getRegIndex(inst.substring(8, 11))] <<= register[getRegIndex(inst.substring(5, 8))];
        } else if (inst.startsWith("11101") && inst.endsWith("00010")) {       // SLT
            if (register[getRegIndex(inst.substring(5, 8))] < register[getRegIndex(inst.substring(8, 11))]) {
                T = 1;
            } else {
                T = 0;
            }
        } else if (inst.startsWith("01010")) {       // SLTI
            if (register[getRegIndex(inst.substring(5, 8))] < Misc.signExtend(inst.substring(8))) {
                T = 1;
            } else {
                T = 0;
            }
        } else if (inst.startsWith("11101") && inst.endsWith("00011")) {       // SLTU
            if (Misc.unsignedLess(register[getRegIndex(inst.substring(5, 8))], register[getRegIndex(inst.substring(8, 11))])) {
                T = 1;
            } else {
                T = 0;
            }
        } else if (inst.startsWith("11101") && inst.endsWith("00011")) {       // SLTUI
            if (Misc.unsignedLess(register[getRegIndex(inst.substring(5, 8))], Misc.zeroExtend(inst.substring(8)))) {
                T = 1;
            } else {
                T = 0;
            }
        } else if (inst.startsWith("00110") && inst.endsWith("11")) {       // SRA
            int imm = Misc.zeroExtend(inst.substring(11, 14));
            register[getRegIndex(inst.substring(5, 8))] = (short) (register[getRegIndex(inst.substring(8, 11))] >> (imm == 0 ? 8 : imm));
        } else if (inst.startsWith("11101") && inst.endsWith("00111")) {       // SRAV
            register[getRegIndex(inst.substring(8, 11))] >>= register[getRegIndex(inst.substring(5, 8))];
        } else if (inst.startsWith("00110") && inst.endsWith("10")) {       // SRL
            int imm = Misc.zeroExtend(inst.substring(11, 14));
            register[getRegIndex(inst.substring(5, 8))] = Misc.logicalShiftRight(register[getRegIndex(inst.substring(8, 11))], (short) (imm == 0 ? 8 : imm));
        } else if (inst.startsWith("11101") && inst.endsWith("00110")) {       // SRLV
            int rx = getRegIndex(inst.substring(8, 11));
            register[rx] = Misc.logicalShiftRight(register[rx], register[getRegIndex(inst.substring(5, 8))]);
        } else if (inst.startsWith("11100") && inst.endsWith("11")) {       // SUBU
            register[getRegIndex(inst.substring(11, 14))] = (short) (register[getRegIndex(inst.substring(5, 8))] - register[getRegIndex(inst.substring(8, 11))]);
        } else if (inst.startsWith("11011")) {       // SW
            memory.store((short) (register[getRegIndex(inst.substring(5, 8))] + Misc.zeroExtend(inst.substring(11))), register[getRegIndex(inst.substring(8, 11))]);
        } else if (inst.startsWith("01100010")) {       // SW-RS
            memory.store((short) (SP + Misc.zeroExtend(inst.substring(8))), RA);
        } else if (inst.startsWith("11010")) {       // SW-SP
            memory.store((short) (SP + Misc.zeroExtend(inst.substring(8))), register[getRegIndex(inst.substring(5, 8))]);
        } else if (inst.startsWith("11101") && inst.endsWith("01110")) {       // XOR
            register[getRegIndex(inst.substring(5, 8))] ^= register[getRegIndex(inst.substring(8, 11))];
        } else if (inst.startsWith("00001")) {       // NOP
            // do nothing
        }
    }

    private int getRegIndex(String regName) {
        if (regName.equals("000")) {
            return 0;
        } else if (regName.equals("001")) {
            return 1;
        } else if (regName.equals("010")) {
            return 2;
        } else if (regName.equals("011")) {
            return 3;
        } else if (regName.equals("100")) {
            return 4;
        } else if (regName.equals("101")) {
            return 5;
        } else if (regName.equals("110")) {
            return 6;
        } else if (regName.equals("111")) {
            return 7;
        } else {
            return 0;
        }
    }

    private void updateUI() {
        uiConfig.registers = register;
        uiConfig.PC = PC;
        uiConfig.SP = SP;
        uiConfig.IH = IH;
        uiConfig.RA = RA;
        uiConfig.T = T;
        uiConfig.instTable = getInstTabel();
        uiConfig.compiled = compiled;
        uiConfig.running = running;
    }
    private short register[];
    private short PC;   // Program counter (instrucion pointer)
    private short SP;   // Stack pointer
    private short IH;
    private short RA;   // Return address
    private short T;
    private MemoryIO memory;
    private boolean compiled;
    private boolean cancelled;
    private boolean running;
    private String[] lines;
    private Map<Short, Integer> lineNumMap;
    private UIConfig uiConfig;
}
