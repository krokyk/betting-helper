/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

/**
 *
 * @author pk2
 */
public class Resources {
    private static final Class c = Resources.class;
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(c);
    private static final Map<String, Icon> ICONS = new HashMap<String, Icon>();
    private static final Map<String, Set<String>> SETS = new HashMap<String, Set<String>>();
    private static final Map<String, Map<String, String>> MAPS = new HashMap<String, Map<String, String>>();
    public static final String PREFIX = "/resources/";
    
    public static Icon getIcon(String resourceName) {
        if(!ICONS.containsKey(resourceName)) {
            LOG.debug("Loading resource: " + resourceName + " into an icon");
            ICONS.put(resourceName, new ImageIcon(c.getResource(PREFIX + resourceName)));
        }
        return ICONS.get(resourceName);
    }
    
    public static Icon getCheckIcon(int x, int y) {
        return getIcon("check_outlined-must_have_icons-" + x + "x" + y + ".png");
    }

    public static Icon getCanceledIcon(int x, int y) {
        return getIcon("canceled-must_have_icons-" + x + "x" + y + ".png");
    }
    
    public static Icon getErrorIcon(int x, int y) {
        return getIcon("error_triangle-" + x + "x" + y + ".png");
    }
    
    public static Icon getWarningIcon(int x, int y) {
        return getIcon("warning_triangle-" + x + "x" + y + ".png");
    }
    
    public static Icon getDeleteIcon(int x, int y) {
        return getIcon("delete-must_have_icons-" + x + "x" + y + ".png");
    }
    
    public static Icon getRestoreIcon(int x, int y) {
        return getIcon("undo-must_have_icons-" + x + "x" + y + ".png");
    }
    
    public static Icon getAddIcon(int x, int y) {
        return getIcon("add_green-must_have_icons-" + x + "x" + y + ".png");
    }
}
