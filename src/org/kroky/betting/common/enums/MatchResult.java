/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.enums;

/**
 *
 * @author Kroky
 */
public enum MatchResult {
    NOT_DRAW(0),
    DRAW(1),
    CANCELED(-1);
    
    private final int code;
    
    MatchResult(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
    
    public static MatchResult getStatus(int code) {
        switch(code) {
            case 0:  return NOT_DRAW;
            case 1:  return DRAW;
            default: return CANCELED;
        }
    }
}
