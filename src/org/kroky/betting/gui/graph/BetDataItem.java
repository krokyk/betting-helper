/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.graph;

import org.jfree.data.xy.XYDataItem;
import org.kroky.betting.db.objects.Bet;

/**
 *
 * @author Kroky
 */
class BetDataItem extends XYDataItem {
    public static final XYDataItem EMPTY = new BetDataItem(0, 0, null);
    private final Bet bet;
    
    public BetDataItem(double x, double y, Bet bet) {
        super(x, y);
        this.bet = bet;
    }

    public BetDataItem(Number x, Number y, Bet bet) {
        super(x, y);
        this.bet = bet;
    }

    public Bet getBet() {
        return bet;
    }

}
