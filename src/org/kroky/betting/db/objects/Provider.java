/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db.objects;

import org.kroky.betting.common.enums.ProviderType;

/**
 *
 * @author pk2
 */
public class Provider extends DBObject implements Comparable<Provider> {

    private String url;
    private String league;
    private String sport;
    private String country;
    private String season;
    private int flType;

    public Provider() {};

    public Provider(String country, String sport, String league, String url, String season, ProviderType type) {
        this.url = url;
        this.sport = sport;
        this.country = country;
        this.league = league;
        this.season = season;
        if(type != null) {
            setType(type);
        }
    }
    
    public ProviderType getType() {
        return ProviderType.getType(getFlType());
    }
    
    private void setType(ProviderType type) {
        setFlType(type.getCode());
    }

    /**
     * DON'T USE THIS DIRECTLY, USE THE getType()
     * @return 
     */
    public int getFlType() {
        return flType;
    }

    /**
     * DON'T USE THIS DIRECTLY, USE THE setType()
     * @return 
     */
    public void setFlType(int flType) {
        this.flType = flType;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Provider)) {
            return false;
        }
        final Provider other = (Provider) obj;
        if ((this.getUrl() == null) ? (other.getUrl() != null) : !this.getUrl().equals(other.getUrl())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.getUrl() != null ? this.getUrl().hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Provider o) {
        int rv = getCountry().compareToIgnoreCase(o.getCountry());
        if (rv == 0) {
            rv = getSport().compareToIgnoreCase(o.getSport());
        }
        if (rv == 0) {
            rv = getLeague().compareToIgnoreCase(o.getLeague());
        }
        return rv;
    }

    public String getControlText() {
        return "<<<" + getCountry() + "|" + getSport() + "|" + getLeague() + "|" + getSeason() + "|" + getUrl() + ">>>";
    }

    @Override
    public String toString() {
        return getSport() + ", " + getCountry() + ", League: " + getLeague() + ", Season: " + getSeason();
    }

    @Override
    public String toStringForException() {
        return "Provider{" + "url=" + getUrl() + ", league=" + getLeague() + ", sport=" + getSport() + ", country=" + getCountry() + ", season=" + getSeason() + ", flType=" + getFlType() + '}';
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder("Sport: ").append(getSport()).append("<br/>")
                .append("Country: ").append(getCountry()).append("<br/>")
                .append("League: ").append(getLeague()).append("<br/>")
                .append("Season: ").append(getSeason()).append("<br/>")
                .append("URL: ").append(getUrl());
        return sb.toString();
    }

}