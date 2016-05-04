/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db.objects;

import java.sql.Timestamp;
import org.kroky.betting.common.enums.MatchResult;
import org.kroky.betting.common.exceptions.BettingRuntimeException;
import org.kroky.betting.common.util.Utils;
import org.kroky.betting.db.objects.compositeIds.MatchId;

/**
 *
 * @author Kroky
 */
public class Match extends DBObject implements Comparable<Match> {
    
    private MatchId id;
    private int flResult;
    private Integer homeGoals;
    private Integer awayGoals;
    private Timestamp updated;
    private String season;
    private String league;
    
    public Match() {};

    public Match(MatchId id, Integer homeGoals, Integer awayGoals, String season, String league, MatchResult result) {
        this.id = id;
        setResult(result);
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        //validation
        this.updated = Utils.now();
        this.season = season;
        this.league = league;
        if(homeGoals == null && awayGoals == null) {
            if(!MatchResult.CANCELED.equals(result)) {
                throw new BettingRuntimeException("Internal error in match creation", "Goals are null, but result is not CANCELED. " + toStringForException());
            }
        } else if (homeGoals != null && awayGoals == null || homeGoals == null && awayGoals != null) {
            throw new BettingRuntimeException("Internal error in match creation", "One of the goals is nul the other is not. " + toStringForException());
        } else if (homeGoals.equals(awayGoals)) {
            if(!MatchResult.DRAW.equals(result)) {
                throw new BettingRuntimeException("Internal error in match creation", "Goals are equal, but result is not DRAW. " + toStringForException());
            }
        } else if (!homeGoals.equals(awayGoals)) {
            if(!MatchResult.NOT_DRAW.equals(result)) {
                throw new BettingRuntimeException("Internal error in match creation", "Goals are not equal, but result is not NOT_DRAW. " + toStringForException());
            }
        }
    }
    
    public Match(Timestamp date, Team homeTeam, Team awayTeam, Integer homeGoals, Integer awayGoals, String season, String league, MatchResult result) {
        this(new MatchId(date, homeTeam, awayTeam), homeGoals, awayGoals, season, league, result);
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Team getHomeTeam() {
        return getId().getHomeTeam();
    }

    public Timestamp getDate() {
        return getId().getDate();
    }

    public Team getAwayTeam() {
        return getId().getAwayTeam();
    }

    public Integer getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    public Integer getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getFlResult() {
        return flResult;
    }

    public void setFlResult(int flResult) {
        this.flResult = flResult;
    }
    
    public MatchResult getResult() {
        switch(getFlResult()) {
            case -1: return MatchResult.CANCELED;
            case 0: return MatchResult.NOT_DRAW;
            case 1: return MatchResult.DRAW;
            default: throw new IllegalArgumentException("Unknown flag for bet[id=" + getId() + "] received from DB: " + getFlResult());
        }
    }

    public final void setResult(MatchResult result) {
        switch(result) {
            case NOT_DRAW: setFlResult(0); break;
            case DRAW: setFlResult(1); break;
            default: setFlResult(-1); break;
        }
    }

    public MatchId getId() {
        return id;
    }

    public void setId(MatchId id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(Utils.formatDateTime(getId().getDate()));
        sb.append(" ").append(getId().getHomeTeam()).append(" vs. ").append(getId().getAwayTeam());
        sb.append(" (").append(getResult()).append(")");
        return sb.toString();
    }
    
    @Override
    public int compareTo(Match o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Match)) {
            return false;
        }
        final Match other = (Match) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public final String toStringForException() {
        return "Match{" + "id=" + getId() + ", flResult=" + getFlResult() + ", homeGoals=" + getHomeGoals() + ", awayGoals=" + getAwayGoals() + ", updated=" + getUpdated() + ", season=" + getSeason() + ", league=" + getLeague() + '}';
    }

    @Override
    public final String toHtml() {
        String date = Utils.formatDate(getDate());
        String home = getHomeTeam().getName();
        String away = getAwayTeam().getName();
        StringBuilder sb = new StringBuilder(date).append("<br/>")
                .append(home).append(" vs. ").append(away).append("<br/>")
                .append("Result: ").append(getResult()).append("<br/>");
        return sb.toString();
    }
}
