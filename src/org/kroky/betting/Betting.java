/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting;

import java.beans.Beans;
import org.apache.log4j.Logger;
import org.kroky.betting.common.util.Utils;
import org.kroky.betting.gui.forms.MainFrame;

/**
 *
 * @author Kroky
 */
public class Betting {
    private static final Logger LOG = Logger.getLogger(MainFrame.class);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //get rid of the citra table popup
            Beans.setDesignTime(true);
            MainFrame.main(args);
        } catch (Throwable t) {
            LOG.error("Failed to start the application", t);
            Utils.showError(null, t);
            System.exit(1);
        }
    }
}
