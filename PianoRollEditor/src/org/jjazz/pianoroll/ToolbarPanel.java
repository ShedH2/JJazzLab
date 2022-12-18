/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 *  Copyright @2019 Jerome Lelasseux. All rights reserved.
 *
 *  This file is part of the JJazzLabX software.
 *   
 *  JJazzLabX is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License (LGPLv3) 
 *  as published by the Free Software Foundation, either version 3 of the License, 
 *  or (at your option) any later version.
 *
 *  JJazzLabX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with JJazzLabX.  If not, see <https://www.gnu.org/licenses/>
 * 
 *  Contributor(s): 
 */
package org.jjazz.pianoroll;

import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Timer;
import org.jjazz.quantizer.api.Quantization;


/**
 * Toolbar panel of the PianoRollEditor.
 */
public class ToolbarPanel extends javax.swing.JPanel
{

    private final PianoRollEditorImpl editor;

    /**
     * Creates new form ToolbarPanel
     */
    public ToolbarPanel(PianoRollEditorImpl editor)
    {
        this.editor = editor;
        initComponents();
        
        var qModel = new DefaultComboBoxModel(Quantization.values());
        qModel.removeElement(Quantization.HALF_BAR);
        cmb_quantization.setModel(qModel);
        cmb_quantization.setSelectedItem(editor.getQuantization());
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        cmb_quantization = new javax.swing.JComboBox<>();
        cb_snap = new javax.swing.JCheckBox();
        btn_test1 = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        fbtn_select = new org.jjazz.ui.flatcomponents.api.FlatToggleButton();
        fbtn_pencil = new org.jjazz.ui.flatcomponents.api.FlatToggleButton();
        fbtn_eraser = new org.jjazz.ui.flatcomponents.api.FlatToggleButton();

        cmb_quantization.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmb_quantizationActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cb_snap, org.openide.util.NbBundle.getMessage(ToolbarPanel.class, "ToolbarPanel.cb_snap.text")); // NOI18N
        cb_snap.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cb_snapActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btn_test1, org.openide.util.NbBundle.getMessage(ToolbarPanel.class, "ToolbarPanel.btn_test1.text")); // NOI18N
        btn_test1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_test1ActionPerformed(evt);
            }
        });

        jToolBar1.setRollover(true);

        fbtn_select.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/pianoroll/edittools/resources/SelectionToolOFF.png"))); // NOI18N
        fbtn_select.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/pianoroll/edittools/resources/SelectionToolON.png"))); // NOI18N
        jToolBar1.add(fbtn_select);

        fbtn_pencil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/pianoroll/edittools/resources/PencilOFF.png"))); // NOI18N
        fbtn_pencil.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/pianoroll/edittools/resources/PencilON.png"))); // NOI18N
        jToolBar1.add(fbtn_pencil);

        fbtn_eraser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/pianoroll/edittools/resources/EraserOFF.png"))); // NOI18N
        fbtn_eraser.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/pianoroll/edittools/resources/EraserON.png"))); // NOI18N
        jToolBar1.add(fbtn_eraser);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmb_quantization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_snap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_test1)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmb_quantization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_snap)
                        .addComponent(btn_test1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cb_snapActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cb_snapActionPerformed
    {//GEN-HEADEREND:event_cb_snapActionPerformed
        editor.setSnapEnabled(cb_snap.isEnabled());
    }//GEN-LAST:event_cb_snapActionPerformed

    private void cmb_quantizationActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmb_quantizationActionPerformed
    {//GEN-HEADEREND:event_cmb_quantizationActionPerformed
        editor.setQuantization((Quantization) cmb_quantization.getSelectedItem());
    }//GEN-LAST:event_cmb_quantizationActionPerformed

    private void btn_test1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_test1ActionPerformed
    {//GEN-HEADEREND:event_btn_test1ActionPerformed
        var yRange = editor.getVisiblePitchRange();
        int p= (int) yRange.getCenter();
        var key = editor.getKeyboardComponent().getKey(p);
        key.setPressed(100, Color.red);
        Timer timer = new Timer(1000, event -> key.release());
        timer.setRepeats(false);
        timer.start();        
    }//GEN-LAST:event_btn_test1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_test1;
    private javax.swing.JCheckBox cb_snap;
    private javax.swing.JComboBox<Quantization> cmb_quantization;
    private org.jjazz.ui.flatcomponents.api.FlatToggleButton fbtn_eraser;
    private org.jjazz.ui.flatcomponents.api.FlatToggleButton fbtn_pencil;
    private org.jjazz.ui.flatcomponents.api.FlatToggleButton fbtn_select;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
