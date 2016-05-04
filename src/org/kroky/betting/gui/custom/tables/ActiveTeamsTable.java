/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.awt.Component;
import javax.swing.SortOrder;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.kroky.betting.db.objects.Team;
import org.kroky.betting.gui.custom.Colors;

/**
 *
 * @author Kroky
 */
public class ActiveTeamsTable extends EditableTable {

    private ActiveTeamsTableModel model;
    
    private HighlightPredicate ifRecentlyUpdated = new HighlightPredicate() {

        @Override
        public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
            int modelIndex = adapter.convertRowIndexToModel(adapter.row);
            final Team team = model.getTeam(modelIndex);
            return team.isUpdatedRecently();
        }
    };
    
    public ActiveTeamsTable(ActiveTeamsTableModel model, int defaultSortColumn, SortOrder defaultSortOrder) {
        super(model, defaultSortColumn, defaultSortOrder);
        this.model = model;
        //trigger default sorting
        Highlighter recentlyUpdatedHighlighter = new ColorHighlighter(ifRecentlyUpdated, Colors.RECENTLY_UPDATED, null);
        addHighlighter(recentlyUpdatedHighlighter);
    }
    
    public int getIndexOf(Team team) {
        for(int index = 0; index < this.getRowCount(); index++) {
            if(getTeam(index).equals(team)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Gets the correct object from the underlying model regardless of sorting
     * @param index index of the row in the table
     * @return 
     */
    public Team getTeam(int index) {
        return model.getTeam(convertRowIndexToModel(index));
    }
}
