/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.parsers;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JProgressBar;
import org.apache.log4j.Logger;
import org.kroky.betting.common.enums.MatchResult;
import org.kroky.betting.common.util.Utils;
import org.kroky.betting.db.objects.Provider;

/**
 *
 * @author Kroky
 */
public abstract class AbstractParser implements Parser, Comparable<Parser> {
    
    private static final Logger LOG = Logger.getLogger(AbstractParser.class);
    
    private final String name;
    private JProgressBar progressBar = new JProgressBar();
    
    protected AbstractParser(String name) {
        this. name = name;
    }

    void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    
    protected void setProgressBarValue(int value) {
        progressBar.setValue(value);
    }
    
    protected BufferedReader createBufferedReader(String text) {
        return new BufferedReader(new StringReader(text));
    }
    
    //<<<country|sport|league|season|url>>>
    private static final Pattern PROVIDER_PATTERN = Pattern.compile("<<<(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)>>>");
    /**
     * Return an array where 1st element is country, second is sport and third is league
     * @param line
     * @return 
     */
    protected Provider getProvider(String line) {
        Matcher matcher = PROVIDER_PATTERN.matcher(line);
        if (matcher.find()) {
            return new Provider(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(5), matcher.group(4), null);
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Parser o) {
        return name.compareToIgnoreCase(o.getName());
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    protected class ParseResultImpl implements ParseResult, Comparable<ParseResultImpl> {
        private Timestamp date;
        private String homeTeamName, awayTeamName, sport, country, league, season;
        private Integer homeGoals, awayGoals;
        private MatchResult result;
        
        private double odds;
        
        @Override
        public double getOdds() {
            return odds;
        }
        @Override
        public void setOdds(double odds) {
            this.odds = odds;
        }
        
        public ParseResultImpl(Timestamp date, String homeTeamName, String awayTeamName, String country, String sport, String league, String season) {
            this(date, homeTeamName, awayTeamName, null, null, country, sport, league, season);
        }

        public ParseResultImpl(Timestamp date, String homeTeamName, String awayTeamName, Integer homeGoals, Integer awayGoals, String country, String sport, String league, String season) {
            this.date = date;
            this.homeTeamName = homeTeamName;
            this.awayTeamName = awayTeamName;
            if(homeGoals != null && awayGoals != null) {
                result = homeGoals.intValue() == awayGoals.byteValue() ? MatchResult.DRAW : MatchResult.NOT_DRAW;
                this.homeGoals = homeGoals;
                this.awayGoals = awayGoals;
            } else {
                //for fixtures this is irrelevant
                result = MatchResult.CANCELED;
            }
            this.sport = sport;
            this.country = country;
            this.league = league;
            this.season = season;
        }

        @Override
        public String getLeague() {
            return league;
        }

        @Override
        public String getCountry() {
            return country;
        }

        @Override
        public String getSport() {
            return sport;
        }

        @Override
        public Integer getAwayGoals() {
            return awayGoals;
        }

        @Override
        public Integer getHomeGoals() {
            return homeGoals;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(Utils.formatDateTime(date));
            sb.append(" ").append(homeTeamName);
            if(!result.equals(MatchResult.CANCELED)) {
                sb.append(" ").append(homeGoals).append(":").append(awayGoals).append(" ");
            } else {
                sb.append(" vs. ");
            }
            sb.append(awayTeamName);
            if(!result.equals(MatchResult.CANCELED)) {
                sb.append(" ").append(result);
            }
            return sb.toString();
        }

        /**
        * @return the date
        */
        @Override
        public Timestamp getDate() {
            return date;
        }

        @Override
        public String getAwayTeamName() {
            return awayTeamName;
        }

        @Override
        public String getHomeTeamName() {
            return homeTeamName;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ParseResultImpl other = (ParseResultImpl) obj;
            if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
                return false;
            }
            if ((this.homeTeamName == null) ? (other.homeTeamName != null) : !this.homeTeamName.equals(other.homeTeamName)) {
                return false;
            }
            if ((this.awayTeamName == null) ? (other.awayTeamName != null) : !this.awayTeamName.equals(other.awayTeamName)) {
                return false;
            }
            return true;
        }
        
        

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + (this.date != null ? this.date.hashCode() : 0);
            hash = 47 * hash + (this.homeTeamName != null ? this.homeTeamName.hashCode() : 0);
            hash = 47 * hash + (this.awayTeamName != null ? this.awayTeamName.hashCode() : 0);
            return hash;
        }

        
        
        @Override
        public int compareTo(ParseResultImpl o) {
            int rv = getDate().compareTo(o.getDate());
            if (rv == 0) {
                rv = getHomeTeamName().compareTo(o.getHomeTeamName());
            }
            if (rv == 0) {
                rv = getAwayTeamName().compareTo(o.getAwayTeamName());
            }
            return rv;
        }

        @Override
        public String getSeason() {
            return season;
        }

        @Override
        public MatchResult getResult() {
            return result;
        }
    }
}
