/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.exceptions;

/**
 *
 * @author Kroky
 */
public class BettingRuntimeException extends RuntimeException {

    private String userFriendlyMessage;
    
    /**
     * 
     * @param message
     * @param cause
     * @param c which component to display alert dialog on
     */
    public BettingRuntimeException(String userFriendlyMessage, String message, Throwable cause) {
        super(message, cause);
        this.userFriendlyMessage = userFriendlyMessage;
    }

    public BettingRuntimeException(String userFriendlyMessage, String message) {
        super(message);
        this.userFriendlyMessage = userFriendlyMessage;
    }

    public String getUserFriendlyMessage() {
        return userFriendlyMessage;
    }

}
