/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.parsers;

import java.sql.Timestamp;
import org.kroky.betting.common.enums.MatchResult;

/**
 *
 * @author pk2
 */
public interface ParseResult {

    /**
     * @return the date
     */
    Timestamp getDate();

    /**
     * @return the team1
     */
    String getHomeTeamName();

    /**
     * @return the team2
     */
    String getAwayTeamName();

    Integer getHomeGoals();
    
    Integer getAwayGoals();
    
    String getSport();
    
    String getCountry();
    
    public String getLeague();

    public String getSeason();
    
    public MatchResult getResult();

    //experimental
    public void setOdds(double odds);
    public double getOdds();
}
