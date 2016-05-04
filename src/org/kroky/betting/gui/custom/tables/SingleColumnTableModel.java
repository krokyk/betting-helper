/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.util.Collection;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Kroky
 */
public class SingleColumnTableModel extends AbstractTableModel {
    
    private String[] items;

    public SingleColumnTableModel(Collection<?> items) {
        this.items = new String[items.size()];
        int i = 0;
        for (Iterator<? extends Object> it = items.iterator(); it.hasNext();) {
            this.items[i++] = it.next().toString();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    
    @Override
    public int getRowCount() {
        return items.length;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return items[rowIndex];
    }
    
    public String getValueAt(int index) {
        return items[index];
    }
}
