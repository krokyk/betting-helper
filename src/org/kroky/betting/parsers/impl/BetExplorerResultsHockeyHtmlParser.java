/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.parsers.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.kroky.betting.db.objects.Provider;
import org.kroky.betting.parsers.AbstractParser;
import org.kroky.betting.parsers.ParseResult;
import org.kroky.betting.parsers.Parser;

/**
 * 
 * @author Kroky
 */
public class BetExplorerResultsHockeyHtmlParser extends AbstractParser {
    
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(BetExplorerResultsHockeyHtmlParser.class);
    
    //DRAW: <tr class=""><td class="first-cell tl"><a href="../matchdetails.php?matchid=817477" onclick="win(this.href, 500, 500, 0, 1); return false;">Team1 - Team2</a></td><td>3:2 <span title="After Extra Time">ET</span></td><td class="odds">2.12</td><td class="odds best-betrate">3.80</td><td class="odds">2.87</td><td class="last-cell nobr date">08.04.2012</td></tr>
    //REG:  <tr class=""><td class="first-cell tl"><a href="../matchdetails.php?matchid=817477" onclick="win(this.href, 500, 500, 0, 1); return false;">Team1 - Team2</a></td><td>3:0</td><td class="odds best-betrate">1.64</td><td class="odds">4.28</td><td class="odds">4.13</td><td class="last-cell nobr date">08.04.2012</td></tr>
    //DRAW: <tr class=""><td class="first-cell tl"><a href="../matchdetails.php?matchid=817477" onclick="win(this.href, 500, 500, 0, 1); return false;">Team1 - Team2</a></td><td>4:3 <span title="After Penalties">pen.</span></td><td class="odds">1.96</td><td class="odds best-betrate">3.96</td><td class="odds">3.11</td><td class="last-cell nobr date">07.04.2012</td></tr>
    private static final Pattern PATTERN = Pattern.compile("<tr[^<]+<[^<]+<[^>]+>(.*)\\s+-\\s+(.*)<[^<]+<[^<]+<[^<]+(\\d+:\\d+)(.*)(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d)");
    private static final Pattern oddsPattern = Pattern.compile(">\\d+\\.\\d+<.*>(\\d+\\.\\d+)<.*>\\d+\\.\\d+<");
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    
    public BetExplorerResultsHockeyHtmlParser() {
        super("www.betexplorer.com results HOCKEY (HTML)");
        LOG.debug("Using pattern: " + PATTERN.pattern());
    }
    
    @Override
    public Set<ParseResult> parse(String text) {
        String message = "Parsing started";
        LOG.info(message);
        BufferedReader br = createBufferedReader(text);
        Set<ParseResult> results = new LinkedHashSet<ParseResult>();
        int duplicates = 0;
        int lineCount = 0;
        try {
            String line;
            Provider currentProvider = null;
            while((line = br.readLine()) != null) {
                lineCount++;
                setProgressBarValue(lineCount);
                Matcher matcher = PATTERN.matcher(line);
                Provider newProvider = getProvider(line);
                if(newProvider != null) {
                    currentProvider = newProvider;
                }
                if (matcher.find()) {
                    String homeTeamName = matcher.group(1).trim();
                    String awayTeamName = matcher.group(2).trim();
                    //this can happen if the match was canceled or postponed or abandoned or something else than X:Y is there
                    String[] goals = matcher.group(3).trim().split(":");
                    Integer goals1 = goals.length == 2 ? Integer.parseInt(goals[0]) : null;
                    Integer goals2 = goals.length == 2 ? Integer.parseInt(goals[1]) : null;
                    
                    String temp = matcher.group(4);
                    if ((temp.indexOf(">ET<") != -1 || temp.indexOf(">pen.<") != -1) && goals1 != null && goals2 != null) {
                        goals1 = Math.min(goals1, goals2);
                        goals2 = Math.min(goals1, goals2);
                    }
                    Matcher m = oddsPattern.matcher(temp);
                    double odds = 0.0;
                    if(m.find()) {
                        odds = Double.parseDouble(m.group(1));
                    }
                    odds = odds == 0.0 ? 3.5 : odds;
                    String dateStr = matcher.group(5).trim();
                    Timestamp date;
                    try {
                        date = new Timestamp(DATE_FORMAT.parse(dateStr).getTime());
                    } catch (ParseException ex) {
                        throw new Exception("Could not parse date: \"" + dateStr + "\" using formatter: " + DATE_FORMAT.toPattern(), ex);
                    }
                    String country = null;
                    String sport = null;
                    String league = null;
                    String season = null;
                    if(currentProvider != null) {
                        country = currentProvider.getCountry();
                        sport = currentProvider.getSport();
                        league = currentProvider.getLeague();
                        season = currentProvider.getSeason();
                    }
                    ParseResult result = new ParseResultImpl(date, homeTeamName, awayTeamName, goals1, goals2, country, sport, league, season);
                    result.setOdds(odds);
                    boolean change = results.add(result);
                    if(!change) {
                        duplicates++;
                        message = "Found duplicate: " + result;
                        LOG.debug(message);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Error occured during parsing.", ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                //Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        message = lineCount + " lines, " + results.size() + " result(s), " + duplicates + " duplicates";
        LOG.info(message);
        return results;
    }

    @Override
    public int compareTo(Parser o) {
        return super.compareTo(o);
    }
    
    @Override
    public Pattern getPattern() {
        return PATTERN;
    }
}
