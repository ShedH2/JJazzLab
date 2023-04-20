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
package org.jjazz.ui.cl_editor.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jjazz.leadsheet.chordleadsheet.api.item.CLI_ChordSymbol;
import org.jjazz.ui.cl_editor.api.CL_EditorTopComponent;
import org.jjazz.ui.cl_editor.api.CL_Editor;
import org.jjazz.ui.cl_editor.api.CL_SelectionUtilities;
import org.jjazz.ui.itemrenderer.api.IR_ChordSymbolSettings;
import org.jjazz.util.api.ResUtil;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.actions.Presenter;

/**
 * Allow user to change color of selected chords via a JPopupMenu.
 */
@ActionID(category = "JJazz", id = "org.jjazz.ui.cl_editor.actions.SetChordColor")
@ActionRegistration(displayName = "#CTL_SetChordColor", lazy = false)
@ActionReferences(
        {
            @ActionReference(path = "Actions/ChordSymbol", position = 2000, separatorBefore = 1999)
        })
public final class SetChordColor extends AbstractAction implements Presenter.Popup
{

    private static final Color DEFAULT_COLOR = IR_ChordSymbolSettings.getDefault().getColor();
    public static final Color[] COLORS =
    {
        DEFAULT_COLOR,
        new Color(0x026a2e),
        new Color(0xb73003),
        new Color(0x004699)
    };
    private ColorMenu menu;
    private final String undoText = ResUtil.getString(getClass(), "CTL_SetChordColor");
    private static final Logger LOGGER = Logger.getLogger(SetChordColor.class.getSimpleName());

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Useless
    }


    // ============================================================================================= 
    // Presenter.Popup implementation
    // =============================================================================================      
    @Override
    public JMenuItem getPopupPresenter()
    {
        if (menu == null)
        {
            menu = new ColorMenu(undoText);
        }
        return menu;
    }

    // ============================================================================================= 
    // Private methods
    // =============================================================================================    

    // ============================================================================================= 
    // Private class
    // =============================================================================================    
    private static class ColorMenu extends JMenu
    {

        public ColorMenu(String title)
        {
            super(title);

            prepareMenu();
        }


        private void prepareMenu()
        {
            removeAll();

            for (final Color c : COLORS)
            {
                JMenuItem mi = new JMenuItem("    ");
                mi.setEnabled(true);
                mi.setOpaque(true);
                mi.setBackground(c);
                mi.addActionListener(ae -> 
                {
                    CL_Editor editor = CL_EditorTopComponent.getActive().getEditor();
                    CL_SelectionUtilities selection = new CL_SelectionUtilities(editor.getLookup());
                    for (var item : selection.getSelectedItems())
                    {
                        if (item instanceof CLI_ChordSymbol cliCs)
                        {
                            Color cc = c == DEFAULT_COLOR ? null : c;
                            cliCs.getClientProperties().putColor(
                                    IR_ChordSymbolSettings.SONG_CLIENT_PROPERTY_USER_FONT_COLOR, cc);
                        }
                    }
                });
                add(mi);
            }
        }
    }
}
