package simulator.core;

/**
 *
 * @author LeeThree
 */
public final class Misc {

    public static short signExtend(String immediate) {
        if (immediate.startsWith("1")) {
            StringBuffer imm = new StringBuffer(immediate);
            for (int i = 0; i < 16 - immediate.length(); i++) {
                imm.insert(0, "1");
            }
            return encode(imm.toString());
        } else {
            return zeroExtend(immediate);
        }
    }

    public static short zeroExtend(String immediate) {
        StringBuffer imm = new StringBuffer(immediate);
        for (int i = 0; i < 16 - immediate.length(); i++) {
            imm.insert(0, "0");
        }
        return encode(imm.toString());
    }

    public static boolean unsignedLess(short a, short b) {
        if (a < 0 && b > 0) {
            return false;

        } else if (a >= 0 && b < 0) {
            return true;
        } else {
            return a < b;
        }
    }

    public static short logicalShiftRight(short a, short b) {
        short result = a;
        for (int i = 0; i < b; i++) {
            result >>= 1;
            result &= 32767;
        }
        return result;
    }

    public static short encode(String str) {
        short inst = 0;
        for (int i = 0; i < 16; i++) {
            inst <<= 1;
            if (str.charAt(i) == '1') {
                inst++;
            }
        }
        return inst;
    }

    public static String decode(short inst) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            if (inst % 2 == 0) {
                str.insert(0, '0');
            } else {
                str.insert(0, '1');
            }
            inst >>= 1;
        }
        return str.toString();
    }

    public static String toHex(short s) {
        String str = Integer.toHexString(s).toUpperCase();
        return str.substring(Math.max(str.length() - 4, 0));
    }

    public static String deleteComment(String line) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '#' || line.charAt(i) == '%' || line.charAt(i) == ';') {
                break;
            }
            buffer.append(line.charAt(i));
        }
        return buffer.toString();
    }
}
