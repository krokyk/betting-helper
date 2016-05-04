/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db.objects.compositeIds;

import java.io.Serializable;
import org.kroky.betting.db.objects.DBObject;

/**
 *
 * @author Kroky
 */
public class TeamMappingId extends DBObject implements Serializable {
    private String externalName;
    private String sport;
    private String country;
    
    public TeamMappingId() {};
    
    public TeamMappingId(String externalName, String sport, String country) {
        this.externalName = externalName;
        this.sport = sport;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TeamMappingId)) {
            return false;
        }
        final TeamMappingId other = (TeamMappingId) obj;
        if ((this.getExternalName() == null) ? (other.getExternalName() != null) : !this.getExternalName().equals(other.getExternalName())) {
            return false;
        }
        if ((this.getSport() == null) ? (other.getSport() != null) : !this.getSport().equals(other.getSport())) {
            return false;
        }
        if ((this.getCountry() == null) ? (other.getCountry() != null) : !this.getCountry().equals(other.getCountry())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.getExternalName() != null ? this.getExternalName().hashCode() : 0);
        hash = 97 * hash + (this.getSport() != null ? this.getSport().hashCode() : 0);
        hash = 97 * hash + (this.getCountry() != null ? this.getCountry().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "TeamMappingId{" + "externalName=" + getExternalName() + ", sport=" + getSport() + ", country=" + getCountry() + "}";
    }

    @Override
    public String toStringForException() {
        return toString();
    }

    @Override
    public String toHtml() {
        return toStringForException();
    }

}
