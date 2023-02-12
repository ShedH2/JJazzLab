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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Let user select the visible tracks.
 */
public class ShowTracksPanel extends javax.swing.JPanel
{

    /**
     * newValue = ths list of visible track names.
     */
    public static final String PROP_VISIBLE_TRACK_NAMES = "visibleTrackNames";
    private List<String> trackNames = new ArrayList<>();
    private boolean blockChangeEventFiring = false;
    private static final Logger LOGGER = Logger.getLogger(ShowTracksPanel.class.getSimpleName());

    /**
     * Creates new form ShowTracksPanel
     */
    public ShowTracksPanel()
    {

        initComponents();

        list_tracks.addListSelectionListener(e ->
        {
            if (!e.getValueIsAdjusting() && !blockChangeEventFiring)
            {
                firePropertyChange(PROP_VISIBLE_TRACK_NAMES, null, getVisibleTracks());
            }
        });
    }

    /**
     * Set the track names list.
     *
     * @param names
     */
    public void setTracks(List<String> names)
    {
        list_tracks.clearSelection();        
        trackNames.clear();
        trackNames.addAll(names);
        blockChangeEventFiring = true;
        list_tracks.setListData(names.toArray(String[]::new));
        blockChangeEventFiring = false;
    }


    public void setVisibleTracks(List<String> names)
    {
        blockChangeEventFiring = true;
        list_tracks.clearSelection();
        for (var n : names)
        {
            int index = trackNames.indexOf(n);
            if (index != -1)
            {
                list_tracks.setSelectedIndex(index);
            }
        }
        blockChangeEventFiring = false;
    }

    /**
     * Get the names of the visible tracks.
     *
     * @return
     */
    public List<String> getVisibleTracks()
    {
        List<String> res = new ArrayList<>();
        for (var i : list_tracks.getSelectedIndices())
        {
            res.add(trackNames.get(i));
        }
        return res;
    }

    /**
     * Overridden because the JList is embedded in a JScollPane.
     * <p>
     * Note: JList size is sized indirectly via setPrototypeCellValue() in initComponents().
     *
     * @return
     */
    @Override
    public Dimension getPreferredSize()
    {
        var d = super.getPreferredSize();
        var ld = list_tracks.getPreferredSize();
        if (ld.width > d.width)
        {
            d.width = ld.width; // JScrollPane scrollbars width should be added too
        }
        return d;
    }
    
     /**
     * Overridden because the JList is embedded in a JScollPane.
     * <p>
     * Note: JList is sized indirectly via setPrototypeCellValue() in initComponents().
     *
     * @return
     */
    @Override
    public Dimension getMinimumSize()
    {
        var d = super.getMinimumSize();
        var ld = list_tracks.getMinimumSize();
        if (ld.width > d.width)
        {
            d.width = ld.width; // JScrollPane scrollbars width should be added too
        }
        return d;
    }

    // ==============================================================================================================
    // Private methods
    // ==============================================================================================================

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        list_tracks = new javax.swing.JList<>();
        btn_clearAll = new javax.swing.JButton();

        list_tracks.setToolTipText(org.openide.util.NbBundle.getMessage(ShowTracksPanel.class, "ShowTracksPanel.list_tracks.toolTipText")); // NOI18N
        list_tracks.setPrototypeCellValue("10: Phrase2 - elec. guitar");
        list_tracks.setVisibleRowCount(9);
        jScrollPane1.setViewportView(list_tracks);

        org.openide.awt.Mnemonics.setLocalizedText(btn_clearAll, org.openide.util.NbBundle.getMessage(ShowTracksPanel.class, "ShowTracksPanel.btn_clearAll.text")); // NOI18N
        btn_clearAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_clearAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_clearAll)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_clearAll)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_clearAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_clearAllActionPerformed
    {//GEN-HEADEREND:event_btn_clearAllActionPerformed
        list_tracks.clearSelection();
    }//GEN-LAST:event_btn_clearAllActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_clearAll;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> list_tracks;
    // End of variables declaration//GEN-END:variables


}
