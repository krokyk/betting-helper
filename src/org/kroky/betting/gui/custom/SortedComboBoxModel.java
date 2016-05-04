/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.gui.custom;

import java.util.Arrays;
import java.util.Comparator;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author pk2
 */
public class SortedComboBoxModel extends DefaultComboBoxModel {

    private Comparator comparator;

    /*
     * Static method is required to make sure the data is in sorted order before
     * it is added to the model
     */
    @SuppressWarnings("unchecked")
    protected static Object[] sortArray(Object[] items, Comparator comparator) {
        Arrays.sort(items, comparator);
        return items;
    }

    /*
     * Create an empty model that will use the natural sort order of the item
     */
    public SortedComboBoxModel() {
        super();
    }

    /*
     * Create an empty model that will use the specified Comparator
     */
    public SortedComboBoxModel(Comparator comparator) {
        super();
        this.comparator = comparator;
    }

    /*
     * Create a model with data and use the nature sort order of the items
     */
    public SortedComboBoxModel(Object[] items) {
        super(sortArray(items, null));
    }

    /*
     * Create a model with data and use the specified Comparator
     */
    public SortedComboBoxModel(Object[] items, Comparator comparator) {
        super(sortArray(items, comparator));
        this.comparator = comparator;
    }

    @Override
    public void addElement(Object element) {
        insertElementAt(element, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertElementAt(Object element, int index) {
        if(element == null) {
            return;
        }
        int size = getSize();

        for (index = 0; index < size; index++) {
            if(element.equals(getElementAt(index))) {
                return;
            }
        }
        
        //  Determine where to insert element to keep model in sorted order

        for (index = 0; index < size; index++) {
            if (comparator != null) {
                Object o = getElementAt(index);

                if (comparator.compare(o, element) > 0) {
                    break;
                }
            } else {
                Comparable c = (Comparable) getElementAt(index);

                if (c.compareTo(element) > 0) {
                    break;
                }
            }
        }

        super.insertElementAt(element, index);
    }
}
