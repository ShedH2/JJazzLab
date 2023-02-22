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
package org.jjazz.ui.zoomablesliders;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.jjazz.ui.utilities.api.Zoomable;
import org.openide.awt.Actions;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.*;
import org.openide.util.lookup.ServiceProvider;

/**
 * A status bar Zoom X component.
 */
@ServiceProvider(service = StatusLineElementProvider.class, position = 100)
public class ZoomXWidget extends javax.swing.JPanel implements StatusLineElementProvider, PropertyChangeListener
{

    private final Lookup context;
    private Zoomable currentZoomable;
    private final Lookup.Result<Zoomable> lkpResult;
    private final LookupListener lkpListener;
    private static final Logger LOGGER = Logger.getLogger(ZoomXWidget.class.getSimpleName());

    public ZoomXWidget()
    {
        initComponents();

        // Listen to Zoomable object in the lookup
        context = org.openide.util.Utilities.actionsGlobalContext();
        lkpResult = context.lookupResult(Zoomable.class);
        // For WeakReferences to work, we need to keep a strong reference on the listeners (see WeakListeners java doc).
        lkpListener = new LookupListener()
        {
            @Override
            public void resultChanged(LookupEvent le)
            {
                zoomableUpdated();
            }
        };
        // Need to use WeakListeners so than action can be GC'ed
        // See http://forums.netbeans.org/viewtopic.php?t=35921
        lkpResult.addLookupListener(WeakListeners.create(LookupListener.class, lkpListener, lkpResult));

        setEnabled(false);
    }

  

    @Override
    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        label.setEnabled(b);
        slider.setEnabled(b);
    }

    @Override
    public Component getStatusLineElement()
    {
        return this;
    }

    private void zoomableUpdated()
    {
        if (currentZoomable != null)
        {
            currentZoomable.removePropertyListener(this);
        }
        currentZoomable = context.lookup(Zoomable.class);
        if (currentZoomable != null && currentZoomable.getZoomCapabilities().equals(Zoomable.Capabilities.Y_ONLY))
        {
            currentZoomable = null;
        }
        if (currentZoomable != null)
        {
            currentZoomable.addPropertyListener(this);
            int x = currentZoomable.getZoomXFactor();
            slider.setValue(x);
            slider.setToolTipText(String.valueOf(x));
        }
        setEnabled(currentZoomable != null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals(Zoomable.PROPERTY_ZOOM_X))
        {
            int newFactor = (int) evt.getNewValue();
            if (newFactor < 0 || newFactor > 100)
            {
                throw new IllegalStateException("factor=" + newFactor);   
            }
            slider.setValue(newFactor);
            slider.setToolTipText(String.valueOf(newFactor));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * <p>
     * WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        label = new javax.swing.JLabel();
        slider = org.jjazz.ui.utilities.api.Utilities.buildSlider(SwingConstants.HORIZONTAL, 0.7f);
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 32767));

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/ui/zoomablesliders/resources/zoomXarrow.png"))); // NOI18N
        label.setToolTipText(org.openide.util.NbBundle.getMessage(ZoomXWidget.class, "ZoomXWidget.label.toolTipText")); // NOI18N
        label.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                labelMouseClicked(evt);
            }
        });
        add(label);

        slider.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                sliderStateChanged(evt);
            }
        });
        slider.addMouseWheelListener(new java.awt.event.MouseWheelListener()
        {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt)
            {
                sliderMouseWheelMoved(evt);
            }
        });
        add(slider);
        add(filler1);
    }// </editor-fold>//GEN-END:initComponents

    private void labelMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_labelMouseClicked
    {//GEN-HEADEREND:event_labelMouseClicked
        if (evt.getClickCount() == 1
                && SwingUtilities.isLeftMouseButton(evt)
                && ((evt.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK
                || (evt.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK))
        {
            // Simple CTRL-CLICK
            Action a = Actions.forID("JJazz", "org.jjazz.ui.zoomablesliders.zoomfitwidth");   
            if (a == null)
            {
                LOGGER.warning("Can't find the ZoomFitWidth action: org.jjazz.ui.zoomablesliders.zoomfitwidth");   
            } else
            {
                a.actionPerformed(null);
            }
        }

    }//GEN-LAST:event_labelMouseClicked

    private void sliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_sliderStateChanged
    {//GEN-HEADEREND:event_sliderStateChanged
        if (currentZoomable != null)
        {
            int newValue = slider.getValue();
            currentZoomable.setZoomXFactor(newValue, slider.getValueIsAdjusting());
        }
    }//GEN-LAST:event_sliderStateChanged

    private void sliderMouseWheelMoved(java.awt.event.MouseWheelEvent evt)//GEN-FIRST:event_sliderMouseWheelMoved
    {//GEN-HEADEREND:event_sliderMouseWheelMoved
        if (!isEnabled())
        {
            return;
        }
        int value = slider.getValue();
        boolean ctrl = (evt.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK;
        int step = ctrl ? 5 : 1;
        if (evt.getWheelRotation() < 0)
        {
            if (value + step <= slider.getMaximum())
            {
                slider.setValue(value + step);
            } else
            {
                slider.setValue(slider.getMaximum());
            }

        } else if (evt.getWheelRotation() > 0)
        {
            if (value - step >= slider.getMinimum())
            {
                slider.setValue(value - step);
            } else
            {
                slider.setValue(slider.getMinimum());
            }
        }
    }//GEN-LAST:event_sliderMouseWheelMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel label;
    private javax.swing.JSlider slider;
    // End of variables declaration//GEN-END:variables
}
