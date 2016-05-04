/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.kroky.betting.db.objects.Team;

/**
 *
 * @author Kroky
 */
public class ActiveTeamsTableModel extends AbstractTableModel {
    
    private List<Team> teams;
    private static final String[] COLUMN_NAMES = new String[] {
        "Team", "Sport", "Country", "League", "Next Match"
    };
    
    private static final Class<?>[] COLUMN_CLASSES = new Class<?>[] {
        Team.class, String.class, String.class, String.class, Timestamp.class
    };

    public ActiveTeamsTableModel(List<Team> teams) {
        this.teams = teams;
    }
    
    public ActiveTeamsTableModel() {
        teams = new ArrayList<Team>();
    }

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
            case 0: return team;
            case 1: return team.getSport();
            case 2: return team.getCountry();
            case 3: return team.getLeague();
            case 4: return team.getNextMatchDate();
            default: throw new IndexOutOfBoundsException("Invalid column index: " + String.valueOf(columnIndex));
        }
    }
    
    public Team getTeam(int index) {
        return teams.get(index);
    }

    public void addTeam(Team team) {
        if (teams.add(team)) {
            fireTableRowsInserted(teams.size() - 1, teams.size() - 1);
        }
    }
    
    public void removeTeam(Team team) {
        int index = teams.indexOf(team);
        if (teams.remove(team)) {
            fireTableRowsDeleted(index, index);
        }
    }

    public int getIndexOf(Team team) {
        return teams.indexOf(team);
    }

    public void clear() {
        this.teams.clear();
        fireTableDataChanged();
    }

    public void replaceAll(List<Team> teams) {
        this.teams.clear();
        addAll(teams);
    }
    
    public void addAll(List<Team> teams) {
        this.teams.addAll(teams);
        fireTableDataChanged();
    }

    public boolean exists(Team team) {
        return teams.contains(team);
    }

    public Team getTeam(String name) {
        for(Team team : teams) {
            if(team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
