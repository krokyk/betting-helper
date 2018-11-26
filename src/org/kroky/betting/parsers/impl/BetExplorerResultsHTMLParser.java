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
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.kroky.betting.common.util.Utils;
import org.kroky.betting.db.objects.Provider;
import org.kroky.betting.parsers.AbstractParser;
import org.kroky.betting.parsers.ParseResult;
import org.kroky.betting.parsers.Parser;

/**
 *
 * @author Kroky
 */
public class BetExplorerResultsHTMLParser extends AbstractParser {

    private static final Logger LOG = org.apache.log4j.Logger.getLogger(BetExplorerResultsHTMLParser.class);

    //<tr class="first-row"><td class="first-cell tl"><a href="matchdetails.php?matchid=fu02wHIO" onclick="win(this.href, 560, 500, 0, 1); return false;">All Boys - Boca Juniors</a></td><td class="result"><a href="matchdetails.php?matchid=fu02wHIO" onclick="win(this.href, 560, 500, 0, 1); return false;">3:1</a></td><td class="odds best-betrate" data-odd="3.43"></td><td class="odds" data-odd="3.16"></td><td class="odds" data-odd="2.08"></td><td class="last-cell nobr date">24.06.2012</td></tr>
    private static final Pattern PATTERN = Pattern.compile("class=\"in-match\"><span>(<strong>)?([^<]+)(</strong>)?<[^<]+<span>(<strong>)?([^<]+)(</strong>)?</span></a></td><td class=\"h-text-center\"><a[^>]+>([^<]+)</a>.+(\\d\\d\\.\\d\\d\\.\\d*|Today|Yesterday)</td></tr>");
    //<a\s+href\s*=\s*"[\./]*matchdetails\.php[^>]+>(.+)\s+-\s+([^<]+)<.*<a\s+href\s*=\s*"[\./]*matchdetails\.php[^>]+>([^<]+).*class="odds.*class="odds([^>]+).*class="odds.*(\d\d\.\d\d\.\d\d\d\d)
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat DATE_FORMAT_WITHOUT_YEAR = new SimpleDateFormat("dd.MM.");

    private static final Pattern ODDS_EXTRACT_PATTERN = Pattern.compile("data-odd=\"\\d+.\\d+\".+data-odd=\"(\\d+.\\d+)\".+data-odd=\"\\d+.\\d+\".+(\\d+\\.\\d+)");

    public BetExplorerResultsHTMLParser() {
        super("www.betexplorer.com results (HTML)");
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
            while ((line = br.readLine()) != null) {
                lineCount++;
                setProgressBarValue(lineCount);
                Matcher matcher = PATTERN.matcher(line);
                Provider newProvider = getProvider(line);
                if (newProvider != null) {
                    currentProvider = newProvider;
                }
                if (matcher.find()) {
                    String homeTeamName = matcher.group(2).trim();
                    String awayTeamName = matcher.group(5).trim();
                    //this can happen if the match was canceled or postponed or abandoned or something else than X:Y is there
                    //OR it is a draw but after PENalties or ExtraTime
                    String[] goals = matcher.group(7).split(":");
                    Integer goals1 = goals.length == 2 ? Integer.parseInt(goals[0].trim()) : null;
                    Integer goals2 = goals.length == 2 ? Integer.parseInt(goals[1].trim()) : null;
                    if (goals.length == 2 && goals[1].contains(" ")) {
                        if (line.contains(">ET<") || line.contains(">PEN.<")) {
                            if (goals1 < goals2) {
                                goals2 = goals1;
                            } else {
                                goals1 = goals2;
                            }
                        }
                    }

                    String dateStr = matcher.group(8).trim();
                    Calendar cal = Calendar.getInstance();
                    if ("Today".equals(dateStr)) {
                        dateStr = Utils.format(cal.getTime(), DATE_FORMAT);
                    } else if ("Yesterday".equals(dateStr)) {
                        cal.add(Calendar.DAY_OF_MONTH, -1);
                        dateStr = Utils.format(cal.getTime(), DATE_FORMAT);
                    } else if (dateStr.length() < 7) { //i.e. without year
                        dateStr = dateStr + cal.get(Calendar.YEAR);
                    } else {
                        //there is a full dd.MM.yyyy format found
                    }
                    Timestamp date;
                    try {
                        date = new Timestamp(DATE_FORMAT.parse(dateStr).getTime());
                    } catch (ParseException ex) {
                        try {
                            date = new Timestamp(DATE_FORMAT_WITHOUT_YEAR.parse(dateStr).getTime());
                        } catch (ParseException ex2) {
                            throw new Exception("Could not parse date: \"" + dateStr + "\" using formatters: " + DATE_FORMAT.toPattern() + " or " + DATE_FORMAT_WITHOUT_YEAR.toPattern(), ex2);
                        }
                    }
                    String country = null;
                    String sport = null;
                    String league = null;
                    String season = null;
                    if (currentProvider != null) {
                        country = currentProvider.getCountry();
                        sport = currentProvider.getSport();
                        league = currentProvider.getLeague();
                        season = currentProvider.getSeason();
                    }

                    Matcher m = ODDS_EXTRACT_PATTERN.matcher(line);
                    double odds;
                    if (m.find()) {
                        String oddsStr = m.group(1);
                        odds = Double.parseDouble(oddsStr);
                    } else {
                        odds = 3.0;
                    }

                    ParseResult result = new ParseResultImpl(date, homeTeamName, awayTeamName, goals1, goals2, country, sport, league, season);
                    result.setOdds(odds);
                    boolean change = results.add(result);
                    if (!change) {
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
