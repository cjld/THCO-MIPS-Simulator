package simulator.ui.editor;

/**
 *
 * @author LeeThree
 */
public class Edit {

    public Edit(String text, int caret, int selectionStart, int selectionEnd) {
        this.text = text;
        this.caret = caret;
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edit) {
            Edit edit = (Edit) obj;
            return text.equals(edit.text);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.text != null ? this.text.hashCode() : 0);
        return hash;
    }
    public String text;
    public int caret;
    public int selectionStart;
    public int selectionEnd;
}
