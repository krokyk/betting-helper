/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.kroky.betting.db.DAO;
import org.kroky.betting.db.objects.Provider;

/**
 *
 * @author Kroky
 */
public class ProvidersTableModel extends AbstractTableModel {
    private static final Logger LOG = Logger.getLogger(ProvidersTableModel.class);
    private List<Provider> providers = new ArrayList<Provider>();

    private static final String[] COLUMN_NAMES = new String[] {
        "Country", "Sport", "League", "Season", "URL"
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
        return providers.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Provider provider = providers.get(rowIndex);
        switch(columnIndex) {
            case 0: return provider.getCountry();
            case 1: return provider.getSport();
            case 2: return provider.getLeague();
            case 3: return provider.getSeason();
            case 4: return provider.getUrl();
            default: throw new IndexOutOfBoundsException("Invalid column index: " + String.valueOf(columnIndex));
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 4;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object oldValue = getValueAt(rowIndex, columnIndex);
        if (oldValue != null && oldValue.equals(aValue)) {
            return;
        }
        String newValue = (String) aValue;
        Provider provider = providers.get(rowIndex);
        switch(columnIndex) {
            case 0: 
                provider.setCountry(newValue);
                break;
            case 1: 
                provider.setSport(newValue);
                break;
            case 2: 
                provider.setLeague(newValue);
                break;
            case 3: 
                provider.setSeason(newValue);
                break;
            default: throw new IndexOutOfBoundsException("Invalid column index: " + String.valueOf(columnIndex));
        }
        DAO.saveOrUpdateProvider(provider);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addProvider(Provider provider) {
        providers.add(provider);
        fireTableRowsInserted(providers.size() - 1, providers.size() - 1);
    }

    public Provider getProvider(int rowIndex) {
        return providers.get(rowIndex);
    }
    
    public void clear() {
        this.providers.clear();
        fireTableDataChanged();
    }
    
    public void addAll(Collection<Provider> providers) {
        this.providers.addAll(providers);
        fireTableDataChanged();
    }

    public void replaceAll(Collection<Provider> providers) {
        this.providers.clear();
        addAll(providers);
    }

    public void remove(Provider provider) {
        int index = providers.indexOf(provider);
        if(providers.remove(provider)) {
            fireTableRowsDeleted(index, index);
        }
    }
}
