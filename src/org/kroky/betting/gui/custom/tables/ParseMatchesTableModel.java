/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author pk2
 */
public class ParseMatchesTableModel extends AbstractTableModel {
    private List<Object[]> rows = new ArrayList<Object[]>();

    private static final String[] COLUMN_NAMES = new String[] {
        "Date", "Home Team", "Away Team", "Draw", "Country", "Sport", "League", "Season"
    };
    
    private static final Class<?>[] COLUMN_CLASSES = new Class<?>[] {
        Timestamp.class, String.class, String.class, Icon.class, String.class, String.class, String.class, String.class
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
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] row = rows.get(rowIndex);
        return row[columnIndex];
    }

    public void addRow(Object[] row) {
        rows.add(row);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }    
}
