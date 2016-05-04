/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.graph;

import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.kroky.betting.db.objects.Bet;

/**
 *
 * @author Kroky
 */
class BetTooltipGenerator implements XYToolTipGenerator {

    public BetTooltipGenerator() {
    }
    
    @Override
    public String generateToolTip(XYDataset dataset, int series, int item) {
        Bet bet = ((BetDataItem)((XYSeriesCollection)dataset).getSeries(series).getDataItem(item)).getBet();
        if(bet == null) {
            return null;
        }
        return new StringBuilder("<html>").append(bet.toHtml()).append("</html>").toString();
    }
    
}
