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
package org.jjazz.ui.mixconsole.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import org.jjazz.midi.api.InstrumentMix;
import org.jjazz.midi.api.InstrumentSettings;
import org.jjazz.midimix.api.MidiMix;
import org.jjazz.outputsynth.api.OutputSynthManager;
import org.jjazz.util.api.ResUtil;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.actions.Presenter;

@ActionID(category = "MixConsole", id = "org.jjazz.ui.mixconsole.actions.toggleuserchannel")
@ActionRegistration(displayName = "#CTL_ToggleUserChannel", lazy = false) // lazy must be false for Presenter.Popup to work
@ActionReferences(
        {
            @ActionReference(path = "Actions/MixConsole/MenuBar/Edit", position = 150)
        })
public class ToggleUserChannel extends AbstractAction implements Presenter.Menu
{
    private final String undoText = ResUtil.getString(getClass(), "CTL_ToggleUserChannel");
    private JCheckBoxMenuItem checkbox;
    private MidiMix songMidiMix;
    private final LookupListener lookupListener;
    private final Lookup.Result<MidiMix> result;
    private static final Logger LOGGER = Logger.getLogger(ToggleUserChannel.class.getSimpleName());

    public ToggleUserChannel()
    {
        putValue(NAME, undoText);
        putValue(Action.SHORT_DESCRIPTION, ResUtil.getString(getClass(), "CTL_ToggleUserChannelTooltip"));
        result = Utilities.actionsGlobalContext().lookupResult(MidiMix.class);
        lookupListener = new LookupListener()
        {
            @Override
            public void resultChanged(LookupEvent ev)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Collection<? extends MidiMix> res = result.allInstances();
                        assert res.size() < 2 : "res=" + res;   //NOI18N
                        setEnabled(!res.isEmpty());
                        if (res.isEmpty())
                        {
                            songMidiMix = null;
                        } else
                        {
                            songMidiMix = res.iterator().next();
                            if (checkbox != null)
                            {
                                checkbox.setSelected(songMidiMix.getUserChannel() != -1);
                            }
                        }
                    }
                });
            }
        };
        result.addLookupListener(WeakListeners.create(LookupListener.class, this.lookupListener, result));
        lookupListener.resultChanged(null);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        LOGGER.fine("actionPerformed() songMidiMix=" + songMidiMix);   //NOI18N
        assert checkbox != null;   //NOI18N
        if (!checkbox.isSelected())
        {
            songMidiMix.removeUserChannel();
        } else
        {
            InstrumentMix insMix = new InstrumentMix(OutputSynthManager.getInstance().getOutputSynth().getUserInstrument(), new InstrumentSettings());
            try
            {
                songMidiMix.addUserChannel(insMix, -1);
            } catch (MidiUnavailableException ex)
            {
                NotifyDescriptor d = new NotifyDescriptor.Message(ex.getLocalizedMessage(), NotifyDescriptor.WARNING_MESSAGE);
                DialogDisplayer.getDefault().notify(d);
                checkbox.setSelected(false);
            }
        }
    }

    // ============================================================================================= 
    // Presenter.Menu implementation
    // =============================================================================================      
    @Override
    public JMenuItem getMenuPresenter()
    {
        checkbox = new JCheckBoxMenuItem();
        checkbox.setSelected(false);
        checkbox.setAction(this);
        return checkbox;
    }

}
