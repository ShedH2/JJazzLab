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
package org.jjazz.analytics.api;

import org.jjazz.analytics.spi.AnalyticsProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import org.jjazz.upgrade.UpgradeManager;
import org.jjazz.upgrade.spi.UpgradeTask;
import org.json.JSONObject;
import org.openide.util.*;
import org.openide.util.lookup.ServiceProvider;

/**
 * Feature usage analytics methods.
 * <p>
 * The class acts as a centralized bridge to collect all feature analytics events and pass them to AnalyticsProcessor instances
 * present in the global lookup.
 */
public class Analytics
{

    private static final String PREF_JJAZZLAB_COMPUTER_ID = "JJazzLabComputerId";
    private static Analytics INSTANCE;

    private List<AnalyticsProcessor> processors;
    private static Preferences prefs = NbPreferences.forModule(Analytics.class);

    public static Analytics getInstance()
    {
        synchronized (Analytics.class)
        {
            if (INSTANCE == null)
            {
                INSTANCE = new Analytics();
            }
        }
        return INSTANCE;
    }

    private Analytics()
    {
        processors = new ArrayList<>(Lookup.getDefault().lookupAll(AnalyticsProcessor.class));
    }

    /**
     * Log a generic event with no properties.
     *
     * @param eventName
     */
    static public void logEvent(String eventName)
    {
        getInstance().processors.forEach(p -> p.logEvent(eventName));
    }

    /**
     * Generic event with properties.
     *
     * @param eventName
     * @param properties
     */
    static public void logEvent(String eventName, JSONObject properties)
    {
        getInstance().processors.forEach(p -> p.logEvent(eventName, properties));
    }

    /**
     * Update the properties of the current JJazzLab computer.
     * <p>
     *
     * @param properties
     * @see Analytics#getJJazzLabComputerId()
     */
    static public void setProperties(JSONObject properties)
    {
        getInstance().processors.forEach(p -> p.setProperties(properties));
    }

    /**
     * Update the properties of the current JJazzLab computer only if they are not already set.
     * <p>
     *
     * @param properties
     * @see Analytics#getJJazzLabComputerId()
     */
    static public void setPropertiesOnce(JSONObject properties)
    {
        getInstance().processors.forEach(p -> p.setPropertiesOnce(properties));
    }

    /**
     * Increment the properties of the current JJazzLab computer by the corresponding Long value.
     *
     * @param properties
     * @see Analytics#getJJazzLabComputerId()
     */
    static public void incrementProperties(Map<String, Long> properties)
    {
        getInstance().processors.forEach(p -> p.incrementProperties(properties));
    }

    /**
     * A unique and anonymous id computed when JJazzLab is run for the first time on a given computer.
     * <p>
     * The id is stored as a user preference, so it might be deleted if Netbeans user directory is deleted. If user upgrades to a
     * new version, the id is imported from the previous version settings.
     * <p>
     * Id is calculated from current time in milliseconds + a random number, converted to hexadecimal.
     *
     * @return
     */
    public String getJJazzLabComputerId()
    {
        String id = prefs.get(PREF_JJAZZLAB_COMPUTER_ID, null);
        if (id == null)
        {
            id = Long.toHexString(System.currentTimeMillis() + (long) (Math.random() * 100000));
            prefs.put(PREF_JJAZZLAB_COMPUTER_ID, id);
        }
        return id;
    }

    // =====================================================================================
    // Upgrade Task
    // =====================================================================================
    @ServiceProvider(service = UpgradeTask.class)
    static public class RestoreSettingsTask implements UpgradeTask
    {

        @Override
        public void upgrade(String oldVersion)
        {
            // Copy the PREF_JJAZZLAB_COMPUTER_ID preference if present
            UpgradeManager um = UpgradeManager.getInstance();
            um.duplicateOldPreferences(prefs);
        }

    }

}
