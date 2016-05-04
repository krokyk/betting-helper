/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom.tables;

import java.util.Collection;
import javax.swing.SortOrder;
import org.kroky.betting.db.objects.Provider;

/**
 *
 * @author Kroky
 */
public class ProvidersTable extends EditableTable {
    
    private ProvidersTableModel model;

    public ProvidersTable(ProvidersTableModel model, int defaultSortColumn, SortOrder defaultSortOrder) {
        super(model, defaultSortColumn, defaultSortOrder);
        this.model = model;
    }
    
    /**
     * Guarantees not null array returned
     * @return 
     */
    public Provider[] getSelectedProviders() {
        int[] indices = getSelectedRows();
        Provider[] providers = new Provider[0];
        if(indices != null) {
            providers = new Provider[indices.length];
            int i = 0;
            for(int index : indices) {
                providers[i++] = model.getProvider(convertRowIndexToModel(index));
            }
        }
        return providers;
    }

    public void clear() {
        model.clear();
    }
    
    public void addProvider(Provider provider) {
        model.addProvider(provider);
    }

    public void replaceAll(Collection<Provider> providers) {
        model.replaceAll(providers);
    }

    public void removeProvider(Provider provider) {
        model.remove(provider);
    }
}
