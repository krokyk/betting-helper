/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db.objects;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import static org.kroky.betting.common.enums.BetStatus.ACTIVE;
import static org.kroky.betting.common.enums.BetStatus.CANCELED;
import static org.kroky.betting.common.enums.BetStatus.LOST;
import static org.kroky.betting.common.enums.BetStatus.WON;
import org.kroky.betting.common.enums.TeamStatus;
import org.kroky.betting.common.util.Utils;

/**
 *
 * @author Kroky
 */
public class Team extends DBObject implements Comparable<Team> {

    private Integer id;
    private String name;
    private String sport;
    private String country;
    private int flStatus;
    private Provider fixturesProvider;
    private Timestamp resultUpdated;
    private Timestamp nextMatchDate;
    private String league;
    private Set<Match> homeMatches;
    private Set<Match> awayMatches;
    private Set<Bet> bets;

    public Team() {
    }

    ;

    public Team(String name, String sport, String country, String league) {
        this.name = name;
        this.sport = sport;
        this.country = country;
        this.flStatus = 0;
        this.league = league;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Set<Bet> getBets() {
        if (bets == null) {
            bets = new HashSet<Bet>();
        }
        return bets;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getFlStatus() {
        return flStatus;
    }

    public void setFlStatus(int flStatus) {
        this.flStatus = flStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getNextMatchDate() {
        return nextMatchDate;
    }

    public void setNextMatchDate(Timestamp nextMatchDate) {
        this.nextMatchDate = nextMatchDate;
    }

    public Provider getFixturesProvider() {
        return fixturesProvider;
    }

    public void setFixturesProvider(Provider fixturesProvider) {
        this.fixturesProvider = fixturesProvider;
    }

    public Timestamp getResultUpdated() {
        return resultUpdated;
    }

    public void setResultUpdated(Timestamp resultUpdated) {
        this.resultUpdated = resultUpdated;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setBets(Set<Bet> bets) {
        this.bets = bets;
    }

    public boolean isActive() {
        return getStatus().equals(TeamStatus.ACTIVE);
    }

    public Set<Match> getAwayMatches() {
        if (awayMatches == null) {
            awayMatches = new HashSet<Match>();
        }
        return awayMatches;
    }

    public void setAwayMatches(Set<Match> awayMatches) {
        this.awayMatches = awayMatches;
    }

    public Set<Match> getHomeMatches() {
        if (homeMatches == null) {
            homeMatches = new HashSet<Match>();
        }
        return homeMatches;
    }

    public void setHomeMatches(Set<Match> homeMatches) {
        this.homeMatches = homeMatches;
    }

    public boolean addHomeMatch(Match match) {
        return getHomeMatches().add(match);
    }

    public boolean addAwayMatch(Match match) {
        return getAwayMatches().add(match);
    }

    public Set<Match> getAllMatches() {
        Set<Match> set = new HashSet<Match>(getHomeMatches().size() + getAwayMatches().size());
        set.addAll(getHomeMatches());
        set.addAll(getAwayMatches());
        return set;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(Team o) {
        return getName().toString().compareTo(o.getName().toString());
    }

    public boolean addBet(Bet newBet) {
        return getBets().add(newBet);
    }

    public boolean hasActiveBets() {
        Iterator<Bet> it = new TreeSet<Bet>(getBets()).descendingSet().iterator();
        while (it.hasNext()) {
            Bet bet = it.next();
            if (bet.getStatus().equals(CANCELED)) {
                continue;
            }
            return ACTIVE.equals(bet.getStatus());
        }
        return false;
    }

    public boolean hasIncompleteChain() {
        Iterator<Bet> it = new TreeSet<Bet>(getBets()).descendingSet().iterator();
        while (it.hasNext()) {
            Bet bet = it.next();
            if (bet.getStatus().equals(CANCELED)) {
                continue;
            }
            return LOST.equals(bet.getStatus());
        }
        return false;
    }

    public boolean hasCompleteChain() {
        Iterator<Bet> it = new TreeSet<Bet>(getBets()).descendingSet().iterator();
        while (it.hasNext()) {
            Bet bet = it.next();
            if (bet.getStatus().equals(CANCELED)) {
                continue;
            }
            return WON.equals(bet.getStatus());
        }
        return true;
    }

    public boolean removeBet(Bet bet) {
        return getBets().remove(bet);
    }

    /**
     * Convenient method for <code>isUpdatedRecently(5)</code>
     * @return true if the team's bet was updated in the last 5 minutes, false otherwise
     */
    public boolean isUpdatedRecently() {
        return isUpdatedRecently(15);
    }

    /**
     * Tells if this team's bet was updated less than <code>mins</code> minutes ago
     * @param mins how many minutes to look back
     * @return true if the team's bet was updated in the last <code>mins</code> minutes, false otherwise
     */
    public boolean isUpdatedRecently(int mins) {
        final Timestamp updated = getResultUpdated();
        if (updated == null) {
            return false;
        }
        Calendar later = Calendar.getInstance();
        later.setTime(updated);
        later.add(Calendar.MINUTE, mins);
        return Utils.now().before(later.getTime());
    }

    public Bet getLastBet() {
        Iterator<Bet> it = new TreeSet<Bet>(getBets()).descendingSet().iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public Set<Bet> getCurrentBetChain() {
        Iterator<Bet> it = new TreeSet<Bet>(getBets()).descendingSet().iterator();
        TreeSet<Bet> currentChain = new TreeSet<Bet>();
        while (it.hasNext()) {
            Bet bet = it.next();
            if (bet.getStatus().equals(CANCELED)) {
                continue;
            }
            if (WON.equals(bet.getStatus())) {
                return currentChain;
            }
            currentChain.add(bet);
        }
        return currentChain;
    }

    public double getFirstBetValueInCurrentChain() {
        Iterator<Bet> it = getCurrentBetChain().iterator();
        if (it.hasNext()) {
            Bet bet = it.next();
            return bet.getBetValue();
        } else {
            return 0.0;
        }
    }

    public Set<Bet> getLastCompletedBetChain() {
        Iterator<Bet> it = new TreeSet<Bet>(getBets()).descendingSet().iterator();
        TreeSet<Bet> chain = new TreeSet<Bet>();
        if (it.hasNext()) {
            Bet bet = it.next();
            while ((!WON.equals(bet.getStatus()) || CANCELED.equals(bet.getStatus())) && it.hasNext()) {
                bet = it.next();
            }
            chain.add(bet);
            if (it.hasNext()) {
                bet = it.next();
                while (!WON.equals(bet.getStatus()) && it.hasNext()) {
                    if (bet.getStatus().equals(CANCELED)) {
                        continue;
                    }
                    chain.add(bet);
                    bet = it.next();
                }
            }
            if (WON.equals(bet.getStatus())) {
                return chain;
            }
        }
        return Collections.EMPTY_SET;
    }

    public String getControlText() {
        return "<<<" + getCountry() + "|" + getSport() + "|" + getLeague() + "|" + getFixturesProvider().getSeason() + "|" + getFixturesProvider().getUrl() + ">>>";
    }

    public TeamStatus getStatus() {
        return TeamStatus.getStatus(getFlStatus());
    }

    public void setStatus(TeamStatus status) {
        setFlStatus(status.getCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Team)) {
            return false;
        }
        final Team other = (Team) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toStringForException() {
        return "Team{" + "id=" + getId() + ", name=" + getName() + ", sport=" + getSport() + ", country=" + getCountry() + '}';
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder(getName()).append("<br/>")
                .append("Sport: ").append(getSport()).append("<br/>")
                .append("Country: ").append(getCountry()).append("<br/>")
                .append("League: ").append(getLeague()).append("<br/>");
        return sb.toString();
    }
}
