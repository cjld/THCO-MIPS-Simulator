/*
 * SimulatorView.java
 */
package simulator.ui;

import simulator.SimulatorApp;
import simulator.ui.AboutBox;
import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EventObject;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.application.Application.ExitListener;
import simulator.ui.Terminal;
import simulator.ui.UIConfig;

/**
 * The application's main frame.
 */
public class SimulatorView extends FrameView {

    public SimulatorView(SingleFrameApplication app) {
        super(app);

        initComponents();
        //menuBar.setVisible(false);

        saved = true;
        //buffer = new String();
        fileChooser = new JFileChooser();
        filter = new FileNameExtensionFilter("MIPS Code Document (*.mips)", "mips");
        fileChooser.setFileFilter(filter);

        editorScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                scrollBarSync();
            }
        });
        lineNumScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                scrollBarSync();
            }
        });
        app.addExitListener(new ExitListener() {

            @Override
            public boolean canExit(EventObject e) {
                if (!saved) {
                    int option = JOptionPane.showConfirmDialog((Component) e.getSource(), "Do you want to save changes to your current file?", "Closing", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    switch (option) {
                        case JOptionPane.YES_OPTION:
                            save();
                            return true;
                        case JOptionPane.CANCEL_OPTION:
                            return false;
                    }
                }
                return true;
            }

            @Override
            public void willExit(EventObject arg0) {
            }
        });
        mainTabbedPane.setEnabledAt(1, false);
        mainTabbedPane.setEnabledAt(2, false);
        watchTable.setVisible(false);
        watchScrollPane.setVisible(false);
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SimulatorApp.getApplication().getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SimulatorApp.getApplication().show(aboutBox);
    }

    public String getSourceCode() {
        return editor.getText();
    }

    public void update() {
        if (config.instTable != null) {
            this.debuggerTable.setModel(config.instTable);
        }
        registerPanel.update();
        debuggerTable.highlight(config.PC);
        debuggerTable.update();
        memoryPanel.update();
        if (config.compiled) {
            mainTabbedPane.setEnabledAt(1, true);
            mainTabbedPane.setEnabledAt(2, true);
            saveBinButton.setEnabled(true);
            saveBinMenuItem.setEnabled(true);
            if (config.running) {
                stopButton.setEnabled(true);
                stopMenuItem.setEnabled(true);
            } else {
                stopButton.setEnabled(false);
                stopMenuItem.setEnabled(false);
            }
            if (config.stepping) {
                runButton.setEnabled(false);
                runMenuItem.setEnabled(false);
                stepButton.setEnabled(false);
                stepMenuItem.setEnabled(false);
            } else {
                runButton.setEnabled(true);
                runMenuItem.setEnabled(true);
                stepButton.setEnabled(true);
                stepMenuItem.setEnabled(true);
            }
        } else {
            mainTabbedPane.setEnabledAt(1, false);
            mainTabbedPane.setEnabledAt(2, false);
            saveBinButton.setEnabled(false);
            saveBinMenuItem.setEnabled(false);
            runButton.setEnabled(false);
            stopButton.setEnabled(false);
            stepButton.setEnabled(false);
            runMenuItem.setEnabled(false);
            stopMenuItem.setEnabled(false);
            stepMenuItem.setEnabled(false);
            mainTabbedPane.setSelectedIndex(0);
        }
    }

    public void setConfig(UIConfig config) {
        this.config = config;
        registerPanel.setConfig(config);
        memoryPanel.setConfig(config);
        debuggerTable.setConfig(config);
    }

    public void openTab(int tabIndex) {
        mainTabbedPane.setSelectedIndex(tabIndex);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        newButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        saveBinButton = new javax.swing.JButton();
        toolBarSeparator2 = new javax.swing.JToolBar.Separator();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        toolBarSeparator3 = new javax.swing.JToolBar.Separator();
        compileButton = new javax.swing.JButton();
        toolBarSeparator4 = new javax.swing.JToolBar.Separator();
        runButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        stepButton = new javax.swing.JButton();
        toolBarSeparator5 = new javax.swing.JToolBar.Separator();
        terminalToggleButton = new javax.swing.JToggleButton();
        mainSplitPane = new javax.swing.JSplitPane();
        mainTabbedPane = new javax.swing.JTabbedPane();
        editorSplitPane = new javax.swing.JSplitPane();
        lineNumScrollPane = new javax.swing.JScrollPane();
        lineNumArea = new javax.swing.JTextArea();
        editorScrollPane = new javax.swing.JScrollPane();
        editor = new simulator.ui.editor.MipsEditor();
        debuggerPanel = new javax.swing.JPanel();
        registerPanel = new simulator.ui.RegisterPanel();
        debuggerScrollPane = new javax.swing.JScrollPane();
        debuggerTable = new simulator.ui.DebuggerTable();
        memoryPanel = new simulator.ui.MemoryPanel();
        consoleSplitPane = new javax.swing.JSplitPane();
        consoleScrollPane = new javax.swing.JScrollPane();
        console = new simulator.ui.Console();
        watchScrollPane = new javax.swing.JScrollPane();
        watchTable = new javax.swing.JTable();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        saveBinMenuItem = new javax.swing.JMenuItem();
        fileMenuSeparator = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        debugMenu = new javax.swing.JMenu();
        compileMenuItem = new javax.swing.JMenuItem();
        runMenuItem = new javax.swing.JMenuItem();
        stopMenuItem = new javax.swing.JMenuItem();
        stepMenuItem = new javax.swing.JMenuItem();
        emptyMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(simulator.SimulatorApp.class).getContext().getActionMap(SimulatorView.class, this);
        newButton.setAction(actionMap.get("newDoc")); // NOI18N
        newButton.setFocusable(false);
        newButton.setHideActionText(true);
        newButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newButton.setName("newButton"); // NOI18N
        newButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(newButton);

        openButton.setAction(actionMap.get("open")); // NOI18N
        openButton.setFocusable(false);
        openButton.setHideActionText(true);
        openButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openButton.setName("openButton"); // NOI18N
        openButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(openButton);

        saveButton.setAction(actionMap.get("save")); // NOI18N
        saveButton.setFocusable(false);
        saveButton.setHideActionText(true);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setName("saveButton"); // NOI18N
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(saveButton);

        saveAsButton.setAction(actionMap.get("saveAs")); // NOI18N
        saveAsButton.setFocusable(false);
        saveAsButton.setHideActionText(true);
        saveAsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveAsButton.setName("saveAsButton"); // NOI18N
        saveAsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(saveAsButton);

        saveBinButton.setAction(actionMap.get("saveBin")); // NOI18N
        saveBinButton.setEnabled(false);
        saveBinButton.setFocusable(false);
        saveBinButton.setHideActionText(true);
        saveBinButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveBinButton.setName("saveBinButton"); // NOI18N
        saveBinButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(saveBinButton);

        toolBarSeparator2.setName("toolBarSeparator2"); // NOI18N
        toolBar.add(toolBarSeparator2);

        undoButton.setAction(actionMap.get("undo")); // NOI18N
        undoButton.setEnabled(false);
        undoButton.setFocusable(false);
        undoButton.setHideActionText(true);
        undoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undoButton.setName("undoButton"); // NOI18N
        undoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(undoButton);

        redoButton.setAction(actionMap.get("redo")); // NOI18N
        redoButton.setEnabled(false);
        redoButton.setFocusable(false);
        redoButton.setHideActionText(true);
        redoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redoButton.setName("redoButton"); // NOI18N
        redoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(redoButton);

        toolBarSeparator3.setName("toolBarSeparator3"); // NOI18N
        toolBar.add(toolBarSeparator3);

        compileButton.setAction(actionMap.get("compile")); // NOI18N
        compileButton.setFocusable(false);
        compileButton.setHideActionText(true);
        compileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        compileButton.setName("compileButton"); // NOI18N
        compileButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(compileButton);

        toolBarSeparator4.setName("toolBarSeparator4"); // NOI18N
        toolBar.add(toolBarSeparator4);

        runButton.setAction(actionMap.get("run")); // NOI18N
        runButton.setEnabled(false);
        runButton.setFocusable(false);
        runButton.setHideActionText(true);
        runButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runButton.setName("runButton"); // NOI18N
        runButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(runButton);

        stopButton.setAction(actionMap.get("stop")); // NOI18N
        stopButton.setEnabled(false);
        stopButton.setFocusable(false);
        stopButton.setHideActionText(true);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setName("stopButton"); // NOI18N
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(stopButton);

        stepButton.setAction(actionMap.get("step")); // NOI18N
        stepButton.setEnabled(false);
        stepButton.setFocusable(false);
        stepButton.setHideActionText(true);
        stepButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stepButton.setName("stepButton"); // NOI18N
        stepButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(stepButton);

        toolBarSeparator5.setName("toolBarSeparator5"); // NOI18N
        toolBar.add(toolBarSeparator5);

        terminalToggleButton.setAction(actionMap.get("toggleTerminal")); // NOI18N
        terminalToggleButton.setFocusable(false);
        terminalToggleButton.setHideActionText(true);
        terminalToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        terminalToggleButton.setName("terminalToggleButton"); // NOI18N
        terminalToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(terminalToggleButton);

        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerLocation(400);
        mainSplitPane.setDividerSize(3);
        mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setResizeWeight(1.0);
        mainSplitPane.setName("mainSplitPane"); // NOI18N

        mainTabbedPane.setName("mainTabbedPane"); // NOI18N

        editorSplitPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        editorSplitPane.setDividerSize(0);
        editorSplitPane.setName("editorSplitPane"); // NOI18N

        lineNumScrollPane.setBorder(null);
        lineNumScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        lineNumScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        lineNumScrollPane.setMinimumSize(new java.awt.Dimension(23, 4));
        lineNumScrollPane.setName("lineNumScrollPane"); // NOI18N
        lineNumScrollPane.setPreferredSize(new java.awt.Dimension(40, 23));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(simulator.SimulatorApp.class).getContext().getResourceMap(SimulatorView.class);
        lineNumArea.setBackground(resourceMap.getColor("lineNumArea.background")); // NOI18N
        lineNumArea.setColumns(4);
        lineNumArea.setEditable(false);
        lineNumArea.setFont(resourceMap.getFont("lineNumArea.font")); // NOI18N
        lineNumArea.setAutoscrolls(false);
        lineNumArea.setFocusable(false);
        lineNumArea.setMargin(new java.awt.Insets(4, 1, 2, 2));
        lineNumArea.setName("lineNumArea"); // NOI18N
        lineNumScrollPane.setViewportView(lineNumArea);

        editorSplitPane.setLeftComponent(lineNumScrollPane);

        editorScrollPane.setName("editorScrollPane"); // NOI18N

        editor.setName("editor"); // NOI18N
        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                editorKeyTyped(evt);
            }
        });
        editorScrollPane.setViewportView(editor);

        editorSplitPane.setRightComponent(editorScrollPane);

        mainTabbedPane.addTab(resourceMap.getString("editorSplitPane.TabConstraints.tabTitle"), resourceMap.getIcon("editorSplitPane.TabConstraints.tabIcon"), editorSplitPane); // NOI18N

        debuggerPanel.setName("debuggerPanel"); // NOI18N

        registerPanel.setName("registerPanel"); // NOI18N

        debuggerScrollPane.setName("debuggerScrollPane"); // NOI18N

        debuggerTable.setName("debuggerTable"); // NOI18N
        debuggerScrollPane.setViewportView(debuggerTable);

        javax.swing.GroupLayout debuggerPanelLayout = new javax.swing.GroupLayout(debuggerPanel);
        debuggerPanel.setLayout(debuggerPanelLayout);
        debuggerPanelLayout.setHorizontalGroup(
            debuggerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(registerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
            .addComponent(debuggerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
        );
        debuggerPanelLayout.setVerticalGroup(
            debuggerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, debuggerPanelLayout.createSequentialGroup()
                .addComponent(debuggerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainTabbedPane.addTab(resourceMap.getString("debuggerPanel.TabConstraints.tabTitle"), resourceMap.getIcon("debuggerPanel.TabConstraints.tabIcon"), debuggerPanel); // NOI18N

        memoryPanel.setName("memoryPanel"); // NOI18N
        mainTabbedPane.addTab(resourceMap.getString("memoryPanel.TabConstraints.tabTitle"), resourceMap.getIcon("memoryPanel.TabConstraints.tabIcon"), memoryPanel); // NOI18N

        mainSplitPane.setTopComponent(mainTabbedPane);

        consoleSplitPane.setBorder(null);
        consoleSplitPane.setDividerSize(0);
        consoleSplitPane.setResizeWeight(1.0);
        consoleSplitPane.setName("consoleSplitPane"); // NOI18N

        consoleScrollPane.setName("consoleScrollPane"); // NOI18N

        console.setBackground(resourceMap.getColor("console.background")); // NOI18N
        console.setName("console"); // NOI18N
        consoleScrollPane.setViewportView(console);

        consoleSplitPane.setLeftComponent(consoleScrollPane);

        watchScrollPane.setName("watchScrollPane"); // NOI18N
        watchScrollPane.setPreferredSize(new java.awt.Dimension(200, 402));

        watchTable.setAutoCreateRowSorter(true);
        watchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Expression", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        watchTable.setName("watchTable"); // NOI18N
        watchScrollPane.setViewportView(watchTable);

        consoleSplitPane.setRightComponent(watchScrollPane);

        mainSplitPane.setRightComponent(consoleSplitPane);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE))
        );

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 359, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newMenuItem.setAction(actionMap.get("newDoc")); // NOI18N
        newMenuItem.setText(resourceMap.getString("newMenuItem.text")); // NOI18N
        newMenuItem.setName("newMenuItem"); // NOI18N
        fileMenu.add(newMenuItem);

        openMenuItem.setAction(actionMap.get("open")); // NOI18N
        openMenuItem.setText(resourceMap.getString("openMenuItem.text")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        fileMenu.add(openMenuItem);

        saveMenuItem.setAction(actionMap.get("save")); // NOI18N
        saveMenuItem.setText(resourceMap.getString("saveMenuItem.text")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAction(actionMap.get("saveAs")); // NOI18N
        saveAsMenuItem.setName("saveAsMenuItem"); // NOI18N
        fileMenu.add(saveAsMenuItem);

        saveBinMenuItem.setAction(actionMap.get("saveBin")); // NOI18N
        saveBinMenuItem.setEnabled(false);
        saveBinMenuItem.setName("saveBinMenuItem"); // NOI18N
        fileMenu.add(saveBinMenuItem);

        fileMenuSeparator.setName("fileMenuSeparator"); // NOI18N
        fileMenu.add(fileMenuSeparator);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N

        undoMenuItem.setAction(actionMap.get("undo")); // NOI18N
        undoMenuItem.setText(resourceMap.getString("undoMenuItem.text")); // NOI18N
        undoMenuItem.setEnabled(false);
        undoMenuItem.setName("undoMenuItem"); // NOI18N
        editMenu.add(undoMenuItem);

        redoMenuItem.setAction(actionMap.get("redo")); // NOI18N
        redoMenuItem.setText(resourceMap.getString("redoMenuItem.text")); // NOI18N
        redoMenuItem.setEnabled(false);
        redoMenuItem.setName("redoMenuItem"); // NOI18N
        editMenu.add(redoMenuItem);

        menuBar.add(editMenu);

        debugMenu.setText(resourceMap.getString("debugMenu.text")); // NOI18N
        debugMenu.setName("debugMenu"); // NOI18N

        compileMenuItem.setAction(actionMap.get("compile")); // NOI18N
        compileMenuItem.setText(resourceMap.getString("compileMenuItem.text")); // NOI18N
        compileMenuItem.setName("compileMenuItem"); // NOI18N
        debugMenu.add(compileMenuItem);

        runMenuItem.setAction(actionMap.get("run")); // NOI18N
        runMenuItem.setEnabled(false);
        runMenuItem.setName("runMenuItem"); // NOI18N
        debugMenu.add(runMenuItem);

        stopMenuItem.setAction(actionMap.get("stop")); // NOI18N
        stopMenuItem.setText(resourceMap.getString("stopMenuItem.text")); // NOI18N
        stopMenuItem.setEnabled(false);
        stopMenuItem.setName("stopMenuItem"); // NOI18N
        debugMenu.add(stopMenuItem);

        stepMenuItem.setAction(actionMap.get("step")); // NOI18N
        stepMenuItem.setEnabled(false);
        stepMenuItem.setName("stepMenuItem"); // NOI18N
        debugMenu.add(stepMenuItem);

        menuBar.add(debugMenu);

        emptyMenu.setText(resourceMap.getString("emptyMenu.text")); // NOI18N
        emptyMenu.setEnabled(false);
        emptyMenu.setFocusable(false);
        emptyMenu.setMaximumSize(new java.awt.Dimension(32767, 32767));
        emptyMenu.setName("emptyMenu"); // NOI18N
        menuBar.add(emptyMenu);

        helpMenu.setIcon(resourceMap.getIcon("helpMenu.icon")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N
        helpMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuActionPerformed(evt);
            }
        });

        helpMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        helpMenuItem.setName("helpMenuItem"); // NOI18N
        helpMenu.add(helpMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
    }// </editor-fold>//GEN-END:initComponents

private void helpMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuActionPerformed
    showAboutBox();
}//GEN-LAST:event_helpMenuActionPerformed

private void editorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editorKeyTyped

    if ((evt.isControlDown() && (evt.getKeyChar() == (char) 24 || evt.getKeyChar() == (char) 22)) || (!evt.isActionKey() && !evt.isControlDown() && !evt.isAltDown())) {
        updateLineNum();
        if (saved) {
            saved = false;
            updateTab();
        }
        updateUndoRedo();
    }
}//GEN-LAST:event_editorKeyTyped

    private void updateLineNum() {
        int newLineNum = editor.getLineNum();
        if (newLineNum != lineNum) {
            lineNum = newLineNum;
            StringBuffer buffer = new StringBuffer();
            for (int i = 1; i <= lineNum; i++) {
                for (int j = String.valueOf(i).length(); j < 3; j++) {
                    buffer.append(" ");
                }
                buffer.append(i + "\n");
            }
            buffer.append("\n");
            lineNumArea.setText(buffer.toString());
        }
    }

    private void scrollBarSync() {
        lineNumScrollPane.getVerticalScrollBar().setValue(editorScrollPane.getVerticalScrollBar().getValue());
    }

    @Action
    public void newDoc() {
        if (!saved) {
            int option = JOptionPane.showConfirmDialog(this.getFrame(), "Do you want to save changes to your current file?", "New", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            switch (option) {
                case JOptionPane.YES_OPTION:
                    save();
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
            }
        }
        editor.setText("");
        updateLineNum();
        saved = true;
        currentFile = null;
        updateTab();
        updateUndoRedo();
    }

    @Action
    public void open() {
        if (!saved) {
            int option = JOptionPane.showConfirmDialog(this.getFrame(), "Do you want to save changes to your current file?", "Open", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            switch (option) {
                case JOptionPane.YES_OPTION:
                    save();
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
            }
        }
        if (fileChooser.showOpenDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                StringBuffer text = new StringBuffer();
                while (reader.ready()) {
                    text.append((char) reader.read());
                }
                editor.setText(text.toString());
                updateLineNum();
                currentFile = file;
                saved = true;
                updateTab();
                updateUndoRedo();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this.getFrame(), "File " + file.getName() + " doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.getFrame(), "An error occured while opening.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Action
    public void save() {
        if (currentFile == null) {
            saveAs();
        } else if (!saved) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(currentFile);
                writer.write(this.getSourceCode());
                saved = true;
                updateTab();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.getFrame(), "An error occured while saving this file.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Action
    public void saveAs() {

        if (fileChooser.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!filter.accept(file)) {
                file = new File(file.getAbsolutePath() + ".mips");
            }
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(this.getFrame(), "File " + file.getName() + " already exists, overwrite?", "Overwrite confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.write(this.getSourceCode());
                currentFile = file;
                saved = true;
                updateTab();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.getFrame(), "An error occured while saving this file.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private void updateTab() {
        String title = "MIPS Editor";
        if (currentFile != null) {
            title += " - " + currentFile.getName();
        }
        if (!saved) {
            title += "*";
        }
        mainTabbedPane.setTitleAt(0, title);
    }

    private void updateUndoRedo() {
        if (editor.canUndo()) {
            undoButton.setEnabled(true);
            undoMenuItem.setEnabled(true);
        } else {
            undoButton.setEnabled(false);
            undoMenuItem.setEnabled(false);
        }
        if (editor.canRedo()) {
            redoButton.setEnabled(true);
            redoMenuItem.setEnabled(true);
        } else {
            redoButton.setEnabled(false);
            redoMenuItem.setEnabled(false);
        }
    }

    @Action
    public void undo() {
        editor.undo();
        updateLineNum();
        updateUndoRedo();
    }

    @Action
    public void redo() {
        editor.redo();
        updateLineNum();
        updateUndoRedo();
    }

    @Action
    public void toggleTerminal() {
        Terminal.getInstance().setVisible(terminalToggleButton.isSelected());
    }

    @Action
    public void saveBin() {
        JFileChooser binFileChooser = new JFileChooser();
        if (binFileChooser.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = binFileChooser.getSelectedFile();
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(this.getFrame(), "File " + file.getName() + " already exists, overwrite?", "Overwrite confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.write(SimulatorApp.getApplication().getBinaryCode());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.getFrame(), "An error occured while saving this file.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton compileButton;
    private javax.swing.JMenuItem compileMenuItem;
    private simulator.ui.Console console;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JSplitPane consoleSplitPane;
    private javax.swing.JMenu debugMenu;
    private javax.swing.JPanel debuggerPanel;
    private javax.swing.JScrollPane debuggerScrollPane;
    private simulator.ui.DebuggerTable debuggerTable;
    private javax.swing.JMenu editMenu;
    private simulator.ui.editor.MipsEditor editor;
    private javax.swing.JScrollPane editorScrollPane;
    private javax.swing.JSplitPane editorSplitPane;
    private javax.swing.JMenu emptyMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JSeparator fileMenuSeparator;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JTextArea lineNumArea;
    private javax.swing.JScrollPane lineNumScrollPane;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JTabbedPane mainTabbedPane;
    private simulator.ui.MemoryPanel memoryPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton newButton;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JButton openButton;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton redoButton;
    private javax.swing.JMenuItem redoMenuItem;
    private simulator.ui.RegisterPanel registerPanel;
    private javax.swing.JButton runButton;
    private javax.swing.JMenuItem runMenuItem;
    private javax.swing.JButton saveAsButton;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JButton saveBinButton;
    private javax.swing.JMenuItem saveBinMenuItem;
    private javax.swing.JButton saveButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton stepButton;
    private javax.swing.JMenuItem stepMenuItem;
    private javax.swing.JButton stopButton;
    private javax.swing.JMenuItem stopMenuItem;
    private javax.swing.JToggleButton terminalToggleButton;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JToolBar.Separator toolBarSeparator2;
    private javax.swing.JToolBar.Separator toolBarSeparator3;
    private javax.swing.JToolBar.Separator toolBarSeparator4;
    private javax.swing.JToolBar.Separator toolBarSeparator5;
    private javax.swing.JButton undoButton;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JScrollPane watchScrollPane;
    private javax.swing.JTable watchTable;
    // End of variables declaration//GEN-END:variables
    //private MipsEditor editor;
    //private javax.swing.JScrollPane editorScrollPane;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private JFileChooser fileChooser;
    private FileNameExtensionFilter filter;
    private int lineNum = 0;
    //private String buffer;
    private File currentFile;
    private boolean saved;
    private UIConfig config;
}
