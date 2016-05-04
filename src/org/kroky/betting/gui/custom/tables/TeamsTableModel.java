/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.kroky.betting.common.enums.MatchResult;
import org.kroky.betting.common.util.Resources;
import org.kroky.betting.common.util.Utils;
import org.kroky.betting.db.DAO;
import org.kroky.betting.db.objects.Match;
import org.kroky.betting.db.objects.Team;

/**
 *
 * @author Kroky
 */
public class TeamsTableModel extends AbstractTableModel {
    private static final Logger LOG = Logger.getLogger(TeamsTableModel.class);
    private List<Team> teams = new ArrayList<Team>();

    private static final String[] COLUMN_NAMES = new String[] {
        "Team", "Sport", "Country", "League", "No Draw", "Active"
    };
    
    private static final Class<?>[] COLUMN_CLASSES = new Class<?>[] {
        String.class, String.class, String.class, String.class, Integer.class, Icon.class
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
        return teams.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Team team = teams.get(rowIndex);
        switch(columnIndex) {
            case 0: return team.getName();
            case 1: return team.getSport();
            case 2: return team.getCountry();
            case 3: return team.getLeague();
            case 4: 
                int nonDrawCount = 0;
                Set<Match> sortedSetDesc = new TreeSet<Match>(team.getAllMatches()).descendingSet();
                for(Match r : sortedSetDesc) {
                    if(MatchResult.DRAW.equals(r.getResult())) {
                        break;
                    }
                    if(MatchResult.CANCELED.equals(r.getResult())) {
                        continue;
                    }
                    nonDrawCount++;
                }
                return nonDrawCount;
            case 5: return team.isActive() ? Resources.getCheckIcon(16, 16) : null;
            case 999: return team;
            default: throw new IndexOutOfBoundsException("Invalid column index: " + String.valueOf(columnIndex));
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0 || columnIndex == 3;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object oldValue = getValueAt(rowIndex, columnIndex);
        String newValue = Utils.isEmpty(aValue) ? null : (String) aValue;
        if (oldValue != null && oldValue.equals(newValue) || oldValue == null && newValue == null) {
            return;
        }
        Team team = teams.get(rowIndex);
        switch(columnIndex) {
            case 0: 
                team.setName(newValue);
                break;
            case 3: 
                team.setLeague(newValue);
                break;
            default: throw new IllegalArgumentException("Column not editable: " + COLUMN_NAMES[columnIndex]);
        }
        DAO.saveOrUpdateTeam(team, COLUMN_NAMES[columnIndex], newValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addTeam(Team newTeam) {
        teams.add(newTeam);
        int index = teams.size() - 1;
        fireTableRowsInserted(index, index);
    }

    public Team getTeam(int rowIndex) {
        return teams.get(rowIndex);
    }
    
    public void clear() {
        this.teams.clear();
        fireTableDataChanged();
    }
    
    public void addAll(List<Team> teams) {
        this.teams.addAll(teams);
        fireTableDataChanged();
    }
    
    public Team getTeam(String teamName) {
        for(Team team : teams) {
            if(team.getName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    public void replaceAll(List<Team> teams) {
        this.teams.clear();
        addAll(teams);
    }
}
