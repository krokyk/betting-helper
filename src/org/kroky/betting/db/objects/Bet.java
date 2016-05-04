/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db.objects;

import java.sql.Timestamp;
import org.kroky.betting.common.enums.BetStatus;
import static org.kroky.betting.common.enums.BetStatus.CANCELED;
import static org.kroky.betting.common.enums.BetStatus.LOST;
import static org.kroky.betting.common.enums.BetStatus.WON;
import org.kroky.betting.common.util.Utils;

/**
 *
 * @author Kroky
 */
public class Bet extends DBObject implements Comparable<Bet> {

    private Integer id;
    private Timestamp matchDate;
    private Team homeTeam;
    private Team awayTeam;
    private Double odds;
    private Double betValue;
    private Integer homeTeamGoals;
    private Integer awayTeamGoals;
    private int flStatus = -1;
    private Team activeTeam;
    private Timestamp created;
    private Timestamp updated;

    public Bet() {
    }

    public int getFlStatus() {
        return flStatus;
    }

    public void setFlStatus(int flStatus) {
        this.flStatus = flStatus;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Team getActiveTeam() {
        return activeTeam;
    }

    public void setActiveTeam(Team activeTeam) {
        this.activeTeam = activeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Integer getAwayTeamGoals() {
        return awayTeamGoals;
    }

    public void setAwayTeamGoals(Integer awayTeamGoals) {
        this.awayTeamGoals = awayTeamGoals;
    }

    public Integer getHomeTeamGoals() {
        return homeTeamGoals;
    }

    public void setHomeTeamGoals(Integer homeTeamGoals) {
        this.homeTeamGoals = homeTeamGoals;
    }

    /**
     *
     * @return <code>WON</code> if the bet was correct<br/>
     * <code>LOST</code> if the bet was incorrect
     * <code>CANCELED</code> if the match this bet was for was canceled
     * <code>ACTIVE</code> otherwise; this means that the bet is active and result is not yet available<br/>
     */
    public BetStatus getStatus() {
        return BetStatus.getStatus(getFlStatus());
    }

    public void setStatus(BetStatus status) {
        setFlStatus(status.getCode());
    }

    public Double getBetValue() {
        return betValue;
    }

    public void setBetValue(Double betValue) {
        this.betValue = betValue;
    }

    public Double getOdds() {
        return odds;
    }

    public void setOdds(Double odds) {
        this.odds = odds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Timestamp matchDate) {
        this.matchDate = matchDate;
    }

    @Override
    public int compareTo(Bet o) {
        int rv = getMatchDate().compareTo(o.getMatchDate());
        if (rv == 0) {
            rv = getUpdated().compareTo(o.getUpdated());
        }
        return rv;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Bet)) {
            return false;
        }
        final Bet other = (Bet) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(Utils.formatDateTime(getMatchDate())).append(" ");
        String homeTeamName = getHomeTeam().getName();
        String awayTeamName = getAwayTeam().getName();
        if (getActiveTeam().equals(getHomeTeam())) {
            homeTeamName = homeTeamName.toUpperCase();
        } else {
            awayTeamName = awayTeamName.toUpperCase();
        }
        sb.append(Utils.formatTwoDecimal(getBetValue())).append(" ").append(Utils.formatTwoDecimal(getOdds())).append(" ")
                .append(homeTeamName).append(" vs. ").append(awayTeamName).append(" ")
                .append(getStatus());
        return sb.toString();
    }

    public Double getWinnings() {
        switch (getStatus()) {
            case WON:
                return getOdds() * getBetValue();
            case LOST:
                return -1.0 * getBetValue();
            case CANCELED:
                return 0.0;
            default:
                return null;
        }
    }

    public Double getProfit() {
        switch (getStatus()) {
            case WON:
                return (getOdds() - 1.0) * getBetValue();
            case LOST:
                return -1.0 * getBetValue();
            case CANCELED:
                return 0.0;
            default:
                return null;
        }
    }

    @Override
    public String toStringForException() {
        return "Bet{" + "id=" + getId() + ", matchDate=" + getMatchDate() + ", homeTeam=" + getHomeTeam() + ", awayTeam=" + getAwayTeam() + ", odds=" + getOdds() + ", betValue=" + getBetValue() + ", homeTeamGoals=" + getHomeTeamGoals() + ", awayTeamGoals=" + getAwayTeamGoals() + ", flStatus=" + getFlStatus() + ", activeTeam=" + getActiveTeam() + ", created=" + getCreated() + ", updated=" + getUpdated() + '}';
    }

    @Override
    public String toHtml() {
        String date = Utils.formatDate(getMatchDate());
        String home = getHomeTeam().getName();
        String away = getAwayTeam().getName();
        if (getActiveTeam().getName().equals(home)) {
            home = "<b>" + home + "</b>";
        } else {
            away = "<b>" + away + "</b>";
        }
        StringBuilder sb = new StringBuilder(date).append("<br/>")
                .append(home).append(" vs. ").append(away).append("<br/>")
                .append("Odds: ").append(getOdds()).append("<br/>")
                .append("Value: ").append(getBetValue()).append("<br/>")
                .append("Winnings: ").append(Utils.formatTwoDecimal(getWinnings()));
        return sb.toString();
    }
}
