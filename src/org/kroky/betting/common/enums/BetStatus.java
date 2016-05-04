/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.enums;

/**
 *
 * @author Kroky
 */
public enum BetStatus {
    ACTIVE(-1),
    WON(1),
    LOST(0),
    CANCELED(2);
    
    private final int code;
    
    BetStatus(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
    
    public static BetStatus getStatus(int code) {
        switch(code) {
            case 0:  return LOST;
            case 1:  return WON;
            case 2:  return CANCELED;
            default: return ACTIVE;
        }
    }
}
