/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db;

import java.util.HashMap;
import java.util.Map;
import org.kroky.betting.db.objects.DBObject;

/**
 *
 * @author Kroky
 */
public class DBState {
    private static Map<Class<? extends DBObject>, Boolean> updatesMap = new HashMap<Class<? extends DBObject>, Boolean>();
    private static Map<Class<? extends DBObject>, Boolean> additionsMap = new HashMap<Class<? extends DBObject>, Boolean>();
    private static Map<Class<? extends DBObject>, Boolean> removalsMap = new HashMap<Class<? extends DBObject>, Boolean>();
    static boolean generalUpdate = false;
    
    public static <T extends DBObject> void setUpdated(Class<T> c, boolean updated) {
        updatesMap.put(c, updated);
    }
    
    public static <T extends DBObject> boolean isUpdated(Class<T> c) {
        if(!updatesMap.containsKey(c)) {
            updatesMap.put(c, false);
        }
        return updatesMap.get(c);
    }
    
    public static <T extends DBObject> void setAdded(Class<T> c, boolean added) {
        additionsMap.put(c, added);
    }
    
    public static <T extends DBObject> boolean isAdded(Class<T> c) {
        if(!additionsMap.containsKey(c)) {
            additionsMap.put(c, false);
        }
        return additionsMap.get(c);
    }
    
    public static <T extends DBObject> void setRemoved(Class<T> c, boolean removed) {
        removalsMap.put(c, removed);
    }
    
    public static <T extends DBObject> boolean isRemoved(Class<T> c) {
        if(!removalsMap.containsKey(c)) {
            removalsMap.put(c, false);
        }
        return removalsMap.get(c);
    }
    
    public static boolean isGeneralUpdate() {
        return generalUpdate;
    }
}
