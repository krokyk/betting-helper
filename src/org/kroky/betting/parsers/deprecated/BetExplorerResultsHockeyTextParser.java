/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.parsers.deprecated;

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
public class BetExplorerResultsHockeyTextParser extends AbstractParser {
    
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(BetExplorerResultsHockeyTextParser.class);
    
    //DRAW: \t\t\tMinnesota Wild - Los Angeles Kings\t\t\t4:3 \tpen.\t\t\t3.03\t\t3.83\t\t2.03\t\t01.04.2012\t\t
    //REG:  \t\t\tMinnesota Wild - Los Angeles Kings\t\t\t4:5\t\t2.14\t\t3.84\t\t2.80\t\t01.04.2012\t\t
    private static final Pattern PATTERN = Pattern.compile("(.*)(\\s+-\\s+)([^\\t]+)\\s+([^\\t]+).*(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d)");
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    
    public BetExplorerResultsHockeyTextParser() {
        super("www.betexplorer.com results (TEXT)");
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
                    String awayTeamName = matcher.group(3).trim();
                    //this can happen if the match was canceled or postponed or abandoned or something else than X:Y is there
                    String[] goals = matcher.group(4).trim().split(":");
                    Integer goals1 = goals.length == 2 ? Integer.parseInt(goals[0]) : null;
                    Integer goals2 = goals.length == 2 ? Integer.parseInt(goals[1]) : null;
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
