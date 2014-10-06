package simulator.ui.editor;

import java.awt.Color;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author LeeThree
 */
public final class CodeHighlighter {

    public static StyledDocument highlight(String text) {
        if (!init) {
            initialize();
        }
        StyledDocument doc = new DefaultStyledDocument();
        try {
            boolean inComment = false;
            StringTokenizer tokenize = new StringTokenizer(text, DELIM, true);
            while (tokenize.hasMoreTokens()) {
                String str = tokenize.nextToken();
                Style s = null;
                if (str.equals("\n")) {
                    inComment = false;
                } else if (str.equals("#") || str.equals("%") || str.equals(";")) {
                    inComment = true;
                }
                if (inComment) {
                    s = styleContext.getStyle("comment");
                } else if (insts.contains(str.trim().toLowerCase())) {
                    s = styleContext.getStyle("keywords");
                } else if (regs.contains(str.trim().toLowerCase())) {
                    s = styleContext.getStyle("registers");
                } else {
                    s = styleContext.getStyle("none");
                }
                doc.insertString(doc.getLength(), str, s);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private static void initialize() {
        addStyle("none", DEFAULT_FORGROUND, SIZE, CODE_FAMILY, false, false);
        addStyle("keywords", KEYWORDS_FOREGROUND, SIZE, CODE_FAMILY, true, false);
        addStyle("registers", REGISTERS_FOREGROUND, SIZE, CODE_FAMILY, false, true);
        addStyle("comment", COMMENT_FOREGROUND, SIZE, COMMENT_FAMILY, false, false);
        String[] inst = resources.getString(INSTRUCTIONS).split(RESOURCES_DELIM);
        for (int i = 0; i < inst.length; i++) {
            insts.add(inst[i]);
        }
        String[] reg = resources.getString(REGISTERS).split(RESOURCES_DELIM);
        for (int i = 0; i < reg.length; i++) {
            regs.add(reg[i]);
        }
    }

    private static void addStyle(String key, Color color, int size, String fam, boolean bold, boolean italic) {
        Style s = styleContext.addStyle(key, null);
        if (color != null) {
            StyleConstants.setForeground(s, color);
        }
        if (size > 0) {
            StyleConstants.setFontSize(s, size);
        }
        if (fam != null) {
            StyleConstants.setFontFamily(s, fam);
        }
        StyleConstants.setBold(s, bold);
        StyleConstants.setItalic(s, italic);
    }
    private static final String DELIM = "(); {},.[]#:%\n\r\t";
    private static final String RESOURCES_DELIM = " ";
    private static final String CODE_FAMILY = "Courier New";
    private static final String COMMENT_FAMILY = "Courier New";
    private static final int SIZE = 13;
    private static final Color KEYWORDS_FOREGROUND = new Color(51, 102, 153);
    private static final Color REGISTERS_FOREGROUND = new Color(153, 102, 51);
    private static final Color COMMENT_FOREGROUND = new Color(102, 153, 153);
    private static final Color DEFAULT_FORGROUND = new Color(0, 0, 0);
    private static final StyleContext styleContext = new StyleContext();
    private static final String BASENAME = "simulator.ui.resources.MipsKeywords";
    private static final ResourceBundle resources = ResourceBundle.getBundle(BASENAME);
    private static final HashSet<String> insts = new HashSet<String>();
    private static final HashSet<String> regs = new HashSet<String>();
    private static final String INSTRUCTIONS = "mips_instruction";
    private static final String REGISTERS = "mips_register";
    private static boolean init = false;
}
