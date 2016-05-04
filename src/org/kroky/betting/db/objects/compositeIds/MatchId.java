/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db.objects.compositeIds;

import java.io.Serializable;
import java.sql.Timestamp;
import org.kroky.betting.db.objects.DBObject;
import org.kroky.betting.db.objects.Team;

/**
 *
 * @author Kroky
 */
public class MatchId extends DBObject implements Serializable, Comparable<MatchId> {
    private Timestamp date;
    private Team homeTeam;
    private Team awayTeam;

    public MatchId() {};
    
    public MatchId(Timestamp date, Team homeTeam, Team awayTeam) {
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MatchId)) {
            return false;
        }
        final MatchId other = (MatchId) obj;
        if (this.getDate() != other.getDate() && (this.getDate() == null || !this.getDate().equals(other.getDate()))) {
            return false;
        }
        if (this.getHomeTeam() != other.getHomeTeam() && (this.getHomeTeam() == null || !this.getHomeTeam().equals(other.getHomeTeam()))) {
            return false;
        }
        if (this.getAwayTeam() != other.getAwayTeam() && (this.getAwayTeam() == null || !this.getAwayTeam().equals(other.getAwayTeam()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.getDate() != null ? this.getDate().hashCode() : 0);
        hash = 29 * hash + (this.getHomeTeam() != null ? this.getHomeTeam().hashCode() : 0);
        hash = 29 * hash + (this.getAwayTeam() != null ? this.getAwayTeam().hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(MatchId otherId) {
        int rv = getDate().compareTo(otherId.getDate());
        if(rv == 0) {
            rv = getHomeTeam().compareTo(otherId.getHomeTeam());
        }
        if(rv == 0) {
            rv = getAwayTeam().compareTo(otherId.getAwayTeam());
        }
        return rv;
    }

    @Override
    public String toString() {
        return "MatchId{" + "date=" + getDate() + ", homeTeam=" + getHomeTeam() + ", awayTeam=" + getAwayTeam() + '}';
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
