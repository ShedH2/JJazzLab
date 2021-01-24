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
package org.jjazz.ui.utilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.openide.awt.MenuBar;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.actions.ActionPresenterProvider;
import org.openide.util.actions.Presenter;
import org.openide.windows.WindowManager;

public class Utilities
{

    private static JFileChooser fileChooser;
    private static final NoAction NoActionInstance = new NoAction();

    public static JFileChooser getFileChooserInstance()
    {
        synchronized (Utilities.class)
        {
            if (fileChooser == null)
            {
                fileChooser = new JFileChooser();
            }
        }
        return fileChooser;
    }

    /**
     * Get the current screen bounds where specified component is displayed, excluding possible taskbars.
     * <p>
     * Supposed to handle correctly multiple monitors on various OS.<p>
     * See https://stackoverflow.com/questions/10123735/get-effective-screen-size-from-java/10123912 (answer of Rasmus Faber)
     *
     * @param c
     * @return
     */
    public static Rectangle getEffectiveScreenArea(Component c)
    {
        GraphicsConfiguration gc = c.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

        var res = new Rectangle();
        res.x = bounds.x + screenInsets.left;
        res.y = bounds.y + screenInsets.top;
        res.height = bounds.height - screenInsets.top - screenInsets.bottom;
        res.width = bounds.width - screenInsets.left - screenInsets.right;

        return res;
    }

    /**
     * If already on the EDT, call run.run(), otherwise use SwingUtilities.invokeLater(run).
     *
     * @param run
     */
    public static void invokeLaterIfNeeded(Runnable run)
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            run.run();
        } else
        {
            SwingUtilities.invokeLater(run);
        }
    }

    /**
     * Create one or more JMenuItems or JSeparators from a Netbeans action.
     * <p>
     * Copied from part of org.openide.util.Utilities.actionsToPopup(). Special handling if action is instance of:<br>
     * - ContextAwareAction<br>
     * - Presenter.Popup<br>
     * <p>
     * If Presenter.Popup is implemented and the JMenuItem returned by getPopupPresenter()... :<br>
     * - has client property DynamicMenuContent.HIDE_WHEN_DISABLED, then no menu item is created if action is disabled.<br>
     * - is instance of DynamicContent, then use the result of item.getMenuPresenters() (JMenuItems, or JSeparators for null
     * values).
     *
     * @param action
     * @param context The context used for the action if it's a ContextAwareAction instance
     * @return A list of JMenuItems or JSeparators. Can be empty.
     */
    public static List<JComponent> actionToMenuItems(Action action, Lookup context)
    {
        if (action == null)
        {
            throw new IllegalArgumentException("action=" + action + " context=" + context);   //NOI18N
        }

        // switch to replacement action if there is some
        if (action instanceof ContextAwareAction)
        {
            Action contextAwareAction = ((ContextAwareAction) action).createContextAwareInstance(context);
            if (contextAwareAction == null)
            {
                throw new IllegalArgumentException("ContextAwareAction.createContextAwareInstance(context) returns null.");   //NOI18N
            } else
            {
                action = contextAwareAction;
            }
        }

        JMenuItem item;
        if (action instanceof Presenter.Popup)
        {
            item = ((Presenter.Popup) action).getPopupPresenter();
            if (item == null)
            {
                throw new IllegalArgumentException("getPopupPresenter() returning null for action=" + action);   //NOI18N
            }
        } else
        {
            // We need to correctly handle mnemonics with '&' etc.
            item = ActionPresenterProvider.getDefault().createPopupPresenter(action);
        }

        var res = new ArrayList<JComponent>();
        for (Component c : ActionPresenterProvider.getDefault().convertComponents(item))
        {
            if (c instanceof JMenuItem || c instanceof JSeparator)
            {
                res.add((JComponent) c);
            }
        }

        return res;

    }

    /**
     * Recursively enable/disable a JComponent and its JComponent children.
     *
     * @param b boolean
     * @param jc JComponent
     */
    public static void setRecursiveEnabled(boolean b, JComponent jc)
    {
        for (Component c : jc.getComponents())
        {
            if (c instanceof JComponent)
            {
                JComponent jjc = (JComponent) c;
                setRecursiveEnabled(b, jjc);
            }
        }
        jc.setEnabled(b);
    }

    public static Color calculateInverseColor(Color c)
    {
        Color nc;
        // nc = new Color( (c.getRed() < 128) ? 255 : 0, (c.getGreen() < 128) ? 255 : 0, (c.getBlue() < 128) ? 255 : 0);
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        float h = hsb[0] * 256;
        float s = hsb[1] * 256;
        float b = hsb[2] * 256;
        nc = (b < 50) ? Color.WHITE : Color.BLACK;
        return nc;
    }

    /**
     * Install a listener to automatically select all text when component gets the focus.
     *
     * @param comp
     */
    public static void installSelectAllWhenFocused(JTextComponent comp)
    {
        comp.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                comp.selectAll();
            }
        });
    }

    /**
     * Make the specified textComponent capture all ASCII printable key presses.
     * <p>
     * Key presses are used by an editable JTextComponent to display the chars, but it does not consume the key presses. So they
     * are transmitted up the containment hierarchy via the keybinding framework. This means a global Netbeans action might be
     * triggered if user types a global action shortcut (eg SPACE) in the JTextComponent.<p>
     * This method makes textComponent capture all ASCII printable key presses (ASCII char from 32 to 126) to avoid this
     * behaviour.
     * <p>
     *
     * @param textComponent
     */
    public static void installPrintableAsciiKeyTrap(JTextComponent textComponent)
    {
        // HACK ! 
        // Only way to block them is to capture all the printable keys
        // see https://docs.oracle.com/javase/tutorial/uiswing/misc/keybinding.html
        for (char c = 32; c <= 126; c++)
        {
            textComponent.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(c, 0), "doNothing");   //NOI18N
            textComponent.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(c, InputEvent.SHIFT_DOWN_MASK), "doNothing");   //NOI18N
        }
        textComponent.getActionMap().put("doNothing", NoActionInstance);

    }

    /**
     * Get a control-key KeyStroke which works on all OSes: Win, Linux AND Mac OSX.
     *
     * @param keyEventCode A KeyEvent constant like KeyEvent.VK_M (for ctrl-M)
     * @return
     */
    public static KeyStroke getGenericControlKeyStroke(int keyEventCode)
    {
        return KeyStroke.getKeyStroke(keyEventCode, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    }

    /**
     * Get a control-shift key KeyStroke which works on all OSes: Win, Linux AND Mac OSX.
     *
     * @param keyEventCode A KeyEvent constant like KeyEvent.VK_M (for ctrl-shift-M)
     * @return
     */
    public static KeyStroke getGenericAltKeyStroke(int keyEventCode)
    {
        return KeyStroke.getKeyStroke(keyEventCode, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    }

    public static Color calculateDisabledColor(Color c)
    {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[2] = Math.min(hsb[2] + 0.4f, 0.8f);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    /**
     * Change the font size of a menuBar and its submenus.
     *
     * @param menuBar
     * @param fontSizeOffset eg -2 (smaller) or +1.5 (bigger)
     */
    static public void changeMenuBarFontSize(MenuBar menuBar, float fontSizeOffset)
    {
        if (menuBar == null)
        {
            throw new NullPointerException("menuBar=" + menuBar + " fontSizeOffset=" + fontSizeOffset);   //NOI18N
        }
        for (int i = 0; i < menuBar.getMenuCount(); i++)
        {
            if (menuBar.getMenu(i) != null)
            {
                changeMenuFontSize(menuBar.getMenu(i), fontSizeOffset);
            }
        }
    }

    /**
     * Change the font size of a menu and of its components (which can be submenus).
     *
     * @param menu
     * @param fontSizeOffset
     */
    static public void changeMenuFontSize(JMenu menu, float fontSizeOffset)
    {
        changeFontSize(menu, fontSizeOffset);
        int nbMenuComponents = menu.getMenuComponentCount();
        for (int j = 0; j < nbMenuComponents; j++)
        {
            Component c = menu.getMenuComponent(j);
            if (c instanceof JMenu)
            {
                changeMenuFontSize((JMenu) c, fontSizeOffset);
            } else if (c != null)
            {
                changeFontSize(c, fontSizeOffset);
            }
        }
    }

    /**
     * Change the font size of a component.
     *
     * @param c
     * @param fontSizeOffset eg -2 (smaller) or +1.5 (bigger)
     */
    static public void changeFontSize(Component c, float fontSizeOffset)
    {
        if (c == null)
        {
            throw new NullPointerException("c=" + c + " fontSizeOffset=" + fontSizeOffset);   //NOI18N
        }
        Font f = c.getFont();
        if (f != null)
        {
            float newSize = Math.max(6, f.getSize() + fontSizeOffset);
            Font newFont = f.deriveFont(newSize);
            c.setFont(newFont);
        }
    }

    /**
     * Show the JFileChooser to select a directory.
     *
     * @param dirPath Initialize chooser with this directory.
     * @param title Title of the dialog.
     * @return The selected dir or null.
     */
    static public File showDirChooser(String dirPath, String title)
    {
        JFileChooser chooser = getFileChooserInstance();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
        chooser.setDialogTitle(title);
        File f = new File(dirPath);
        File parent = f.getParentFile();
        if (parent != null)
        {
            chooser.setCurrentDirectory(parent);
        }
        chooser.setSelectedFile(f);
        File newDir = null;
        if (chooser.showDialog(WindowManager.getDefault().getMainWindow(), "Select") == JFileChooser.APPROVE_OPTION)
        {
            newDir = chooser.getSelectedFile();
            if (newDir != null && !newDir.isDirectory())
            {
                newDir = null;
            }
        }
        return newDir;
    }

    /**
     * Installs a listener to receive notification when the text of any {@code JTextComponent} is changed.
     * <p>
     * Internally, it installs a {@link DocumentListener} on the text component's {@link Document}, and a
     * {@link PropertyChangeListener} on the text component to detect if the {@code Document} itself is replaced.
     * <p>
     * Usage: addChangeListener(someTextBox, e -> doSomething());
     * <p>
     * From Stackoverflow: https://stackoverflow.com/questions/3953208/value-change-listener-to-jtextfield
     *
     * @param text any text component, such as a {@link JTextField} or {@link JTextArea}
     * @param changeListener a listener to receieve {@link ChangeEvent}s when the text is changed; the source object for the
     * events will be the text component
     * @throws NullPointerException if either parameter is null
     */
    public static void addChangeListener(JTextComponent text, ChangeListener changeListener)
    {
        Objects.requireNonNull(text);
        Objects.requireNonNull(changeListener);


        DocumentListener dl = new DocumentListener()
        {
            private int lastChange = 0, lastNotifiedChange = 0;

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                lastChange++;
                SwingUtilities.invokeLater(() ->
                {
                    if (lastNotifiedChange != lastChange)
                    {
                        lastNotifiedChange = lastChange;
                        changeListener.stateChanged(new ChangeEvent(text));
                    }
                });
            }
        };


        text.addPropertyChangeListener("document", (PropertyChangeEvent e) ->
        {
            Document d1 = (Document) e.getOldValue();
            Document d2 = (Document) e.getNewValue();
            if (d1 != null)
            {
                d1.removeDocumentListener(dl);
            }
            if (d2 != null)
            {
                d2.addDocumentListener(dl);
            }
            dl.changedUpdate(null);
        });


        Document d = text.getDocument();
        if (d != null)
        {
            d.addDocumentListener(dl);
        }
    }

    // =================================================================================================
    // Static classes
    // =================================================================================================
    static private class NoAction extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            //do nothing
        }
    }
}
