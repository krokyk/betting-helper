/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db.objects;

/**
 *
 * @author Kroky
 */
public abstract class DBObject {
    @Override
    public abstract String toString();
    
    @Override
    public abstract boolean equals(Object o);
    
    @Override
    public abstract int hashCode();
    
    public abstract String toStringForException();
    public abstract String toHtml();
    
}
