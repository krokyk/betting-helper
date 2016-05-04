/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.enums;

/**
 *
 * @author Kroky
 */
public enum ProviderType {
    RESULTS(0),
    FIXTURES(1);
    
    private final int code;
    
    ProviderType(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
    
    public static ProviderType getType(int code) {
        switch(code) {
            case 0:  return RESULTS;
            default: return FIXTURES;
        }
    }
}
