/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.kroky.betting.common.util.Utils;
import org.kroky.betting.db.DAO;
import org.kroky.betting.db.objects.Bet;
import org.kroky.betting.db.objects.Team;

/**
 *
 * @author Kroky
 */
public class BetsTableModel extends AbstractTableModel {
    private static final Logger LOG = Logger.getLogger(BetsTableModel.class);
    
    private List<Bet> bets = new ArrayList<Bet>();

    private static final String[] COLUMN_NAMES = new String[] {
        "Date", "Home Team", "Away Team", "HG", "AG", "Odds", "Bet", "Winnings"
    };
    
    private static final Class<?>[] COLUMN_CLASSES = new Class<?>[] {
        Timestamp.class, Team.class, Team.class, Integer.class, Integer.class, Double.class, Double.class, Double.class
    };

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
    @Override
    public int getRowCount() {
        return bets.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0 || columnIndex == 5 || columnIndex == 6;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object oldValue = getValueAt(rowIndex, columnIndex);
        if (oldValue != null && oldValue.equals(aValue)) {
            return;
        }
        Bet bet = bets.get(rowIndex);
        switch(columnIndex) {
            case 0: 
                bet.setMatchDate(Utils.timestamp(aValue));
                break;
            case 5: 
                bet.setOdds((Double) aValue);
                break;
            case 6: 
                bet.setBetValue((Double) aValue);
                break;
            default: throw new IllegalArgumentException("Column not editable: " + COLUMN_NAMES[columnIndex]);
        }
        bet.setUpdated(Utils.now());
        DAO.saveOrUpdateBet(bet, COLUMN_NAMES[columnIndex], aValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * You can acces <code>Bet</code> object by querying the 999th "column"
     * @param rowIndex
     * @param columnIndex
     * @return 
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Bet bet = bets.get(rowIndex);
        switch(columnIndex) {
            case 0: return bet.getMatchDate();
            case 1: return bet.getHomeTeam();
            case 2: return bet.getAwayTeam();
            case 3: return bet.getHomeTeamGoals();
            case 4: return bet.getAwayTeamGoals();
            case 5: return bet.getOdds();
            case 6: return bet.getBetValue();
            case 7: return bet.getWinnings();
            case 999: return bet;
            default: throw new IndexOutOfBoundsException("Invalid column index: " + String.valueOf(columnIndex));
        }
    }

    public void addBet(Bet bet) {
        bets.add(bet);
        fireTableRowsInserted(bets.size() - 1, bets.size() - 1);
    }
    
    public Bet getBet(int index) {
        return bets.get(index);
    }
    
    public void removeBet(Bet bet) {
        int index = bets.indexOf(bet);
        if (bets.remove(bet)) {
            fireTableRowsDeleted(index, index);
        }
    }

    public int getIndexOf(Bet bet) {
        return bets.indexOf(bet);
    }

    public void clear() {
        bets.clear();
        fireTableDataChanged();
    }

    public void replaceAll(List<Bet> bets) {
        this.bets.clear();
        this.bets.addAll(bets);
        fireTableDataChanged();
    }
    
    public void addAll(List<Bet> bets) {
        this.bets.addAll(bets);
        fireTableDataChanged();
    }

    public List<Bet> getBets() {
        return bets;
    }
}
