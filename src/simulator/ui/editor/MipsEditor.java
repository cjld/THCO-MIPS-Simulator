package simulator.ui.editor;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author LeeThree
 */
public class MipsEditor extends JTextPane implements KeyListener {

    public MipsEditor() {
        addKeyListener(this);
        undoManager = new UndoManager();
    }

    public int getLineNum() {
        return (getText() + ".").split("\n").length;
    }

    @Override
    public String getText() {
        try {
            StyledDocument doc = getStyledDocument();
            return doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {
            return null;
        }
    }

    @Override
    public void setText(String t) {
        setDocument(CodeHighlighter.highlight(t));
        undoManager.reset(new Edit(getText(), getCaretPosition(), getSelectionStart(), getSelectionEnd()));
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public void undo() {
        Edit lastEdit = undoManager.undo();
        if (lastEdit != null) {
            unpack(lastEdit);
        }
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public void redo() {
        Edit lastEdit = undoManager.redo();
        if (lastEdit != null) {
            unpack(lastEdit);
        }
    }
//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.add(new JScrollPane(new MipsEditor()));
//        frame.setVisible(true);
//    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        if (!evt.isActionKey() && !evt.isControlDown() && !evt.isAltDown()) {
            update();
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public void setSize(Dimension d) {
        String[] lines = getText().split("\n");
        int maxLineLength = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > maxLineLength) {
                maxLineLength = lines[i].length();
            }
        }
        d.width = Math.max(maxLineLength * 10, 800);
        super.setSize(d);
    }

    private void update() {
        Edit edit = new Edit(getText(), getCaretPosition(), getSelectionStart(), getSelectionEnd());
        undoManager.addChange(edit);
        unpack(edit);
//        setDocument(CodeHighlighter.highlight(text));
//        setCaretPosition(caret);
//        setSelectionStart(selectionStart);
//        setSelectionEnd(selectionEnd);
    }

    private void unpack(Edit edit) {
        setDocument(CodeHighlighter.highlight(edit.text));
        setCaretPosition(edit.caret);
        setSelectionStart(edit.selectionStart);
        setSelectionEnd(edit.selectionEnd);
    }
    UndoManager undoManager;
}
