/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.kroky.betting.db.objects.TeamMapping;

/**
 *
 * @author pk2
 */
public class TeamMappingsTableModel extends AbstractTableModel {
    private List<TeamMapping> mappings = new ArrayList<TeamMapping>();
    private final boolean MANUAL;
    
    public TeamMappingsTableModel(boolean manual) {
        MANUAL = manual;
    }

    private static final String[] COLUMN_NAMES = new String[] {
        "Team", "Map To", "Country", "Sport", "League"
    };
    
    private static final Class<?>[] COLUMN_CLASSES = new Class<?>[] {
        String.class, String.class, String.class, String.class, String.class
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
        return mappings.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TeamMapping mapping = mappings.get(rowIndex);
        switch(columnIndex) {
            case 0: return mapping.getExternalName();
            case 1: return mapping.getInternalName();
            case 2: return mapping.getCountry();
            case 3: return mapping.getSport();
            case 4: return mapping.getLeague();
            default: throw new IndexOutOfBoundsException("Invalid column index: " + String.valueOf(columnIndex));
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return MANUAL ? true : (columnIndex == 1);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object oldValue = getValueAt(rowIndex, columnIndex);
        if (oldValue != null && oldValue.equals(aValue)) {
            return;
        }
        TeamMapping mapping = mappings.get(rowIndex);
        switch(columnIndex) {
            case 0: mapping.setExternalName(String.valueOf(aValue)); break;
            case 1: mapping.setInternalName(String.valueOf(aValue)); break;
            case 2: mapping.setSport(String.valueOf(aValue)); break;
            case 3: mapping.setCountry(String.valueOf(aValue)); break;
            case 4: mapping.setLeague(String.valueOf(aValue)); break;
            default: throw new IndexOutOfBoundsException("Invalid column index: " + String.valueOf(columnIndex));
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addRow(TeamMapping mapping) {
        mappings.add(mapping);
        fireTableRowsInserted(mappings.size() - 1, mappings.size() - 1);
    }

    public void remove(int index) {
        if (mappings.remove(index) != null) {
            fireTableRowsDeleted(index, index);
        }
    }

    public TeamMapping getMapping(int index) {
        return mappings.get(index);
    }

    public void remove(TeamMapping mapping) {
        int index = mappings.indexOf(mapping);
        if (mappings.remove(mapping)) {
            fireTableRowsDeleted(index, index);
        }
    }
}
