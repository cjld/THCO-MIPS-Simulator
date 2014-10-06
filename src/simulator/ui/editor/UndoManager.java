package simulator.ui.editor;

import java.util.Stack;

/**
 *
 * @author LeeThree
 */
public class UndoManager {

    public UndoManager() {
        undoStack = new Stack<Edit>();
        redoStack = new Stack<Edit>();
        currentEdit = new Edit("", 0, 0, 0);
    }

    public void addChange(Edit edit) {
        if (!edit.equals(currentEdit)) {
            undoStack.add(currentEdit);
            currentEdit = edit;
            redoStack.clear();
        }
    }

    public boolean canUndo() {
        return !undoStack.empty();
    }

    public Edit undo() {
        if (!undoStack.empty()) {
            Edit lastEdit = undoStack.pop();
            redoStack.add(currentEdit);
            currentEdit = lastEdit;
            return lastEdit;
        } else {
            return null;
        }
    }

    public boolean canRedo() {
        return !redoStack.empty();
    }

    public Edit redo() {
        if (!redoStack.empty()) {
            Edit lastEdit = redoStack.pop();
            undoStack.add(currentEdit);
            currentEdit = lastEdit;
            return lastEdit;
        } else {
            return null;
        }
    }

    public void reset(Edit edit) {
        undoStack.clear();
        redoStack.clear();
        currentEdit = edit;
    }
    private Stack<Edit> undoStack;
    private Edit currentEdit;
    private Stack<Edit> redoStack;
}
