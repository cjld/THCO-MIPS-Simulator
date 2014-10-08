package simulator.core;

import java.util.Hashtable;
import simulator.ui.Console;
import simulator.core.error.BadImmFormatError;
import simulator.core.error.BadInstFormatError;
import simulator.core.error.BadLabelFormatError;
import simulator.core.error.ImmTooLargeError;
import simulator.core.error.InvalidRegNameError;
import simulator.core.error.NoSuchInstError;
import simulator.core.error.CompileError;
import simulator.core.error.LabelTooFarAwayError;
import simulator.core.error.UndefinedLabelError;

/**
 *
 * @author LeeThree
 */
public class Compiler {

    public String translate(String instruction, short instPtr) throws CompileError {
        this.instPtr = instPtr;
        String inst[] = instruction.toLowerCase().split("[^\\w-]+");
        //String code = new String();
        if (inst[0].equals("addiu")) {
            checkInstFormat(inst, 2);
            return "01001" + transReg(inst[1]) + transImm(inst[2], 8);
        } else if (inst[0].equals("addiu3")) {
            checkInstFormat(inst, 3);
            return "01000" + transReg(inst[1]) + transReg(inst[2]) + "0" + transImm(inst[3], 4);
        } else if (inst[0].equals("addsp3")) {
            checkInstFormat(inst, 2);
            return "00000" + transReg(inst[1]) + transImm(inst[2], 8);
        } else if (inst[0].equals("addsp")) {
            checkInstFormat(inst, 1);
            return "01100" + "011" + transImm(inst[1], 8);
        } else if (inst[0].equals("addu")) {
            checkInstFormat(inst, 3);
            return "11100" + transReg(inst[1]) + transReg(inst[2]) + transReg(inst[3]) + "01";
        } else if (inst[0].equals("and")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "01100";
        } else if (inst[0].equals("b")) {
            checkInstFormat(inst, 1);
            return "00010" + transLabel(inst[1], 11);
        } else if (inst[0].equals("beqz")) {
            checkInstFormat(inst, 2);
            return "00100" + transReg(inst[1]) + transLabel(inst[2], 8);
        } else if (inst[0].equals("bnez")) {
            checkInstFormat(inst, 2);
            return "00101" + transReg(inst[1]) + transLabel(inst[2], 8);
        } else if (inst[0].equals("bteqz")) {
            checkInstFormat(inst, 1);
            return "01100" + "000" + transLabel(inst[1], 8);
        } else if (inst[0].equals("btnez")) {
            checkInstFormat(inst, 1);
            return "01100" + "001" + transLabel(inst[1], 8);
        } else if (inst[0].equals("cmp")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "01010";
        } else if (inst[0].equals("cmpi")) {
            checkInstFormat(inst, 2);
            return "01110" + transReg(inst[1]) + transImm(inst[2], 8);
        } else if (inst[0].equals("int")) {
            checkInstFormat(inst, 1);
            return "11111" + "0000000" + transImm(inst[1], 4);
        } else if (inst[0].equals("jalr")) {
            checkInstFormat(inst, 1);
            return "11101" + transReg(inst[1]) + "11000000";
        } else if (inst[0].equals("jr")) {
            checkInstFormat(inst, 1);
            return "11101" + transReg(inst[1]) + "00000000";
        } else if (inst[0].equals("jrra")) {
            checkInstFormat(inst, 0);
            return "11101" + "000" + "00100000";
        } else if (inst[0].equals("li")) {
            checkInstFormat(inst, 2);
            return "01101" + transReg(inst[1]) + transImm(inst[2], 8);
        } else if (inst[0].equals("lw")) {
            checkInstFormat(inst, 3);
            return "10011" + transReg(inst[1]) + transReg(inst[2]) + transImm(inst[3], 5);
        } else if (inst[0].equals("lw-sp")) {
            checkInstFormat(inst, 2);
            return "10010" + transReg(inst[1]) + transImm(inst[2], 8);
        } else if (inst[0].equals("mfih")) {
            checkInstFormat(inst, 1);
            return "11110" + transReg(inst[1]) + "00000000";
        } else if (inst[0].equals("mfpc")) {
            checkInstFormat(inst, 1);
            return "11101" + transReg(inst[1]) + "01000000";
        } else if (inst[0].equals("move")) {
            checkInstFormat(inst, 2);
            return "01100" + "111" + transReg(inst[1]) + "00" + transReg(inst[2]);
        } else if (inst[0].equals("mtih")) {
            checkInstFormat(inst, 1);
            return "11110" + transReg(inst[1]) + "00000001";
        } else if (inst[0].equals("mtsp")) {
            checkInstFormat(inst, 1);
            return "01100" + "100" + transReg(inst[1]) + "00000";
        } else if (inst[0].equals("neg")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "01011";
        } else if (inst[0].equals("not")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "01111";
        } else if (inst[0].equals("or")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "01101";
        } else if (inst[0].equals("sll")) {
            checkInstFormat(inst, 3);
            return "00110" + transReg(inst[1]) + transReg(inst[2]) + transImm(inst[3], 3) + "00";
        } else if (inst[0].equals("sllv")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "00100";
        } else if (inst[0].equals("slt")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "00010";
        } else if (inst[0].equals("slti")) {
            checkInstFormat(inst, 2);
            return "01010" + transReg(inst[1]) + transImm(inst[2], 8);
        } else if (inst[0].equals("sltu")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "00011";
        } else if (inst[0].equals("sltui")) {
            checkInstFormat(inst, 2);
            return "01011" + transReg(inst[1]) + transImm(inst[2], 8);
        } else if (inst[0].equals("sra")) {
            checkInstFormat(inst, 3);
            return "00110" + transReg(inst[1]) + transReg(inst[2]) + transImm(inst[3], 3) + "11";
        } else if (inst[0].equals("srav")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "00111";
        } else if (inst[0].equals("srl")) {
            checkInstFormat(inst, 3);
            return "00110" + transReg(inst[1]) + transReg(inst[2]) + transImm(inst[3], 3) + "10";
        } else if (inst[0].equals("srlv")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "00110";
        } else if (inst[0].equals("subu")) {
            checkInstFormat(inst, 3);
            return "11100" + transReg(inst[1]) + transReg(inst[2]) + transReg(inst[3]) + "11";
        } else if (inst[0].equals("sw")) {
            checkInstFormat(inst, 3);
            return "11011" + transReg(inst[1]) + transReg(inst[2]) + transImm(inst[3], 5);
        } else if (inst[0].equals("sw-rs")) {
            checkInstFormat(inst, 1);
            return "01100" + "010" + transImm(inst[1], 8);
        } else if (inst[0].equals("sw-sp")) {
            checkInstFormat(inst, 2);
            return "11010" + transReg(inst[1]) + transImm(inst[2], 8);
        } else if (inst[0].equals("xor")) {
            checkInstFormat(inst, 2);
            return "11101" + transReg(inst[1]) + transReg(inst[2]) + "01110";
        } else if (inst[0].equals("nop")) {
            checkInstFormat(inst, 0);
            return "00001" + "00000000000";
        } else {
        	try {
            	String ss = transImm(instruction, 16);
            	return ss;
        	} catch (Exception e) {
                throw new NoSuchInstError(inst[0]);	
        	}
        }
    }

    public String[] preprocess(String sourceCode) throws BadLabelFormatError {
        String[] lines = sourceCode.split("\\n");
        //ArrayList<String> newLines = new ArrayList(lines.length);
        Console.log("Preprocessing...", "info");
        labelTable = new Hashtable<String, Short>(20);
        short memoryPointer = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = Misc.deleteComment(line);
            int colonPos = line.indexOf(':');
            if (line.startsWith("\"") && line.endsWith("\"") && line.length() > 1) {
            	line.replaceAll("\\\"", "\"");
            	memoryPointer += line.length() - 1;
            } else
            if (colonPos != -1) {
                String label = line.substring(0, colonPos).trim().toLowerCase();
                if (label.matches("[\\w-]+")) {
                    if (!labelTable.containsKey(label)) {
                        labelTable.put(label, memoryPointer);
                        Console.log("New label: \"" + label + "\"\t found in line " + (i + 1) + ".");
                    } else {
                        Console.log("Warning: Duplicated label found in line " + (i + 1) + ": \"" + label + "\"\nwhich will be ignored.", "warning");
                    }
                    line = line.substring(colonPos + 1);
                    if (!line.matches("\\s*")) {
                        memoryPointer++;
                    }
                } else {
                    Console.log("Compile Error in line " + (i + 1) + ": \t" + line, "error");
                    throw new BadLabelFormatError(label);
                }
            } else {
                if (!line.matches("\\s*")) {
                    memoryPointer++;
                }
            }
            lines[i] = line.trim();
        }
        Console.log("Preprocessing finished.", "emphasis");
        return lines;
    }

    private String transReg(String regName) throws InvalidRegNameError {
        if (regName.length() == 2 && regName.startsWith("r")) {
            int i = regName.charAt(1) - '0';
            if (i < 8 && i >= 0) {
                switch (i) {
                    case 0:
                        return "000";
                    case 1:
                        return "001";
                    case 2:
                        return "010";
                    case 3:
                        return "011";
                    case 4:
                        return "100";
                    case 5:
                        return "101";
                    case 6:
                        return "110";
                    case 7:
                        return "111";
                    default:
                        return "000";
                }
            } else {
                throw new InvalidRegNameError(regName);
            }
        } else {
            throw new InvalidRegNameError(regName);
        }
    }

    private String transImm(String immediate, int immLength) 
    		throws BadImmFormatError, ImmTooLargeError, UndefinedLabelError, LabelTooFarAwayError 
    {
        String imm = new String(immediate);
        if (imm.endsWith("h")) {
            if (imm.startsWith("-")) {
                imm = "-0x" + imm.substring(1, imm.length() - 1);
            } else {
                imm = "0x" + imm.substring(0, imm.length() - 1);
            }
        }
        int s = 0;
        try {
            s = Integer.decode(imm);
        } catch (NumberFormatException ex) {
            Short labelPos = labelTable.get(imm);
            if (labelPos == null) {
                throw new UndefinedLabelError(imm);
            } else {
                try {
                    //Console.log(labelPos + "-" + instPtr + "-1");
                    Console.log("Label: \"" + imm + "\"\t converted into " + (labelPos) + ".");
                    return transImm(Integer.toString(labelPos), immLength);
                } catch (BadImmFormatError error) {
                    Console.log("Something impossible occurs!", "error");
                    throw new LabelTooFarAwayError(imm);
                } catch (ImmTooLargeError error) {
                    throw new LabelTooFarAwayError(imm);
                }
            }
        }
        if (s > (1 << immLength) - 1 || s < -(1 << immLength - 1) + 1) {          // TODO: warning
            throw new ImmTooLargeError(s, (1 << immLength) - 1, -(1 << immLength - 1) + 1);
        } else {
            return Misc.decode((short) s).substring(16 - immLength);
        }
    }

	private String transLabel(String label, int immLength)
			throws ImmTooLargeError, UndefinedLabelError, LabelTooFarAwayError
	{
		Short labelPos = labelTable.get(label);
		if (labelPos == null) {
			try {
				return transImm(label, immLength);
			} catch (Exception e) {
				throw new UndefinedLabelError(label);
			}
		} else {
			try {
				// Console.log(labelPos + "-" + instPtr + "-1");
				Console.log("Label: \"" + label + "\"\t converted into "
						+ (labelPos - instPtr - 1) + ".");
				return transImm(Integer.toString(labelPos - instPtr - 1),
						immLength);
			} catch (BadImmFormatError error) {
				Console.log("Something impossible occurs!", "error");
				throw new LabelTooFarAwayError(label);
			} catch (ImmTooLargeError error) {
				throw new LabelTooFarAwayError(label);
			}
		}
	}

    private void checkInstFormat(String[] inst, int paraNum) throws BadInstFormatError {
        if (inst.length != paraNum + 1) {
            throw new BadInstFormatError(inst.length - 1, paraNum);
        }
    }
    private Hashtable<String, Short> labelTable;
    private short instPtr;
}
