/*
 * RegisterPanel.java
 *
 * Created on December 21, 2008, 3:39 AM
 */
package simulator.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import simulator.core.Misc;

/**
 *
 * @author  LeeThree
 */
public class RegisterPanel extends javax.swing.JPanel {

    /** Creates new form RegisterPanel */
    public RegisterPanel() {
        initComponents();
    }

    public void setConfig(UIConfig config) {
        this.config = config;
    //update();
    }

    public void update() {
        if (config == null) {
            return;
        }
        if (decRadioButton.isSelected()) {
            setFieldText(r0Field, Short.toString(config.registers[0]));
            setFieldText(r1Field, Short.toString(config.registers[1]));
            setFieldText(r2Field, Short.toString(config.registers[2]));
            setFieldText(r3Field, Short.toString(config.registers[3]));
            setFieldText(r4Field, Short.toString(config.registers[4]));
            setFieldText(r5Field, Short.toString(config.registers[5]));
            setFieldText(t6Field, Short.toString(config.registers[6]));
            setFieldText(r7Field, Short.toString(config.registers[7]));
            setFieldText(pcField, Short.toString(config.PC));
            setFieldText(spField, Short.toString(config.SP));
            setFieldText(ihField, Short.toString(config.IH));
            setFieldText(raField, Short.toString(config.RA));
            setFieldText(tField, Short.toString(config.T));
        } else if (hexRadioButton.isSelected()) {
            setFieldText(r0Field, Misc.toHex(config.registers[0]));
            setFieldText(r1Field, Misc.toHex(config.registers[1]));
            setFieldText(r2Field, Misc.toHex(config.registers[2]));
            setFieldText(r3Field, Misc.toHex(config.registers[3]));
            setFieldText(r4Field, Misc.toHex(config.registers[4]));
            setFieldText(r5Field, Misc.toHex(config.registers[5]));
            setFieldText(t6Field, Misc.toHex(config.registers[6]));
            setFieldText(r7Field, Misc.toHex(config.registers[7]));
            setFieldText(pcField, Misc.toHex(config.PC));
            setFieldText(spField, Misc.toHex(config.SP));
            setFieldText(ihField, Misc.toHex(config.IH));
            setFieldText(raField, Misc.toHex(config.RA));
            setFieldText(tField, Misc.toHex(config.T));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        genRegPanel = new javax.swing.JPanel();
        r0Label = new javax.swing.JLabel();
        r1Label = new javax.swing.JLabel();
        r0Field = new javax.swing.JTextField();
        r1Field = new javax.swing.JTextField();
        r5Label = new javax.swing.JLabel();
        r4Label = new javax.swing.JLabel();
        r2Label = new javax.swing.JLabel();
        r2Field = new javax.swing.JTextField();
        r6Label = new javax.swing.JLabel();
        r5Field = new javax.swing.JTextField();
        t6Field = new javax.swing.JTextField();
        r4Field = new javax.swing.JTextField();
        r3Label = new javax.swing.JLabel();
        r3Field = new javax.swing.JTextField();
        r7Label = new javax.swing.JLabel();
        r7Field = new javax.swing.JTextField();
        spcRegPanel = new javax.swing.JPanel();
        pcLabel = new javax.swing.JLabel();
        ihLabel = new javax.swing.JLabel();
        pcField = new javax.swing.JTextField();
        ihField = new javax.swing.JTextField();
        raLabel = new javax.swing.JLabel();
        spLabel = new javax.swing.JLabel();
        tLabel = new javax.swing.JLabel();
        raField = new javax.swing.JTextField();
        tField = new javax.swing.JTextField();
        spField = new javax.swing.JTextField();
        hexRadioButton = new javax.swing.JRadioButton();
        decRadioButton = new javax.swing.JRadioButton();
        separator = new javax.swing.JSeparator();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(simulator.SimulatorApp.class).getContext().getResourceMap(RegisterPanel.class);
        setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("Form.border.title"))); // NOI18N
        setName("Form"); // NOI18N

        genRegPanel.setName("genRegPanel"); // NOI18N

        r0Label.setText(resourceMap.getString("r0Label.text")); // NOI18N
        r0Label.setName("r0Label"); // NOI18N

        r1Label.setText(resourceMap.getString("r1Label.text")); // NOI18N
        r1Label.setName("r1Label"); // NOI18N

        r0Field.setEditable(false);
        r0Field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r0Field.setText(resourceMap.getString("r0Field.text")); // NOI18N
        r0Field.setName("r0Field"); // NOI18N

        r1Field.setEditable(false);
        r1Field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r1Field.setText(resourceMap.getString("r1Field.text")); // NOI18N
        r1Field.setName("r1Field"); // NOI18N

        r5Label.setText(resourceMap.getString("r5Label.text")); // NOI18N
        r5Label.setName("r5Label"); // NOI18N

        r4Label.setText(resourceMap.getString("r4Label.text")); // NOI18N
        r4Label.setName("r4Label"); // NOI18N

        r2Label.setText(resourceMap.getString("r2Label.text")); // NOI18N
        r2Label.setName("r2Label"); // NOI18N

        r2Field.setEditable(false);
        r2Field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r2Field.setText(resourceMap.getString("r2Field.text")); // NOI18N
        r2Field.setName("r2Field"); // NOI18N

        r6Label.setText(resourceMap.getString("r6Label.text")); // NOI18N
        r6Label.setName("r6Label"); // NOI18N

        r5Field.setEditable(false);
        r5Field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r5Field.setText(resourceMap.getString("r5Field.text")); // NOI18N
        r5Field.setName("r5Field"); // NOI18N

        t6Field.setEditable(false);
        t6Field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t6Field.setText(resourceMap.getString("t6Field.text")); // NOI18N
        t6Field.setName("t6Field"); // NOI18N

        r4Field.setEditable(false);
        r4Field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r4Field.setText(resourceMap.getString("r4Field.text")); // NOI18N
        r4Field.setName("r4Field"); // NOI18N

        r3Label.setText(resourceMap.getString("r3Label.text")); // NOI18N
        r3Label.setName("r3Label"); // NOI18N

        r3Field.setEditable(false);
        r3Field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r3Field.setText(resourceMap.getString("r3Field.text")); // NOI18N
        r3Field.setName("r3Field"); // NOI18N

        r7Label.setText(resourceMap.getString("r7Label.text")); // NOI18N
        r7Label.setName("r7Label"); // NOI18N

        r7Field.setEditable(false);
        r7Field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r7Field.setText(resourceMap.getString("r7Field.text")); // NOI18N
        r7Field.setName("r7Field"); // NOI18N

        javax.swing.GroupLayout genRegPanelLayout = new javax.swing.GroupLayout(genRegPanel);
        genRegPanel.setLayout(genRegPanelLayout);
        genRegPanelLayout.setHorizontalGroup(
            genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(genRegPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r3Label)
                    .addComponent(r0Label)
                    .addComponent(r1Label)
                    .addComponent(r2Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r3Field, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(r2Field, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(r1Field, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(r0Field, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r5Label)
                    .addComponent(r4Label)
                    .addComponent(r6Label)
                    .addComponent(r7Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r7Field, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(t6Field, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(r5Field, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(r4Field, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                .addContainerGap())
        );
        genRegPanelLayout.setVerticalGroup(
            genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(genRegPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(r0Label)
                    .addComponent(r0Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r4Label)
                    .addComponent(r4Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(r1Label)
                    .addComponent(r1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r5Label)
                    .addComponent(r5Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(r2Label)
                    .addComponent(r2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r6Label)
                    .addComponent(t6Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(genRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(r3Label)
                    .addComponent(r3Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r7Label)
                    .addComponent(r7Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        spcRegPanel.setName("spcRegPanel"); // NOI18N

        pcLabel.setText(resourceMap.getString("pcLabel.text")); // NOI18N
        pcLabel.setName("pcLabel"); // NOI18N

        ihLabel.setText(resourceMap.getString("ihLabel.text")); // NOI18N
        ihLabel.setName("ihLabel"); // NOI18N

        pcField.setEditable(false);
        pcField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pcField.setName("pcField"); // NOI18N

        ihField.setEditable(false);
        ihField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ihField.setName("ihField"); // NOI18N

        raLabel.setText(resourceMap.getString("raLabel.text")); // NOI18N
        raLabel.setName("raLabel"); // NOI18N

        spLabel.setText(resourceMap.getString("spLabel.text")); // NOI18N
        spLabel.setName("spLabel"); // NOI18N

        tLabel.setText(resourceMap.getString("tLabel.text")); // NOI18N
        tLabel.setName("tLabel"); // NOI18N

        raField.setEditable(false);
        raField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        raField.setName("raField"); // NOI18N

        tField.setEditable(false);
        tField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tField.setName("tField"); // NOI18N

        spField.setEditable(false);
        spField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        spField.setName("spField"); // NOI18N

        buttonGroup.add(hexRadioButton);
        hexRadioButton.setSelected(true);
        hexRadioButton.setText(resourceMap.getString("hexRadioButton.text")); // NOI18N
        hexRadioButton.setName("hexRadioButton"); // NOI18N
        hexRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hexRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(decRadioButton);
        decRadioButton.setText(resourceMap.getString("decRadioButton.text")); // NOI18N
        decRadioButton.setName("decRadioButton"); // NOI18N
        decRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout spcRegPanelLayout = new javax.swing.GroupLayout(spcRegPanel);
        spcRegPanel.setLayout(spcRegPanelLayout);
        spcRegPanelLayout.setHorizontalGroup(
            spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spcRegPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, spcRegPanelLayout.createSequentialGroup()
                        .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pcLabel)
                            .addComponent(ihLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ihField, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(pcField, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(raLabel)
                            .addComponent(spLabel)
                            .addComponent(tLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tField, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(raField, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(spField, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                    .addGroup(spcRegPanelLayout.createSequentialGroup()
                        .addComponent(hexRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(decRadioButton)))
                .addContainerGap())
        );
        spcRegPanelLayout.setVerticalGroup(
            spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spcRegPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcLabel)
                    .addComponent(pcField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spLabel)
                    .addComponent(spField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ihLabel)
                    .addComponent(ihField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(raLabel)
                    .addComponent(raField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tLabel)
                    .addComponent(tField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(spcRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(decRadioButton)
                    .addComponent(hexRadioButton)))
        );

        separator.setOrientation(javax.swing.SwingConstants.VERTICAL);
        separator.setName("separator"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(genRegPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(spcRegPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(separator)
                    .addComponent(spcRegPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(genRegPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private static void setFieldText(JTextField field, String text) {
        if (text.equals(field.getText())) {
            field.setFont(defaultFont);
            field.setForeground(Color.BLACK);
        } else {
            field.setText(text);
            field.setFont(newFont);
            field.setForeground(Color.RED);
        }
    }

private void hexRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hexRadioButtonActionPerformed
    update();
}//GEN-LAST:event_hexRadioButtonActionPerformed

private void decRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decRadioButtonActionPerformed
    update();
}//GEN-LAST:event_decRadioButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JRadioButton decRadioButton;
    private javax.swing.JPanel genRegPanel;
    private javax.swing.JRadioButton hexRadioButton;
    private javax.swing.JTextField ihField;
    private javax.swing.JLabel ihLabel;
    private javax.swing.JTextField pcField;
    private javax.swing.JLabel pcLabel;
    private javax.swing.JTextField r0Field;
    private javax.swing.JLabel r0Label;
    private javax.swing.JTextField r1Field;
    private javax.swing.JLabel r1Label;
    private javax.swing.JTextField r2Field;
    private javax.swing.JLabel r2Label;
    private javax.swing.JTextField r3Field;
    private javax.swing.JLabel r3Label;
    private javax.swing.JTextField r4Field;
    private javax.swing.JLabel r4Label;
    private javax.swing.JTextField r5Field;
    private javax.swing.JLabel r5Label;
    private javax.swing.JLabel r6Label;
    private javax.swing.JTextField r7Field;
    private javax.swing.JLabel r7Label;
    private javax.swing.JTextField raField;
    private javax.swing.JLabel raLabel;
    private javax.swing.JSeparator separator;
    private javax.swing.JTextField spField;
    private javax.swing.JLabel spLabel;
    private javax.swing.JPanel spcRegPanel;
    private javax.swing.JTextField t6Field;
    private javax.swing.JTextField tField;
    private javax.swing.JLabel tLabel;
    // End of variables declaration//GEN-END:variables
    private UIConfig config;
    private static final Font defaultFont = new Font(Font.MONOSPACED, Font.PLAIN, 11);
    private static final Font newFont = new Font(Font.MONOSPACED, Font.BOLD, 11);
}