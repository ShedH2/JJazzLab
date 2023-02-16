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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import org.jjazz.pianoroll.actions.Quantize;
import org.jjazz.pianoroll.api.PianoRollEditor;
import org.jjazz.quantizer.api.Quantizer;

/**
 * Quantize panel.
 */
public class QuantizePanel extends javax.swing.JPanel implements PropertyChangeListener
{

    private static final Logger LOGGER = Logger.getLogger(QuantizePanel.class.getSimpleName());
    private final PianoRollEditor editor;


    /**
     * Creates new form ToolsSidePanel
     */
    public QuantizePanel(PianoRollEditor editor)
    {
        this.editor = editor;

        initComponents();

        var quantizer = Quantizer.getInstance();
        cb_iterative.setSelected(quantizer.isIterativeQuantizeEnabled());
        quantizer.addPropertyChangeListener(this);
    }

    public void cleanup()
    {
        Quantizer.getInstance().removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        var quantizer = Quantizer.getInstance();
        if (evt.getSource() == quantizer)
        {
            if (evt.getPropertyName().equals(Quantizer.PROP_ITERATIVE_ENABLED))
            {
                cb_iterative.setSelected(quantizer.isIterativeQuantizeEnabled());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btn_quantize = new javax.swing.JButton();
        cb_iterative = new javax.swing.JCheckBox();

        org.openide.awt.Mnemonics.setLocalizedText(btn_quantize, org.openide.util.NbBundle.getMessage(QuantizePanel.class, "QuantizePanel.btn_quantize.text")); // NOI18N
        btn_quantize.setToolTipText(org.openide.util.NbBundle.getMessage(QuantizePanel.class, "QuantizePanel.btn_quantize.toolTipText")); // NOI18N
        btn_quantize.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_quantizeActionPerformed(evt);
            }
        });

        cb_iterative.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(cb_iterative, org.openide.util.NbBundle.getMessage(QuantizePanel.class, "QuantizePanel.cb_iterative.text")); // NOI18N
        cb_iterative.setToolTipText(org.openide.util.NbBundle.getMessage(QuantizePanel.class, "QuantizePanel.cb_iterative.toolTipText")); // NOI18N
        cb_iterative.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cb_iterativeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_quantize, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cb_iterative)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_quantize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_iterative)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cb_iterativeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cb_iterativeActionPerformed
    {//GEN-HEADEREND:event_cb_iterativeActionPerformed
        Quantizer.getInstance().setIterativeQuantizeEnabled(cb_iterative.isSelected());
    }//GEN-LAST:event_cb_iterativeActionPerformed

    private void btn_quantizeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_quantizeActionPerformed
    {//GEN-HEADEREND:event_btn_quantizeActionPerformed
        editor.getAction(Quantize.ACTION_ID).actionPerformed(evt);
    }//GEN-LAST:event_btn_quantizeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_quantize;
    private javax.swing.JCheckBox cb_iterative;
    // End of variables declaration//GEN-END:variables


}
