/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import com.citra.table.styles.Style;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SortOrder;
import static org.kroky.betting.common.enums.BetStatus.*;
import org.kroky.betting.db.objects.Bet;
import org.kroky.betting.gui.custom.Colors;

/**
 *
 * @author Kroky
 */
public class BetsTable extends GenericTable {

    private BetsTableModel model;
    
    public BetsTable(final BetsTableModel model, int defaultSortColumn, SortOrder defaultSortOrder) {
        super(model, defaultSortColumn, defaultSortOrder);
        this.model = model;
        
        //do not alternate colors
        setOddColor(Color.WHITE);
        setEvenColor(Color.WHITE);
        
        styleModel.addStyle(new Style() {

            @Override
            public void apply(Component c, JTable table, int row, int column) {
                if(table.isRowSelected(row)) {
                    c.setForeground(table.getSelectionForeground());
                    c.setBackground(table.getSelectionBackground());
                    return;
                } else {
                    c.setForeground(table.getForeground());
                }
                Bet bet = model.getBet(convertRowIndexToModel(row));
                switch(bet.getStatus()) {
                    case CANCELED: c.setBackground(Colors.BET_CANCELED); break;
                    case LOST: c.setBackground(Colors.BET_LOST); break;
                    case WON: c.setBackground(Colors.BET_WON); break;
                    default: break;
                }
            }
        });
    }
    
    public int getIndexOf(Bet bet) {
        for(int index = 0; index < this.getRowCount(); index++) {
            if(getBet(index).equals(bet)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Gets the correct oject from the underlying model regardles of sorting
     * @param index index of the row in the table view
     * @return 
     */
    public Bet getBet(int index) {
        return model.getBet(convertRowIndexToModel(index));
    }

    /**
     * Guarantees not null array returned
     * @return 
     */
    public Bet[] getSelectedBets() {
        int[] indices = getSelectedRows();
        Bet[] bets = new Bet[0];
        if(indices != null) {
            bets = new Bet[indices.length];
            int i = 0;
            for(int index : indices) {
                bets[i++] = model.getBet(convertRowIndexToModel(index));
            }
        }
        return bets;
    }

    public List<Bet> getBets() {
        return model.getBets();
    }
}
