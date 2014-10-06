package simulator.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextPane;
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
public class Console extends JTextPane {

    public Console() {
        styleContext = new StyleContext();
        addStyle("default", Color.BLACK, 12, Font.SANS_SERIF, false, false);
        addStyle("error", Color.RED, 13, Font.SERIF, true, false);
        addStyle("info", Color.BLUE, 12, Font.SANS_SERIF, false, false);
        addStyle("emphasis", Color.BLUE, 13, Font.SANS_SERIF, true, false);
        addStyle("warning", Color.MAGENTA, 13, Font.SERIF, true, true);
        //this.setContentType("text/html");
        this.setEditable(false);
        setAutoscrolls(true);
        //this.setVisible(true);
        console = this;
    }

    public static void clear() {
        getInstance().setDocument(new DefaultStyledDocument());
    }

    public static void log(String message) {
        log(message, "default");
    }

    public static void log(String message, String type) {
        try {
            StyledDocument doc = getInstance().getStyledDocument();
            doc.insertString(doc.getLength(), message + "\n", styleContext.getStyle(type));
            getInstance().setCaretPosition(doc.getLength());
        } catch (BadLocationException ex) {
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

    private static Console getInstance() {
        if (console == null) {
            console = new Console();
        }
        return console;
    }
    private static StyleContext styleContext;
    private static Console console;
}
