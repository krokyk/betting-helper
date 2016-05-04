/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import javax.swing.SortOrder;
import org.kroky.betting.db.objects.Team;

/**
 *
 * @author Kroky
 */
public class DeletedTeamsTable extends EditableTable {
    
    DeletedTeamsTableModel model;

    public DeletedTeamsTable(DeletedTeamsTableModel model, int defaultSortColumn, SortOrder defaultSortOrder) {
        super(model, defaultSortColumn, defaultSortOrder);
        this.model = model;
    }
    
    public Team getSelectedTeam() {
        if(getSelectedRow() != -1) {
            return getTeam(getSelectedRow());
        }
        return null;
    }
    
    /**
     * Gets the correct oject from the underlying model regardles of sorting
     * @param index index of the row in the table
     * @return 
     */
    public Team getTeam(int index) {
        return model.getTeam(convertRowIndexToModel(index));
    }
    
    /**
     * Guarantees not null array returned
     * @return 
     */
    public Team[] getSelectedTeams() {
        int[] indices = getSelectedRows();
        Team[] teams = new Team[0];
        if(indices != null) {
            teams = new Team[indices.length];
            int i = 0;
            for(int index : indices) {
                teams[i++] = model.getTeam(convertRowIndexToModel(index));
            }
        }
        return teams;
    }

    public DeletedTeamsTableModel getTableModel() {
        return model;
    }
}
