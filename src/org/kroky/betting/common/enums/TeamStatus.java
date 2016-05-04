/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.enums;

/**
 *
 * @author Kroky
 */
public enum TeamStatus {
    REGULAR(0),
    ACTIVE(1),
    DELETED(2);
    
    private final int code;
    
    TeamStatus(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
    
    public static TeamStatus getStatus(int code) {
        switch(code) {
            case 1:  return ACTIVE;
            case 2:  return DELETED;
            default: return REGULAR;
        }
    }
}
