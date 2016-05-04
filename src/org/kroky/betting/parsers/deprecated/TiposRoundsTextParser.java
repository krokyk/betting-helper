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
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.kroky.betting.db.objects.Provider;
import org.kroky.betting.parsers.AbstractParser;
import org.kroky.betting.parsers.ParseResult;

/**
 *
 * @author Kroky
 */
public class TiposRoundsTextParser extends AbstractParser {
    
    private static final Class thisClass = TiposRoundsTextParser.class;
    
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(thisClass);
    
    //01/07 21:00 \tPortuguesa SP vs. Santos \t0:0 \t0:0
    private final Pattern PATTERN = Pattern.compile("(\\d\\d/\\d\\d)\\s+\\d+:\\d+\\s+(.*)(\\s+vs\\.\\s+)([^\\t]+)\\s+(\\d+):(\\d+)\\s+\\d+:\\d+");
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");
    
    public TiposRoundsTextParser() {
        super("Tipos Vy\u017erebovanie z\u00e1pasov (TEXT)");
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
                    String dateStr = matcher.group(1).trim();
                    Timestamp date;
                    try {
                        date = new Timestamp(DATE_FORMAT.parse(dateStr).getTime());
                        Calendar c = Calendar.getInstance();
                        int currentYear = c.get(Calendar.YEAR);
                        c.setTime(date);
                        c.set(Calendar.YEAR, currentYear);
                        date = new Timestamp(c.getTimeInMillis());
                    } catch (ParseException ex) {
                        throw new Exception("Could not parse date: \"" + dateStr + "\" using formatter: " + DATE_FORMAT.toPattern(), ex);
                    }
                    String homeTeamName = matcher.group(2).trim();
                    String awayTeamName = matcher.group(4).trim();
                    int goals1 = Short.parseShort(matcher.group(4).trim());
                    int goals2 = Short.parseShort(matcher.group(5).trim());
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
                //nothing to do
            }
        }
        message = lineCount + " lines, " + results.size() + " result(s), " + duplicates + " duplicates";
        LOG.info(message);
        return results;
    }
    
    @Override
    public Pattern getPattern() {
        return PATTERN;
    }
}
