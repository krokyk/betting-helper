/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.exceptions;

import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.kroky.betting.common.util.Utils;

/**
 *
 * @author Kroky
 */
public class BettingExceptionHandler implements Thread.UncaughtExceptionHandler {
    
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(BettingExceptionHandler.class);
    
    public void handle(Throwable thrown) {
        // for EDT exceptions
        handleException(Thread.currentThread().getName(), thrown);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable thrown) {
        // for other uncaught exceptions
        handleException(thread.getName(), thrown);
    }

    protected void handleException(String tname, Throwable thrown) {
        LOG.error(thrown.getMessage(), thrown);
        Utils.showError(new JFrame(), thrown);
    }

    
}
