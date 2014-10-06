package simulator.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ArrayBlockingQueue;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author LeeThree
 */
public class Terminal extends JDialog {

    public Terminal() {
        super();
        pane = new JTextArea();
        pane.setText(text);
        pane.setEditable(false);
        pane.requestFocus();
        pane.setBackground(new Color(50, 50, 100));
        pane.setForeground(Color.YELLOW);
        pane.setFont(new Font("consolas", Font.PLAIN, 13));
        pane.getCaret().setVisible(false);
        //pane.setPreferredSize(new Dimension(500, 400));
        pane.setColumns(80);
        pane.setRows(25);
        pane.setLineWrap(true);
        pane.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                keyTypedAction(e);
            }
        });

        scrollPane = new JScrollPane(pane);

        this.add(scrollPane);
        this.setTitle("Terminal");
        this.pack();

        buffer = new ArrayBlockingQueue(100);
        term = this;
    }

//    public static void main(String[] args) {
//        TermPane term = new TermPane();
//        term.setVisible(true);
//        while (true) {
//            term.output(term.getInput());
//        }
//    }
    public static void output(char c) {
        switch (c) {
            case '\b': {
                if (term.text.length() > 0) {
                    term.text = term.text.substring(0, term.text.length() - 1);
                }
                break;
            }
            default: {
                term.text += c;
            }
        }
        term.pane.setText(term.text);
    }

    public static void showMessage(String s) {
        term.text += s;
        term.pane.setText(term.text);
    }

    public static void forceStop() {
        term.buffer.offer(' ');
    }

    public static char getInput() {
        try {
            term.setTitle("Terminal - Requesting input");
            term.pane.getCaret().setVisible(true);
            char c = term.buffer.take();
            output(c);
            term.pane.getCaret().setVisible(false);
            term.setTitle("Terminal");
            //System.out.println("getT:" + c);
            return c;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return '\0';
        }
    }

    public static void reset() {
        term.text = "";
        term.pane.setText("");
        term.buffer.clear();
        term.setTitle("Terminal");
    }

    public static void setTermVisible(boolean isVisible) {
        term.setVisible(isVisible);
    }

    public static Terminal getInstance() {
//        if (term == null) {
//            term = new Terminal(null);
//        }
        return term;
    }

    private void keyTypedAction(KeyEvent e) {
        buffer.offer(e.getKeyChar());
    }
    private ArrayBlockingQueue<Character> buffer;
    private String text = "";
    private JTextArea pane;
    private JScrollPane scrollPane;
    private static Terminal term;
}
