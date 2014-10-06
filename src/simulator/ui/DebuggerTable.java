package simulator.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author LeeThree
 */
public class DebuggerTable extends JTable {

    public DebuggerTable() {
        setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Line No.", "Assembly", "Code", "Address"
                }) {

            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        setShowVerticalLines(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        highlightRenderer = new DefaultTableCellRenderer();
        highlightRenderer.setBackground(Color.YELLOW);
        breakpointRenderer = new DefaultTableCellRenderer();
        breakpointRenderer.setBackground(Color.RED);
        breakpointRenderer.setForeground(Color.WHITE);
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerHighlightRenderer = new DefaultTableCellRenderer();
        centerHighlightRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerHighlightRenderer.setBackground(Color.YELLOW);
        centerBreakpointRenderer = new DefaultTableCellRenderer();
        centerBreakpointRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerBreakpointRenderer.setBackground(Color.RED);
        centerBreakpointRenderer.setForeground(Color.WHITE);
        highlightRowNum = -1;
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() > 1 && getSelectedRowCount() == 1) {
                    setBreakPoint(getSelectedRow());
                }
            }
        });
    }

    public void setConfig(UIConfig config) {
        this.config = config;
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (convertRowIndexToModel(row) == highlightRowNum) {
            if (convertColumnIndexToModel(column) == 1) {
                return highlightRenderer;
            } else {
                return centerHighlightRenderer;
            }
        } else if (config.breakpoints.contains(convertRowIndexToModel(row))) {
            if (convertColumnIndexToModel(column) == 1) {
                return breakpointRenderer;
            } else {
                return centerBreakpointRenderer;
            }
        } else {
            return super.getCellRenderer(row, column);
        }
    }

    public void update() {
        getColumnModel().getColumn(0).setPreferredWidth(70);
        getColumnModel().getColumn(0).setMaxWidth(300);
        getColumnModel().getColumn(3).setPreferredWidth(70);
        getColumnModel().getColumn(3).setMaxWidth(300);
        //getColumnModel().getColumn(1).setCellRenderer(highlightRenderer);
        getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }

    public void highlight(int rowIndex) {
        highlightRowNum = rowIndex;
    //getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
    }

    private void setBreakPoint(int position) {
        if (!config.breakpoints.add(position)) {
            config.breakpoints.remove(position);
        }
        clearSelection();
        update();
    }
    private DefaultTableCellRenderer highlightRenderer;
    private DefaultTableCellRenderer breakpointRenderer;
    private DefaultTableCellRenderer centerRenderer;
    private DefaultTableCellRenderer centerHighlightRenderer;
    private DefaultTableCellRenderer centerBreakpointRenderer;
    private int highlightRowNum;
    private UIConfig config;
}
